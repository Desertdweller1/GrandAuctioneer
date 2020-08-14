package me.desertdweller.grandauctioneer.items;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.desertdweller.grandauctioneer.GrandAuctioneer;
import net.md_5.bungee.api.ChatColor;

public class AuctionItem {
	static Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);
	private ItemStack item;
	private UUID lister;
	private long timeOfListing;
	private ItemStack displayItem;
	private int price;
	private boolean expired;
	private Auctioneer curAuc;
	
	public AuctionItem(ItemStack item, UUID player, long timeOfListing, int price, boolean expired, Auctioneer aucLocated) {
		this.timeOfListing = timeOfListing;
		lister = player;
		this.item = item;
		this.price = price;
		this.expired = expired;
		curAuc = aucLocated;
	}
	
	//Finds out if this item should be available at the given auctioneer based on the time and distance from it.
	public boolean availableAtAuctioneer(Auctioneer auc) {
		double dist = 0;
		//Checks if it is in another world or not, if not then it uses the distance between points, otherwise it uses the config value.
		if(curAuc.location.getWorld().equals(auc.location.getWorld())) {
			dist = curAuc.location.distance(auc.location);
		}else {
			dist = plugin.getConfig().getDouble("worldDistance");
		}
		long secondsSincePost = (System.currentTimeMillis() - timeOfListing)/1000;
		//Rate of 1 block every x seconds. x = travelSpeed in config
		if(dist*plugin.getConfig().getInt("travelSpeed") < secondsSincePost) {
			return true;
		}
		return false;
	}
	 
	//Handles moving the item into retrieval and setting the owner to the buyer, as well as sending the seller their money in their retrieval.
	public boolean successfulBuy(Player p, Auctioneer newAuc) {
		if(!curAuc.removeItemFromAuction(this))
			return false;
		setExpired(false);
		int tax = 0;
		if(price > plugin.getConfig().getInt("maxStartFee")*10) {
			tax = (price/10) - plugin.getConfig().getInt("maxStartFee");
		}
		if(item.getItemMeta().hasDisplayName()) {
			curAuc.addItemToHeld(Auctioneer.getCurrencyPayment(price - tax, lister, curAuc, ChatColor.YELLOW + item.getItemMeta().getDisplayName()));
		}else {
			curAuc.addItemToHeld(Auctioneer.getCurrencyPayment(price - tax, lister, curAuc, ChatColor.YELLOW + WordUtils.capitalizeFully(item.getType().name().replace("_", " ").toLowerCase())));
		}
		lister = p.getUniqueId();
		curAuc = newAuc;
		curAuc.addItemToHeld(this);
		return true;
	}
	
	//Handles moving the listing to the retrieval list.
	public void cancelListing() {
		if(!curAuc.removeItemFromAuction(this))
			return;
		setExpired(true);
		curAuc.addItemToHeld(this);
	}
	
	public ItemStack retrieve() {
		curAuc.removeItemFromHeld(this);
		return item;
	}
	
	//Creates the item which will be displayed in the specific scenarios.
	public void generateDisplayItem() {
		displayItem = item.clone();
		ItemMeta meta = displayItem.getItemMeta();
		if(meta.hasDisplayName()) {
			meta.setDisplayName(ChatColor.YELLOW + item.getItemMeta().getDisplayName());
		}else {
			meta.setDisplayName(ChatColor.YELLOW + WordUtils.capitalizeFully(item.getType().name().replace("_", " ").toLowerCase()));
		}
		ArrayList<String> stringList = new ArrayList<String>();
		if(item.getItemMeta().hasLore()) {
			stringList.add(ChatColor.GRAY + "----------------------");
			stringList.addAll(item.getItemMeta().getLore());
			stringList.add(ChatColor.GRAY + "----------------------");
		}
		
		stringList.add(ChatColor.GRAY + "Listed By: " + ChatColor.GOLD + Bukkit.getOfflinePlayer(lister).getName());
		stringList.add(ChatColor.GRAY + "Price: " + ChatColor.GREEN + price + plugin.getConfig().getString("currencySuffix"));
		stringList.add(ChatColor.GRAY + "Listed " + (System.currentTimeMillis() - timeOfListing)/3600000 + " hours ago.");
		meta.setLore(stringList);
		displayItem.setItemMeta(meta);
	}
	public void generateRetrievalDisplayItem() {
		displayItem = item.clone();
		ItemMeta meta = displayItem.getItemMeta();
		if(meta.hasDisplayName()) {
			meta.setDisplayName(ChatColor.YELLOW + item.getItemMeta().getDisplayName());
		}else {
			meta.setDisplayName(ChatColor.YELLOW + WordUtils.capitalizeFully(item.getType().name().replace("_", " ").toLowerCase()));
		}
		ArrayList<String> stringList = new ArrayList<String>();
		if(item.getItemMeta().hasLore()) {
			stringList.add(ChatColor.GRAY + "----------------------");
			stringList.addAll(item.getItemMeta().getLore());
			stringList.add(ChatColor.GRAY + "----------------------");
		}
		if(expired) {
			stringList.add(ChatColor.GRAY + "EXPIRED");
			stringList.add(ChatColor.GRAY + "Priced at: " + ChatColor.GREEN + price + plugin.getConfig().getString("currencySuffix"));
			
		}else {
			stringList.add(ChatColor.GRAY + "Sold for: " + ChatColor.GREEN + price + plugin.getConfig().getString("currencySuffix"));
		}
		stringList.add(ChatColor.GRAY + "Deleting in " + (plugin.getConfig().getLong("maxTimeInRecieval") - (System.currentTimeMillis() - timeOfListing)/3600000) + " hours from now");
		stringList.add(ChatColor.GRAY + "if not claimed.");
		meta.setLore(stringList);
		displayItem.setItemMeta(meta);
	}
	public void generatePersonalViewItem() {
		displayItem = item.clone();
		ItemMeta meta = displayItem.getItemMeta();
		if(meta.hasDisplayName()) {
			meta.setDisplayName(ChatColor.YELLOW + item.getItemMeta().getDisplayName());
		}else {
			meta.setDisplayName(ChatColor.YELLOW + WordUtils.capitalizeFully(item.getType().name().replace("_", " ").toLowerCase()));
		}
		ArrayList<String> stringList = new ArrayList<String>();
		if(item.getItemMeta().hasLore()) {
			stringList.add(ChatColor.GRAY + "----------------------");
			stringList.addAll(item.getItemMeta().getLore());
			stringList.add(ChatColor.GRAY + "----------------------");
		}
		stringList.add(ChatColor.GRAY + "Priced at: " + ChatColor.GREEN + price + plugin.getConfig().getString("currencySuffix"));
		stringList.add(ChatColor.GRAY + "Expires in " + (plugin.getConfig().getLong("maxTimeAsListing") - (System.currentTimeMillis() - timeOfListing)/3600000) + " hours from now.");
		stringList.add(ChatColor.RED + "Click to remove. You will not be refunded the listing fee.");
		meta.setLore(stringList);
		displayItem.setItemMeta(meta);
	}
	
	//Getters and setters
	public ItemStack getItem() {
		return item;
	}
	public void setItem(ItemStack item) {
		this.item = item;
	}
	public UUID getListerUUID() {
		return lister;
	}
	public void setLister(UUID lister) {
		this.lister = lister;
	}
	public long getTimeOfListing() {
		return timeOfListing;
	}
	public void setTimeOfListing(long timeOfListing) {
		this.timeOfListing = timeOfListing;
	}
	public ItemStack getDisplayItem() {
		return displayItem;
	}
	public void setDisplayItem(ItemStack displayItem) {
		this.displayItem = displayItem;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	public Auctioneer getAuctioneer() {
		return curAuc;
	}
}
