/**
 * Copyright (C) 2015 DecayLong.  All rights reserved.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation,  version 3.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.decaylong.easykz.commands;

import net.decaylong.easykz.EasyKz;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class EasyKzCommand {
    private EasyKz plugin;

    public EasyKzCommand(EasyKz easyKz) {
        plugin = easyKz;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if ("on".equalsIgnoreCase(args[0]) || "off".equalsIgnoreCase(args[0])) {
                    boolean switcher = "on".equalsIgnoreCase(args[0]);
                    plugin.setPluginStatusSwitcher(((Player) sender), switcher);
                    sender.sendMessage("[EasyKz status]: " + args[0]);
                    return true;
                } else if ("show".equalsIgnoreCase(args[0]) || "hide".equalsIgnoreCase(args[0])) {
                    boolean show = "show".equalsIgnoreCase(args[0]);
                    if (args.length == 1) {
                        //显示或隐藏所有玩家
                        List<Player> playerList = ((Player) sender).getWorld().getPlayers();
                        for (Player player : playerList) {
                            if (show) {
                                ((Player) sender).showPlayer(player);
                            } else {
                                ((Player) sender).hidePlayer(player);
                            }
                        }
                        return true;
                    } else if (args.length > 1) {
                        //显示或隐藏指定玩家
                        for (int i = 1; i <= args.length - 1; ++i) {
                            Player target = sender.getServer().getPlayerExact(args[i]);
                            if (target == null) {
                                sender.sendMessage("[EasyKz]: Player " + args[i] + " is not online!");
                                continue;
                            }
                            if (show) {
                                ((Player) sender).showPlayer(target);
                            } else {
                                ((Player) sender).hidePlayer(target);
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
        }
        sender.sendMessage("[EasyKz]: You aren't a player!");
        return false;
    }
}
