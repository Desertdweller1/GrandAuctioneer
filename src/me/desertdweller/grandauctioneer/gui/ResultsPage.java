package me.desertdweller.grandauctioneer.gui;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import de.tr7zw.nbtapi.NBTItem;
import me.desertdweller.grandauctioneer.GrandAuctioneer;
import me.desertdweller.grandauctioneer.Logger;
import me.desertdweller.grandauctioneer.items.AuctionItem;
import me.desertdweller.grandauctioneer.items.Auctioneer;
import me.desertdweller.grandauctioneer.items.AuctioneerHandler;
import me.desertdweller.guiapi.ClickableClickedEvent;
import me.desertdweller.guiapi.GUIInventory;
import me.desertdweller.guiapi.InventoryVector;
import me.desertdweller.guiapi.Slot;
import me.desertdweller.guiapi.SubInventory;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class ResultsPage implements Listener{
	static Economy econ;
	static Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);
	private GUIInventory inv;
	Player player;
	int page = 1;
	int maxPages;
	Auctioneer auc;
	ResultsType mode;
	ArrayList<AuctionItem> applicableItems;
	String keyword;
	
	// Search uses keyword to find items, while Listings uses the keyword for the categories.
	public ResultsPage(Player p, ResultsType mode, String keyword, Auctioneer auc) {
		econ = GrandAuctioneer.getEconomy();
		this.keyword = keyword;
		player = p;
		if(mode == null) 
			return;
		this.mode = mode;
		this.auc = auc;
		inv = new GUIInventory(6, "Results Page", plugin, true);
		if(mode == ResultsType.PERSONAL)
			inv = new GUIInventory(6, "Personal Listings", plugin, true);
		if(mode == ResultsType.RETRIEVAL)
			inv = new GUIInventory(6, "Item Retrieval", plugin, true);
		inv.createSubInventory(new InventoryVector(1,1), 7, 4, "Results");
		loadGUI();
		inv.openForPlayer(player);
	}
	
	//Loads in the page with fresh data.
	public void loadGUI() {
		updateApplicableItemsList(keyword);
		maxPages = getMaxPagesRequired(applicableItems.size());
		inv.fill(GUIItems.getFiller());
		SubInventory results = (SubInventory) inv.GUIObjects.get(0);
		results.fill(GUIItems.getBasicBackground());
		results.fillWithArray(auctionItemsToSlots(getPageOfItems(applicableItems, page)), GUIItems.getBasicBackground());
		inv.GUIObjects.set(0, results);
		inv.setSlot(new InventoryVector(0,5), GUIItems.getBackButton());
		if(mode == ResultsType.PERSONAL)
			inv.setSlot(new InventoryVector(4,5), GUIItems.getAddListingButton());
		inv.setSlot(new InventoryVector(6,5), GUIItems.getPreviousPageButton());
		inv.setSlot(new InventoryVector(7,5), GUIItems.getNextPageButton());
		inv.drawObjects();
		inv.render();
	}
	
	//Based on what mode the class is in, it will sort through the items to find the correct ones.
	private void updateApplicableItemsList(String keyword) {
		if(mode == ResultsType.LISTINGS)
			applicableItems = findListings(keyword,AuctioneerHandler.getAllItems());
		else if(mode == ResultsType.PERSONAL)
			applicableItems = findPersonalListings(auc.getItemList());
		else if(mode == ResultsType.SEARCH)
			applicableItems = findSearchedItems(AuctioneerHandler.getAllItems(), keyword);
		else if(mode == ResultsType.RETRIEVAL)
			applicableItems = findRetrievalItems(auc.getHeldItems());
	}
	
	//Goes through the list of given items, and finds the ones that fit the given category. Will reference the config.
	private ArrayList<AuctionItem> findListings(String category, ArrayList<AuctionItem> itemList){
		ArrayList<AuctionItem> sortedList = new ArrayList<AuctionItem>();
		for(AuctionItem item : itemList) {
			if(item.availableAtAuctioneer(auc)) {
				NBTItem nbti = new NBTItem(item.getItem());
				//If the category is misc, and this item does not have a designated category:
				boolean found = false;
				if(category.equals("misc")) {
					for(String key : plugin.getConfig().getConfigurationSection("categories").getKeys(false)) {
						for(String material : plugin.getConfig().getStringList("categories." + key)) {
							if(material.equals(item.getItem().getType().toString())) {
								found = true;
							}
						}
					}
					if(!found) {
						sortedList.add(item);
					}
				//If its not misc category, look for applicable items for selected category
				}else {
					//If the item is from DesertsCooking, while also having consumables be what is being searched for, add it to the list.
					if(category.equals("consumables") && nbti.hasKey("Plugin") && nbti.getString("Plugin").equalsIgnoreCase("DesertsCooking")) {
						sortedList.add(item);
					}else {//Otherwise if the item is matching the category specified, then add it to the list.
						for(String material : plugin.getConfig().getStringList("categories." + category)) {
							if(material.equals(item.getItem().getType().toString())) {
								sortedList.add(item);
							}
						}
					}
				}
			}
		}
		return sortedList;
	}
	
	//Sorts through the list finding items that the player can retrieve
	private ArrayList<AuctionItem> findRetrievalItems(ArrayList<AuctionItem> itemList){
		ArrayList<AuctionItem> sortedList = new ArrayList<AuctionItem>();
		for(AuctionItem item : itemList) {
			if(item.getListerUUID().equals(player.getUniqueId())) {
				sortedList.add(item);
			}
		}
		return sortedList;
	}
	
	//Sorts through the given list to find all of the items the player has put in here.
	private ArrayList<AuctionItem> findPersonalListings(ArrayList<AuctionItem> itemList){
		ArrayList<AuctionItem> sortedList = new ArrayList<AuctionItem>();
		for(AuctionItem item : itemList) {
			if(item.getListerUUID().equals(player.getUniqueId())) {
				sortedList.add(item);
			}
		}
		return sortedList;
	}
	
	//Looks at all of the names of the given items to see if it can find the keyword given, and returns the ones it does.
	private ArrayList<AuctionItem> findSearchedItems(ArrayList<AuctionItem> itemList, String keyword){
		ArrayList<AuctionItem> sortedList = new ArrayList<AuctionItem>();
		for(AuctionItem item : itemList) {
			if(item.getItem().getItemMeta().hasDisplayName() && item.getItem().getItemMeta().getDisplayName().toLowerCase().contains(keyword.toLowerCase())) {
				sortedList.add(item);
			}else if(item.getItem().getType().toString().toLowerCase().replace("_", " ").contains(keyword.toLowerCase())) {
				sortedList.add(item);
			}
		}
		return sortedList;
	}
	
	//Turns the AuctionItem array into a slot Array
	private ArrayList<Slot> auctionItemsToSlots(ArrayList<AuctionItem> itemList){
		ArrayList<Slot> slotArray = new ArrayList<Slot>();
		for(AuctionItem item : itemList) {
			if(mode == ResultsType.LISTINGS || mode == ResultsType.SEARCH) {
				item.generateDisplayItem();
			}else if(mode == ResultsType.PERSONAL) {
				item.generatePersonalViewItem();
			}else if(mode == ResultsType.RETRIEVAL) {
				item.generateRetrievalDisplayItem();
			}
			
			
			slotArray.add(new Slot(item.getDisplayItem(),true));
		}
		return slotArray;
	}
	
	//Finds the amount of pages needed to hold given number of items.
	private int getMaxPagesRequired(int numberOfItems) {
		return (numberOfItems/28)+1;
	}
	
	//Takes the list of items given, then based on the page it is told will return the list of items that belong on said page.
	private ArrayList<AuctionItem> getPageOfItems(ArrayList<AuctionItem> itemList, int pageNumber){
		//Grabs the total amount of items on previous pages. (28 is the amount of items each page holds)
		int numberOfPrevItems = (pageNumber*28)-28;
		ArrayList<AuctionItem> filteredItems = new ArrayList<AuctionItem>();
		//Checking if the item list is small enough to fit the rest into this page.
		if(itemList.size() < numberOfPrevItems+28) {
			//Checking that the item list in not empty.
			if(itemList.size() > 0) {
				//Adding the rest of the items to this page.
				filteredItems.addAll(itemList.subList(numberOfPrevItems, itemList.size()));
				return filteredItems;
			}
			//If the size is 0, then just return an empty page.
			return filteredItems;
		}
		//This adds the portion of items appropriate for the page.
		filteredItems.addAll(itemList.subList(numberOfPrevItems, numberOfPrevItems+28));
		return filteredItems;
	}
	
	private void buyItem(AuctionItem item) {
		String lister = item.getListerUUID().toString();
		if(!econ.has(player, item.getPrice()) || !item.successfulBuy(player, auc)) {
			player.sendMessage(ChatColor.RED + "You cannot purchase that item.");
			Logger.addLog(player + " failed to purchase " + item.getItem().getItemMeta().getDisplayName() + " (" + item.getItem().getType() + ") from UUID " + lister + " at auctioneer " + auc.getName() + " for " + item.getPrice());
			return;
		}
		player.sendMessage(ChatColor.GREEN + "You have bought an item! It will be in your retrieval page now.");
		Logger.addLog(player + " bought " + item.getItem().getItemMeta().getDisplayName() + " (" + item.getItem().getType() + ") from UUID " + lister + " at auctioneer " + auc.getName() + " for " + item.getPrice());
		econ.withdrawPlayer(player, item.getPrice());
	}
	
	//Handles functionality of the buttons.
	@EventHandler
	private static void onClickableClicked(ClickableClickedEvent e) {
		Auctioneer auc;
		Player player;
		//If the inventory is not a ResultsPage
		if(!GUIManager.openGUIs.containsKey(e.getPlayer()) || !(GUIManager.openGUIs.get(e.getPlayer()) instanceof ResultsPage) || e.isCancelled()) {
			return;
		}
		
		ResultsPage results = (ResultsPage) GUIManager.openGUIs.get(e.getPlayer());
		auc = results.auc;
		player = e.getPlayer();
		GUIInventory inv = results.inv;
		
		if(e.getSlot().equals(GUIItems.getPreviousPageButton())) {
			//Goes to the previous page if there is one
			if(results.page > 1)
				results.page -= 1;
			SubInventory subInv = (SubInventory) inv.GUIObjects.get(0);
			subInv.fill(GUIItems.getBasicBackground());
			subInv.fillWithArray(results.auctionItemsToSlots(results.getPageOfItems(results.applicableItems, results.page)), GUIItems.getBasicBackground());
			inv.GUIObjects.set(0, subInv);
			inv.drawObjects();
			inv.render();
		}else if(e.getSlot().equals(GUIItems.getNextPageButton())) {
			//Goes to the next page if there is one
			if(results.maxPages > results.page)
				results.page += 1;
			SubInventory subInv = (SubInventory) inv.GUIObjects.get(0);
			subInv.fill(GUIItems.getBasicBackground());
			subInv.fillWithArray(results.auctionItemsToSlots(results.getPageOfItems(results.applicableItems, results.page)), GUIItems.getBasicBackground());
			inv.GUIObjects.set(0, subInv);
			inv.drawObjects();
			inv.render();
		}else if(e.getSlot().equals(GUIItems.getAddListingButton())) {
			//Opens the menu for setting up a listing
			GUIManager.openAddListingPage(player, auc);
		}else if(e.getSlot().equals(GUIItems.getBackButton())) {
			//Goes back to previous page
			if(results.mode == ResultsType.LISTINGS)
				GUIManager.openListingsPage(player, auc);
			else {
				GUIManager.openGrandAuction(player, auc);
			}
		}else{
			//If it was none of the above buttons, then it must be a listing, the following handles what a click would do for each of the 4 modes.
			if(results.mode == ResultsType.LISTINGS || results.mode == ResultsType.SEARCH) {
				InventoryVector vector = e.getVector().add(new InventoryVector(-1,-1));
				AuctionItem item = results.applicableItems.get(vector.getIntFromVector(7) + (28*(results.page-1)));
				if(!player.getUniqueId().equals(item.getListerUUID())) {
					results.buyItem(item);
					GUIManager.refreshGUIs();
				}else {
					player.sendMessage(ChatColor.RED + "You cannot buy your own item, if you wish to unlist it, go into your personal listings page.");
				}
			}else if(results.mode == ResultsType.PERSONAL) {
				//Cancels the clicked item.
				InventoryVector vector = e.getVector().add(new InventoryVector(-1,-1));
				AuctionItem item = results.applicableItems.get(vector.getIntFromVector(7) + (28*(results.page-1)));
				item.cancelListing();
				Logger.addLog(player + " cancelled " + item.getItem().getItemMeta().getDisplayName() + " (" + item.getItem().getType() + ")" + " at auctioneer " + auc.getName());
				GUIManager.refreshGUIs();
			}else if(results.mode == ResultsType.RETRIEVAL) {
				InventoryVector vector = e.getVector().add(new InventoryVector(-1,-1));
				AuctionItem item = results.applicableItems.get(vector.getIntFromVector(7) + (28*(results.page-1)));
				//Gembag check
				NBTItem nbti = new NBTItem(item.getItem());
				if(nbti.hasKey("Material") && nbti.getString("Material").equals("GemBag")) {
					econ.depositPlayer(player, nbti.getInteger("Value"));
					Logger.addLog(player + " retrieved " + nbti.getInteger("Value") + " currency from auctioneer " + auc.getName());
					item.retrieve();
					results.loadGUI();
				}else {
					player.getInventory().addItem(item.retrieve());
					Logger.addLog(player + " retrieved " + item.getItem().getItemMeta().getDisplayName() + " (" + item.getItem().getType() + ")" + " from auctioneer " + auc.getName());
					results.loadGUI();
				}
			}
			
		}
		e.setCancelled(true);
	}
}
