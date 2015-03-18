package net.decaylong.easykz.listeners;

import net.decaylong.easykz.events.PluginStatusSwitcherEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.UUID;


public class EasyKzPlayerListener implements Listener {

    //记录玩家的存点记录及当前存点索引
    private HashMap<UUID, LinkedList<Location>> locationsHashMap;
    private HashMap<UUID, Integer> locationIndexHashMap;

    //保存读点前的位置
    private HashMap<UUID, Location> undoTeleportHashMap;

    //插件状态开关
    private HashMap<UUID, Boolean> pluginStatusSwitcherHashMap;


    public EasyKzPlayerListener() {
        locationsHashMap = new LinkedHashMap<UUID, LinkedList<Location>>();
        locationIndexHashMap = new LinkedHashMap<UUID, Integer>();
        undoTeleportHashMap = new HashMap<UUID, Location>();
        pluginStatusSwitcherHashMap = new LinkedHashMap<UUID, Boolean>();
    }

    @EventHandler
    public void onPluginStatusSwitcher(PluginStatusSwitcherEvent event) {
        if (event.getStatus()) {
            event.getPlayer().getInventory().setHeldItemSlot(8);
        } else {
            clearPositions(event.getPlayer().getUniqueId());
        }
        setPluginStatusSwitcher(event.getPlayer().getUniqueId(), event.getStatus());
    }

    public void setPluginStatusSwitcher(UUID uuid, Boolean status) {
        pluginStatusSwitcherHashMap.put(uuid, status);
    }

    public boolean getPluginStatusSwitcher(UUID uuid) {
        if (pluginStatusSwitcherHashMap.containsKey(uuid)) {
            return pluginStatusSwitcherHashMap.get(uuid);
        }
        return false;
    }

    /**
     * 玩家加入世界，初始化插件状态开关为关闭
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        setPluginStatusSwitcher(event.getPlayer().getUniqueId(), false);
    }

    /**
     * 键盘数字按键切换工具栏项来触发相应的功能项
     * 1、存点
     * 2、读点
     * 3、上个存点
     * 4、下个存点
     * 5、撤销读点
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemHeldChanged(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (getPluginStatusSwitcher(player.getUniqueId())) {
            int index = event.getNewSlot();
            switch (index) {
                case 0:
                    checkPoint(player, player.getLocation());
                    break;
                case 1:
                    teleport(player);
                    break;
                case 2:
                    previousCheckPoint(player);
                    break;
                case 3:
                    nextCheckPoint(player);
                    break;
                case 4:
                    undoTeleport(player);
                    break;
            }
            player.getInventory().setHeldItemSlot(8);
        }
    }

    /**
     * 添加存点记录
     *
     * @param player
     * @param location
     */
    private void checkPoint(Player player, Location location) {
        if (((Entity) player).isOnGround()) {
            if (locationsHashMap.containsKey(player.getUniqueId())) {
                //为该玩家添加存点记录
                LinkedList<Location> locations = locationsHashMap.get(player.getUniqueId());
                int index = locationIndexHashMap.get(player.getUniqueId());
                if (locations.size() > index) {
                    for (int i = locations.size() - 1; i > index; --i) {
                        locations.remove(i);
                    }
                }
                locations.add(location);
                locationIndexHashMap.put(player.getUniqueId(), index + 1);
            } else {
                //无该玩家的存点记录则初始化
                LinkedList<Location> list = new LinkedList<Location>();
                list.add(location);
                locationsHashMap.put(player.getUniqueId(), list);
                setLocationIndexHashMap(player.getUniqueId(), 0);
            }
        } else {
            player.sendMessage("[EasyKz]:  Checkpoint not supported while in the air.");
        }
    }

    /**
     * 读取存点
     *
     * @param player
     */
    private void teleport(Player player) {
        if (locationIndexHashMap.containsKey(player.getUniqueId()) && locationsHashMap.containsKey(player.getUniqueId())) {
            setUndoTeleportHashMap(player.getUniqueId(), player.getLocation());
            int index = locationIndexHashMap.get(player.getUniqueId());
            Location location = locationsHashMap.get(player.getUniqueId()).get(index);
            player.teleport(location);
        }
    }

    /**
     * 读取上个存点
     *
     * @param player
     */
    private void previousCheckPoint(Player player) {
        if (locationIndexHashMap.containsKey(player.getUniqueId()) && locationsHashMap.containsKey(player.getUniqueId())) {
            setUndoTeleportHashMap(player.getUniqueId(), player.getLocation());
            int index = locationIndexHashMap.get(player.getUniqueId()) - 1;
            if (index < 0) {
                index = 0;
            }
            Location location = locationsHashMap.get(player.getUniqueId()).get(index);
            player.teleport(location);
            setLocationIndexHashMap(player.getUniqueId(), index);
        }
    }

    /**
     * 读取下个存点
     *
     * @param player
     */
    private void nextCheckPoint(Player player) {
        if (locationIndexHashMap.containsKey(player.getUniqueId()) && locationsHashMap.containsKey(player.getUniqueId())) {
            setUndoTeleportHashMap(player.getUniqueId(), player.getLocation());
            int index = locationIndexHashMap.get(player.getUniqueId()) + 1;
            int max = locationsHashMap.get(player.getUniqueId()).size() - 1;
            if (index > max) {
                index = max;
            }
            Location location = locationsHashMap.get(player.getUniqueId()).get(index);
            player.teleport(location);
            setLocationIndexHashMap(player.getUniqueId(), index);
        }
    }

    private void setLocationIndexHashMap(UUID uuid, int index) {
        locationIndexHashMap.put(uuid, index);
    }

    private void setUndoTeleportHashMap(UUID uuid, Location location) {
        undoTeleportHashMap.put(uuid, location);
    }

    /**
     * 撤销读点
     *
     * @param player
     */
    private void undoTeleport(Player player) {
        if (undoTeleportHashMap.containsKey(player.getUniqueId())) {
            player.teleport(undoTeleportHashMap.get(player.getUniqueId()));
        }
    }

    /**
     * 清理存点记录
     *
     * @param uuid
     */
    private void clearPositions(UUID uuid) {
        locationsHashMap.remove(uuid);
        locationIndexHashMap.remove(uuid);
        undoTeleportHashMap.remove(uuid);
    }

}
