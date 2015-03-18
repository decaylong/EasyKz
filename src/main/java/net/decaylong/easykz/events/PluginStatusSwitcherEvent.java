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
