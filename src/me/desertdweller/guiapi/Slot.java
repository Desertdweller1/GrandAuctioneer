package me.desertdweller.guiapi;

import org.bukkit.inventory.ItemStack;

public class Slot {
	private ItemStack item;
	private boolean clickable;
	private boolean animated;
	private ItemStack[] animatedList;
	private int ticksBeforeAnim;
	private int ticks = 0;
	
	public Slot(ItemStack item, boolean clickable) {
		this.item = item;
		this.clickable = clickable;
		animated = false;
	}
	
	public Slot(ItemStack item, boolean clickable, ItemStack[] animationList, int ticksBeforeAnim) {
		this.item = item;
		this.clickable = clickable;
		animated = true;
		this.animatedList = animationList;
		this.ticksBeforeAnim = ticksBeforeAnim;
	}
	
	public ItemStack getItem(){
		ticks++;
		if(animated && ticks == ticksBeforeAnim) {
			ticks = 0;
			for(int i = 0; i < animatedList.length; i++) {
				if(animatedList[i].isSimilar(item)) {
					return animatedList[i + 1];
				}
			}
		}
		return item;
	}
	
	public boolean isClickable() {
		return clickable;
	}
	
	public void setClickable(boolean bool) {
		clickable = bool;
	}
	
	public boolean equals(Slot slot) {
		if(item != null && slot.item != null && !item.equals(slot.item)) { //if they both have values, but do not match
			return false;
		}else if((item == null && slot.item != null) || (item != null && slot.item == null)) //If one is null, and the other is not
			return false;
		if(clickable != slot.clickable)
			return false;
		if(animated != slot.animated)
			return false;
		if(animatedList != null && slot.animatedList != null && !animatedList.equals(slot.animatedList))
			return false;
		return true;
	}
}
