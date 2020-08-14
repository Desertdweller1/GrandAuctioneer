package me.desertdweller.grandauctioneer.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.desertdweller.grandauctioneer.GrandAuctioneer;
import me.desertdweller.grandauctioneer.Logger;
import me.desertdweller.grandauctioneer.items.AuctionItem;
import me.desertdweller.grandauctioneer.items.Auctioneer;
import me.desertdweller.guiapi.ClickableClickedEvent;
import me.desertdweller.guiapi.GUIInventory;
import me.desertdweller.guiapi.InventoryVector;
import me.desertdweller.guiapi.Slot;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class AddListingPage implements Listener{
	static Economy econ;
	static Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);
	private GUIInventory inv;
	Player player;
	Auctioneer auc;
	private int price = 10;
	int selectedSlotID = -1;
	
	public AddListingPage(Player p, Auctioneer auc){
		econ = GrandAuctioneer.getEconomy();
		player = p;
		if(player == null || auc == null)
			return;
		this.auc = auc;
		inv = new GUIInventory(2, "Add a Listing", plugin, true);
		load();
		inv.openForPlayer(player);
	}
	
	private void load() {
		inv.fill(GUIItems.getFiller());
		inv.setSlot(new InventoryVector(4,0), new Slot(new ItemStack(Material.AIR), false));
		if(selectedSlotID != -1)
			inv.setSlot(new InventoryVector(4,0), new Slot(player.getInventory().getItem(selectedSlotID), false));
		inv.setSlot(new InventoryVector(7,1), getIncreaseHundred());
		inv.setSlot(new InventoryVector(6,1), getIncreaseTwenty());
		inv.setSlot(new InventoryVector(5,1), getIncreaseFive());
		inv.setSlot(new InventoryVector(4,1), getPriceRelay());
		inv.setSlot(new InventoryVector(3,1), getDecreaseFive());
		inv.setSlot(new InventoryVector(2,1), getDecreaseTwenty());
		inv.setSlot(new InventoryVector(1,1), getDecreaseHundred());
		inv.setSlot(new InventoryVector(0,0), GUIItems.getBackButton());
		inv.render();
	}
	
	private Slot getPriceRelay() {
		ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Sell this item for " + ChatColor.GREEN + price + ChatColor.YELLOW + " gems?");
		int marketFee = price/10;
		int taxFee = 0;
		if(marketFee > plugin.getConfig().getInt("maxStartFee")) {
			taxFee = marketFee - plugin.getConfig().getInt("maxStartFee");
			marketFee = 30;
		}
		List<String> temp = new ArrayList<String>();
		temp.add(ChatColor.GRAY +  "This will cost you " + marketFee + plugin.getConfig().getString("currencySuffix") + " upfront,");
		temp.add(ChatColor.GRAY +  "and an additional " + taxFee + plugin.getConfig().getString("currencySuffix") + " taken from");
		temp.add(ChatColor.GRAY +  "the return of the sale, if it sells.");
		temp.add(" ");
		temp.add(ChatColor.GRAY +  "Click to confirm. You can cancel at any time,");
		temp.add(ChatColor.GRAY +  "but you will not be refunded your money.");
		meta.setLore(temp);
		item.setItemMeta(meta);
		return new Slot(item,true);
	}
	
	private static Slot getDecreaseFive() {
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Decrease price by 5" + plugin.getConfig().getString("currencySuffix") + ".");
		item.setItemMeta(meta);
		return new Slot(item,true);
	}
	
	private static Slot getDecreaseTwenty() {
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Decrease price by 20" + plugin.getConfig().getString("currencySuffix") + ".");
		item.setItemMeta(meta);
		return new Slot(item,true);
	}
	
	private static Slot getDecreaseHundred() {
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Decrease price by 100" + plugin.getConfig().getString("currencySuffix") + ".");
		item.setItemMeta(meta);
		return new Slot(item,true);
	}
	
	private static Slot getIncreaseFive() {
		ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Increase price by 5" + plugin.getConfig().getString("currencySuffix") + ".");
		item.setItemMeta(meta);
		return new Slot(item,true);
	}
	
	private static Slot getIncreaseTwenty() {
		ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Increase price by 20" + plugin.getConfig().getString("currencySuffix") + ".");
		item.setItemMeta(meta);
		return new Slot(item,true);
	}
	
	private static Slot getIncreaseHundred() {
		ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Increase price by 100" + plugin.getConfig().getString("currencySuffix") + ".");
		item.setItemMeta(meta);
		return new Slot(item,true);
	}
	
	@EventHandler
	private static void onClickableSlotClicked(ClickableClickedEvent e) {
		if(!GUIManager.openGUIs.containsKey(e.getPlayer()) || !(GUIManager.openGUIs.get(e.getPlayer()) instanceof AddListingPage)) {
			return;
		}
		AddListingPage listing = (AddListingPage) GUIManager.openGUIs.get(e.getPlayer());
		Auctioneer auc = listing.auc;
		Player player = listing.player;
		
		if(e.getSlot().equals(getDecreaseFive())) {
			listing.price -= 5;
		}else if(e.getSlot().equals(getDecreaseTwenty())) {
			listing.price -= 20;
		}else if(e.getSlot().equals(getDecreaseHundred())) {
			listing.price -= 100;
		}else if(e.getSlot().equals(getIncreaseFive())) {
			listing.price += 5;
		}else if(e.getSlot().equals(getIncreaseTwenty())) {
			listing.price += 20;
		}else if(e.getSlot().equals(getIncreaseHundred())) {
			listing.price += 100;
		}else if(e.getSlot().equals(listing.getPriceRelay()) && listing.selectedSlotID != -1 && listing.player.getInventory().getItem(listing.selectedSlotID) != null) {
			int tax = listing.price/10;
			if(tax > plugin.getConfig().getInt("maxStartFee"))
				tax = plugin.getConfig().getInt("maxStartFee");
			if(!econ.has(listing.player, tax)) {
				e.setCancelled(true);
				listing.player.sendMessage(ChatColor.RED + "You cannot afford the listing fee.");
				return;
			}
			Logger.addLog(e.getPlayer() + " put up " + player.getInventory().getItem(listing.selectedSlotID).getItemMeta().getDisplayName() + " (" + listing.player.getInventory().getItem(listing.selectedSlotID).getType() + ") for " + listing.price + " at auctioneer " + listing.auc.getName());
			//Saves the selected item, in case this fails.
			ItemStack savedItem = player.getInventory().getItem(listing.selectedSlotID);
			AuctionItem savedAItem = new AuctionItem(player.getInventory().getItem(listing.selectedSlotID), player.getUniqueId(),System.currentTimeMillis(), listing.price, false, auc);
			//Add the selected item to the Auctioneer
			auc.addItem(savedAItem);
			//Takes the item from the player's inventory
			e.getPlayer().getInventory().setItem(listing.selectedSlotID, null);
			//Ensures that the player still has enough currency, if not rolls everything back
			if(!econ.has(player, tax)) {
				e.getPlayer().getInventory().setItem(listing.selectedSlotID, savedItem);
				auc.removeItemFromAuction(savedAItem);
				e.getPlayer().sendMessage(ChatColor.RED + "You cannot afford the listing fee.");
			}else {
				//Takes currency from the player
				econ.withdrawPlayer(player, tax);
				GUIManager.refreshGUIs();
				//Opens the Personal Results GUI
				GUIManager.openResultsPage(player, ResultsType.PERSONAL, null, auc);
				
			}
			e.setCancelled(true);
			return;
		}else if(e.getSlot().equals(GUIItems.getBackButton())) {
			GUIManager.openGrandAuction(listing.player, listing.auc);
		}
		if(listing.price < 10) {
			listing.price = 10;
		}
		listing.load();
		e.setCancelled(true);
	}
	
	@EventHandler
	private static void onInvClick(InventoryClickEvent e) {
		//Makes sure that this inventory is an AddListingPage gui
		if(!GUIManager.openGUIs.containsKey((Player) e.getWhoClicked()) || !(GUIManager.openGUIs.get((Player) e.getWhoClicked()) instanceof AddListingPage)) {
			return;
		}
		AddListingPage listing = (AddListingPage) GUIManager.openGUIs.get(e.getWhoClicked());

		if(e.getWhoClicked().getInventory() == e.getClickedInventory() && listing.player.equals((Player) e.getWhoClicked())) {
			if(e.getCurrentItem() != null)
				listing.selectedSlotID = e.getSlot();
		}
		listing.load();
		
		e.setCancelled(true);
	}
}
