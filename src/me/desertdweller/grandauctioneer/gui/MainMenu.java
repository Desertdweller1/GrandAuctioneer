package me.desertdweller.grandauctioneer.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import me.desertdweller.grandauctioneer.GrandAuctioneer;
import me.desertdweller.grandauctioneer.items.Auctioneer;
import me.desertdweller.grandauctioneer.items.AuctioneerHandler;
import me.desertdweller.guiapi.ClickableClickedEvent;
import me.desertdweller.guiapi.GUIInventory;
import me.desertdweller.guiapi.InventoryVector;
import me.desertdweller.guiapi.SubInventory;

public class MainMenu implements Listener{
	Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);
	private GUIInventory inv;
	Player player;
	Boolean displayJobs = false;
	Auctioneer auc;
	SubInventory adinv;
	
	public MainMenu(Player p, Auctioneer auc){
		player = p;
		//Ensures that the given variables are not null
		if(p == null || auc == null) {
			return;
		}
		this.auc = auc;
		//Creates a GUI inventory from my API, and sets 'inv' to it.
		inv = new GUIInventory(6, "Grand Auction Menu", plugin, true);
		inv.createSubInventory(new InventoryVector(0,0), 9, 4, "Player Ads");
		SubInventory jobBoard = (SubInventory) inv.GUIObjects.get(0);
		jobBoard.createSubInventory(new InventoryVector(1,0), 7, 4, "Advertisements");
		adinv = (SubInventory) jobBoard.gUIObjectList.get(0);
		loadGUI();
		inv.openForPlayer(p);
	}
	
	//Loads in the page with fresh data. (Should be used whenever applicable data is changed)
	public void loadGUI() {
		SubInventory jobBoard = (SubInventory) inv.GUIObjects.get(0);
		SubInventory ads = (SubInventory) jobBoard.gUIObjectList.get(0);
		inv.fill(GUIItems.getFiller());
		if(displayJobs) {
			jobBoard.fill(GUIItems.getJobBoardSign());
			ads.fill(GUIItems.getJobBoardBackground());
			for(int i=0; i < AuctioneerHandler.getAdList().size(); i++) {
				ads.setSlot(InventoryVector.getVectorFromInt(i, ads.getWidth()), AuctioneerHandler.getAdList().get(i).getDisplayItem(player));
				
			}
		}else {
			jobBoard.fill(GUIItems.getFiller());
			ads.fill(GUIItems.getFiller());
		}
		inv.setSlot(new InventoryVector(4,4), GUIItems.getJobBoardToggle());
		inv.setSlot(new InventoryVector(2,5), GUIItems.getViewListingsButton());
		inv.setSlot(new InventoryVector(3,5), GUIItems.getSearchButton());
		inv.setSlot(new InventoryVector(4,5), GUIItems.getPersonalListingsButton());
		inv.setSlot(new InventoryVector(5,5), GUIItems.getYourItemsButton());
		inv.setSlot(new InventoryVector(6,5), GUIItems.getAddListingButton());
		//drawObjects handles drawing inventory objects, such as subInventories
		inv.drawObjects();
		//render updates the vanilla inventory it uses
		inv.render();
		
	}
	
	//Handles functionality of the buttons.
	@EventHandler
	static private void onClickableClicked(ClickableClickedEvent e) {
		Auctioneer auc;
		Player player;
		//If the inventory is not a MainMenu
		if(!GUIManager.openGUIs.containsKey(e.getPlayer()) || !(GUIManager.openGUIs.get(e.getPlayer()) instanceof MainMenu) || e.isCancelled()) {
			return;
		}
		
		MainMenu menu = (MainMenu) GUIManager.openGUIs.get(e.getPlayer());
		auc = menu.auc;
		player = e.getPlayer();
		
		if(e.getSlot().equals(GUIItems.getJobBoardBackground())) {
			//Continues onto the ad creation screen.
			GUIManager.openCreateAdPage(player, auc, e.getVector().add(new InventoryVector(-1,0)), false, 0);
		}else if(e.getSlot().equals(GUIItems.getViewListingsButton())) {
			//Opens Listings page with the categories.
			GUIManager.openListingsPage(player, auc);
		}else if(e.getSlot().equals(GUIItems.getSearchButton())) {
			//Later on will open an anvil screen for the player to be able to type in a search
			GUIManager.openSearchPage(player, auc);
		}else if(e.getSlot().equals(GUIItems.getYourItemsButton())) {
			//Opens results inventory with retrieval items shown
			GUIManager.openResultsPage(player, ResultsType.RETRIEVAL, null, auc);
		}else if(e.getSlot().equals(GUIItems.getPersonalListingsButton())) {
			//Opens results inventory with personal listings shown
			GUIManager.openResultsPage(player, ResultsType.PERSONAL, null, auc);
		}else if(e.getSlot().equals(GUIItems.getAddListingButton())) {
			//Opens the menu for setting up a listing
			GUIManager.openAddListingPage(player, auc);
		}else if(e.getSlot().equals(GUIItems.getJobBoardToggle())) {
			//Toggles the job board
			if(menu.displayJobs) {
				menu.displayJobs = false;
			}else {
				menu.displayJobs = true;
			}
			menu.loadGUI();
		}else if(menu.adinv.isWithinInventoryArea(e.getVector()) && !e.isCancelled()) {
			GUIManager.openCreateAdPage(player, auc, e.getVector().add(new InventoryVector(-1,0)), true, AuctioneerHandler.getAdList().get(e.getVector().add(new InventoryVector(-1,0)).getIntFromVector(7)).getTimeListed());
		}

		e.setCancelled(true);
	}
	
}
