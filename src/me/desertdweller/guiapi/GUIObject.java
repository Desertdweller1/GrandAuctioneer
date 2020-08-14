package me.desertdweller.guiapi;

import java.util.ArrayList;

public abstract class GUIObject {
	protected Object parent;
	protected InventoryVector position;
	protected String name;
	protected int layer;
	public ArrayList<GUIObject> gUIObjectList = new ArrayList<GUIObject>();
	
	public abstract void draw();
	
	public Object getParent() {
		return parent;
	}
	
	public GUIInventory getMainParent() {
		GUIObject curObject = this;
		while(true) {
			if(curObject.getParent() instanceof GUIInventory) {
				GUIInventory guiInv = (GUIInventory) curObject.getParent();
				return guiInv;
			}else {
				curObject = (GUIObject) curObject.getParent();
			}
		}
	}
	
	public InventoryVector getGlobalPos() {
		if(parent instanceof GUIInventory) {
			return position;
		}else if(parent instanceof GUIObject){
			GUIObject guiObjectParent = (GUIObject) parent;
			return position.add(guiObjectParent.getGlobalPos());
		}
		return new InventoryVector(0,0);
	}
	
	public void setParent(GUIObject parent) {
		this.parent = parent;
	}
	
	public void setParent(GUIInventory parent) {
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}
}
