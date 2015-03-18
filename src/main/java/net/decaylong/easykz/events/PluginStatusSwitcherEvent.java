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

package net.decaylong.easykz.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PluginStatusSwitcherEvent extends Event implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();
    private boolean cancel = false;
    private Player player;
    private boolean status;

    public PluginStatusSwitcherEvent(final Player who, Boolean status) {
        this.player = who;
        this.status = status;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public final Player getPlayer() {
        return player;
    }

    public boolean getStatus() {
        return status;
    }
}
