package me.desertdweller.guiapi;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

/*
 * This class is a miniaturized version of GUIInventory, that can be put into the GUIInventory class, or even another SubInventory. 
 */
public class SubInventory extends GUIObject{
	private int width;
	private int height;
	private ArrayList<Slot> contents = new ArrayList<Slot>();
	
	/*
	 * This is the constructor for when its parent is the base GUIInventory
	 * position: The location it will be placed, (the top left of the area)
	 * width: The width of the inventory
	 * height: The height of the inventory
	 * name: Again does not matter, just there for easy identification purposes
	 */
	public SubInventory(GUIInventory parent, InventoryVector position, int width, int height, String name) {
		this.name = name;
		this.parent = parent;
		this.position = position;
		this.width = width;
		this.height = height;
		while(contents.size() < width*height) contents.add(null);
		layer = 1;
	}
	
	//This works exactly like the above constructor, except is intended for assigning it a parent of another SubInventory
	public SubInventory(SubInventory parent, InventoryVector position, int width, int height, String name) {
		this.name = name;
		this.parent = parent;
		this.position = position;
		this.width = width;
		this.height = height;
		while(contents.size() < width*height) contents.add(null);
		layer = parent.layer;
	}
	
	//Fills the entire inventory with the specified slot
	public void fill(Slot slot) {
		for(int i = 0; i < contents.size(); i++) {
			contents.set(i, slot);
		}
	}
	
	public void fillWithArray(ArrayList<Slot> slotArray, Slot replacable) {
		for(int i = 0; i < contents.size() && !slotArray.isEmpty(); i++) {
			if(contents.get(i) == null || contents.get(i).equals(replacable)) {
				contents.set(i, slotArray.get(0));
				slotArray.remove(slotArray.get(0));
			}
		}
	}
	
	public void fillWithArray(ArrayList<Slot> slotArray) {
		for(int i = 0; i < contents.size() && !slotArray.isEmpty(); i++) {
			if(contents.get(i) == null) {
				contents.set(i, slotArray.get(0));
				slotArray.remove(slotArray.get(0));
			}
		}
	}
	
	//Sets the specific point in the inventory to the specified slot.
	public void setSlot(InventoryVector vector, Slot slot) {
		int id = vector.getIntFromVector(width); //Finds the slot id from the x and y given
		if(id < contents.size()) {
			contents.set(id,slot);
		}
	}
	
	//Gets the slot at the point
	public ItemStack getSlot(InventoryVector vector) {
		int id = vector.getIntFromVector(width);
		return contents.get(id).getItem();
	}
	
	//Creates a new SubInventory with this as its parent.
	public SubInventory createSubInventory(InventoryVector position, int width, int height, String name) {
		gUIObjectList.add(new SubInventory(this,position,width,height,name));
		return (SubInventory) gUIObjectList.get(gUIObjectList.size()-1);
	}
	
	//Checks to see if the location in this inventory should be visible in the main inventory
	public boolean isWithinInventoryArea(InventoryVector vector) {
		if(vector.x >= 0 && vector.x <= this.width && vector.y >= 0 && vector.y <= this.height) {
			if(getParent() instanceof GUIInventory) {
				GUIInventory guiInv = (GUIInventory) getParent();
				return guiInv.isWithinInventoryArea(vector);
			}else if(getParent() instanceof SubInventory) {
				SubInventory subInv = (SubInventory) getParent();
				return subInv.isWithinInventoryArea(vector);
			}
		}
		return false;
	}

	//Adds the contents of this inventory, to the base GUIInventory contents. Then calls it's child GUIObjects draw functions.
	@Override
	public void draw() {
		for(int i = 0; i < contents.size(); i++) {
			GUIInventory guiInv = getMainParent();
			InventoryVector tempVec = InventoryVector.getVectorFromInt(i, this.width).add(position);
			if(isWithinInventoryArea(tempVec)) {
				guiInv.setSlot(tempVec, contents.get(i));
			}
		}
		for(GUIObject object : gUIObjectList) {
			object.draw();
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
