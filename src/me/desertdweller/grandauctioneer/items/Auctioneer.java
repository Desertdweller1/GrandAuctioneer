package me.desertdweller.grandauctioneer.items;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.itemnbtapi.NBTItem;
import me.desertdweller.grandauctioneer.Logger;

public class Auctioneer {
	//Items currently up for sale
	private ArrayList<AuctionItem> itemList;
	//Items that were bought or expired.
	private ArrayList<AuctionItem> heldItems;
	private String name;
	Location location;
	
	//Creates an auctioneer at the location
	public Auctioneer(Location location, String name) {
		this.location = location;
		this.setName(name);
		
		//This is where we will initialize the items stored and held in this auctioneer
		itemList = new ArrayList<AuctionItem>();
		heldItems = new ArrayList<AuctionItem>();
	}
	
	//Adds an item to the itemList
	public void addItem(ItemStack item, UUID p, int price) {
		itemList.add(new AuctionItem(item, p, System.currentTimeMillis(), price, false, this));
	}
	
	public void addItem(AuctionItem item) {
		itemList.add(item);
	}

	//Removes the specified item from the auction
	public boolean removeItemFromAuction(AuctionItem item) {
		return itemList.remove(item);
	}
	
	public void removeAllFromAuction() {
		itemList.clear();
	}
	
	public void removePlayerAuctions(Player p) {
		@SuppressWarnings("unchecked")
		ArrayList<AuctionItem> cloneList = (ArrayList<AuctionItem>) itemList.clone();
		for(AuctionItem item : cloneList) {
			if(item.getListerUUID().equals(p.getUniqueId())) {
				itemList.remove(item);
			}
		}
		
	}
	
	//Adds the item to the retrieval list.
	public void addItemToHeld(AuctionItem item) {
		item.setTimeOfListing(System.currentTimeMillis());
		heldItems.add(item);
	}
	
	//Removes the item from the retrieval list.
	public boolean removeItemFromHeld(AuctionItem item) {
		return heldItems.remove(item);
	}
	
	public void removeAllFromHeld() {
		heldItems.clear();
	}
	
	public void removePlayerHeld(Player p) {
		@SuppressWarnings("unchecked")
		ArrayList<AuctionItem> cloneList = (ArrayList<AuctionItem>) heldItems.clone();
		for(AuctionItem item : cloneList) {
			if(item.getListerUUID().equals(p.getUniqueId())) {
				heldItems.remove(item);
			}
		}
	}
	
	//Generates an item with a description of the transaction, that when picked up by the player, automatically turns into gems.
	public static AuctionItem getCurrencyPayment(int amount, UUID p, Auctioneer auc, String name) {
		ItemStack item = new ItemStack(Material.EMERALD);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("Material", "GemBag");
		nbti.setInteger("Value", amount);
		item = nbti.getItem();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		Logger.addLog(amount + " currency has been placed in " + p + "'s retrieval at auctioneer " + auc.getName());
		return new AuctionItem(item, p, System.currentTimeMillis(), amount, false, auc);
	}
	
	//Getters and setters
	public ArrayList<AuctionItem> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<AuctionItem> itemList) {
		this.itemList = itemList;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ArrayList<AuctionItem> getHeldItems() {
		return heldItems;
	}

	public void setHeldItems(ArrayList<AuctionItem> heldItems) {
		this.heldItems = heldItems;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
