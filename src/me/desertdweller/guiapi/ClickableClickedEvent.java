package me.desertdweller.guiapi;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClickableClickedEvent extends Event implements Cancellable{
	private Slot slot;
	private InventoryVector vector;
	private GUIInventory gUIInv;
	private Player player;
	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();

	
	public ClickableClickedEvent(Slot slot, InventoryVector vector, GUIInventory gUIInv, Player player) {
		this.slot = slot;
		this.vector = vector;
		this.gUIInv = gUIInv;
		this.player = player;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}

	public Slot getSlot() {
		return slot;
	}

	public InventoryVector getVector() {
		return vector;
	}
	
	public GUIInventory getGUIInventory() {
		return gUIInv;
	}
	
	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
		
	}
}
