package net.decaylong.easykz;

import net.decaylong.easykz.commands.EasyKzCommand;
import net.decaylong.easykz.events.PluginStatusSwitcherEvent;
import net.decaylong.easykz.listeners.EasyKzPlayerListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class EasyKz extends JavaPlugin {
    private static Logger log;
    private EasyKzCommand easyKzCommandExecutor;

    @Override
    public void onEnable() {
        log = getLogger();
        easyKzCommandExecutor = new EasyKzCommand(this);
        getServer().getPluginManager().registerEvents(new EasyKzPlayerListener(), this);
    }

    public void setPluginStatusSwitcher(Player player, Boolean status) {
        getServer().getPluginManager().callEvent(new PluginStatusSwitcherEvent(player, status));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return easyKzCommandExecutor.onCommand(sender, command, label, args);
    }

    @Override
    public void onDisable() {
    }

    public static void logInfo(String info) {
        log.info(info);
    }


}
