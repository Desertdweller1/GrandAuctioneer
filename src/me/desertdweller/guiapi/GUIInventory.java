package me.desertdweller.guiapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class GUIInventory implements Listener{
	private static HashMap<GUIInventory, Player> openInventories = new HashMap<GUIInventory, Player>();
	private Inventory inv;
	private ArrayList<Slot> contents = new ArrayList<Slot>();
	public ArrayList<GUIObject> GUIObjects = new ArrayList<GUIObject>();
	private int rows;
	private boolean listenForClicks;
	private String name;
	
	public static void Init_( Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(new GUIInventory(), plugin);
	}
	
	//This constructor is meant for registering events
	public GUIInventory() {
		
	}
	
	
	/*
	* This constructor is for creating simple chest inventories with any number  rows.
	*
	* rows: The size of the inventory desired. 
	* name: The name of the inventory, this isn't important, it just allows a simple way of identifying certain invs. 
	* plugin: The plugin using the class. 
	* listenForClicks: Set to true if you want the inventory to be clickable - inventory click event is cancelled whether this is true or not.
	*/
	public GUIInventory(int rows, String name, Plugin plugin, boolean listenForClicks) {
		this.name = name;
		this.rows = rows;
		for(int i = 0; i < rows*9; i++) {
			contents.add(new Slot(new ItemStack(Material.AIR), false));
		}
		this.listenForClicks = listenForClicks;
		inv = plugin.getServer().createInventory(null, rows*9, name);
	}
	
	/*
	 * This constructor works much like the one above, only it allows for you to use other types of inventories. WIP
	 */
	
	public GUIInventory(String name, InventoryType type, Plugin plugin, boolean listenForClicks) {
		this.name = name;
		this.listenForClicks = listenForClicks;
		inv = plugin.getServer().createInventory(null, type, name);
		if(type.equals(InventoryType.ANVIL)) {
			for(int i = 0; i < 3; i++)
				contents.add(new Slot(new ItemStack(Material.AIR), false));
		}
	}
	
	//Opens this inventory for the specified player.
	public void openForPlayer(Player p) {
		p.openInventory(inv);
		openInventories.put(this, p);
	}
	
	
	//Updates the contents of the raw Bukkit inventory. Use this after all desired processes have finished to render to the player.
	public void render() {
		inv.clear();
		for(int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, contents.get(i).getItem());
		}
	}
	
	//Loops through its child GUIObjects, and runs their draw() functions, which is meant to add their items to the correct positions in for these contents.
	public void drawObjects() {
		for(GUIObject object : GUIObjects) {
			object.draw();
		}
	}
	
	//Fills the entire inventory with the specified slot
	public void fill(Slot slot) {
		for(int i = 0; i < contents.size(); i++) {
			contents.set(i, slot);
		}
	}
	
	//Sets the specific point in the inventory to the specified slot.
	public void setSlot(InventoryVector vector, Slot slot) {
		int id = vector.y*9 + vector.x; //Finds the slot id from the x and y given
		contents.set(id,slot);
	}
	
	//Gets the slot at the point.
	public ItemStack getSlot(InventoryVector vector) {
		int id = vector.y*9 + vector.x;
		return contents.get(id).getItem();
	}
	
	public GUIObject getObject(String name) {
		for(GUIObject ob : GUIObjects) {
			if(ob.getName().equals(name)) {
				return ob;
			}
		}
		return null;
	}

	public void setObject(String name, GUIObject ob) {
		for(int i = 0; i < GUIObjects.size(); i++) {
			if(GUIObjects.get(i).getName().equals(name)) {
				GUIObjects.set(i, ob);
				return;
			}
		}
		System.out.println("Changing an object of which does not exist!");
		new Exception().printStackTrace();
	}
	
	//Creates a miniature inventory within this inventory, allowing for layered interfaces
	public SubInventory createSubInventory(InventoryVector position, int width, int height, String name) {
		GUIObjects.add(new SubInventory(this,position,width,height,name));
		return (SubInventory) GUIObjects.get(GUIObjects.size()-1);
	}
	
	//Checks to make sure the given vector is within this inventory
	public boolean isWithinInventoryArea(InventoryVector vector) {
		if(vector.x >= 0 && vector.x <= 9 && vector.y >= 0 && vector.y <= this.rows) {
			return true;
		}
		return false;
	}
	
	//When a player clicks the inventory, this will find if the slot clicked was a designated clickable slot, and if so throws the ClickableClickedEvent()
	@EventHandler
	private static void onPlayerClick(InventoryClickEvent e) {
		if(e == null)
			return;
		Player p = (Player) e.getWhoClicked();
		GUIInventory gui = null;
		if(e.getClickedInventory() == null) {
			return;
		}
		if(e.getClickedInventory().equals(p.getOpenInventory().getBottomInventory())) {
			return;
		}
		//Checks that the player has a gui inv open
		if(openInventories.containsValue(p)) {
			for(GUIInventory key : openInventories.keySet()) {
				if(openInventories.get(key).equals(p)) {
					gui = key;
				}
			}
		}
		if(gui == null)
			return;
		
		//checks to see if gui slot is clickable
		if(gui.listenForClicks) {
			if(gui.contents.get(e.getSlot()).isClickable()) {
				ClickableClickedEvent event = new ClickableClickedEvent(gui.contents.get(e.getSlot()),InventoryVector.getVectorFromInt(e.getSlot(), 9), gui, p);
				Bukkit.getServer().getPluginManager().callEvent(event);
			}	
		}
		e.setCancelled(true);
	}
	
	//When an inventory belonging to a GUIInventory closes, this will remove the key from the hashmap
	@EventHandler
	private static void onInventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		GUIInventory gui = null;
		if(openInventories.containsValue(p)) {
			for(GUIInventory key : openInventories.keySet()) {
				if(openInventories.get(key).equals(p)) {
					gui = key;
				}
			}
		}
		if(gui == null)
			return;
		
		openInventories.remove(gui);
	}

	
	//Getters and setters
	
	public int getRows() {
		return rows;
	}

	public Inventory getInventory() {
		return inv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
