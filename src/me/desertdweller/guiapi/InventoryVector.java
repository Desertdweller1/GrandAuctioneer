package me.desertdweller.guiapi;

public class InventoryVector {
	int x;
	int y;
	
	public InventoryVector(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static InventoryVector getVectorFromInt(int integer, int width) {
		return new InventoryVector(findRemainer(integer,width), integer/width);
	}
	
	public int getIntFromVector(int width) {
		return this.y*width + this.x;
	}
	
	public InventoryVector add(InventoryVector vector) {
		return new InventoryVector(x + vector.x,y + vector.y);
	}
	
	private static int findRemainer(int integer, int divisor) {
		while(integer >= divisor) {
			integer = integer - divisor;
		}
		return integer;
	}
	
	@Override
	public String toString() {
		return "InvVec{" + x + "," + y + "}";
	}
}
