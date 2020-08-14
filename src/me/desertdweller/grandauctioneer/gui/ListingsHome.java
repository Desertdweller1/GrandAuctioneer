package me.desertdweller.grandauctioneer.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import me.desertdweller.grandauctioneer.GrandAuctioneer;
import me.desertdweller.grandauctioneer.items.Auctioneer;
import me.desertdweller.guiapi.ClickableClickedEvent;
import me.desertdweller.guiapi.GUIInventory;
import me.desertdweller.guiapi.InventoryVector;

public class ListingsHome implements Listener{
	Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class); 
	private GUIInventory inv;
	Player player;
	Auctioneer auc;
	
	public ListingsHome(Player p, Auctioneer auc) {
		player = p;
		if(p == null || auc == null)
			return;
		this.auc = auc;
		inv = new GUIInventory(4, "Listings Home", plugin, true);
		inv.fill(GUIItems.getFiller());
		inv.setSlot(new InventoryVector(1,1), GUIItems.getConsumablesButton());
		inv.setSlot(new InventoryVector(2,1), GUIItems.getArmorsButton());
		inv.setSlot(new InventoryVector(3,1), GUIItems.getWeaponsButton());
		inv.setSlot(new InventoryVector(4,1), GUIItems.getToolsButton());
		inv.setSlot(new InventoryVector(5,1), GUIItems.getBlocksButton());
		inv.setSlot(new InventoryVector(6,1), GUIItems.getBooksButton());
		inv.setSlot(new InventoryVector(7,1), GUIItems.getResourcesButton());
		inv.setSlot(new InventoryVector(1,2), GUIItems.getRedstoneButton());
		inv.setSlot(new InventoryVector(2,2), GUIItems.getOrganicsButton());
		inv.setSlot(new InventoryVector(3,2), GUIItems.getMiscButton());
		inv.setSlot(new InventoryVector(4,2), GUIItems.getBasicBackground());
		inv.setSlot(new InventoryVector(5,2), GUIItems.getBasicBackground());
		inv.setSlot(new InventoryVector(6,2), GUIItems.getBasicBackground());
		inv.setSlot(new InventoryVector(7,2), GUIItems.getBasicBackground());
		inv.setSlot(new InventoryVector(0,3), GUIItems.getBackButton());
		inv.render();
		inv.openForPlayer(p);
	}
	
	//Handles functionality of the buttons.
	@EventHandler
	private static void onClickableClicked(ClickableClickedEvent e) {
		Auctioneer auc;
		Player player;
		//If the inventory is not a ListingsHome
		if(!GUIManager.openGUIs.containsKey(e.getPlayer()) || !(GUIManager.openGUIs.get(e.getPlayer()) instanceof ListingsHome) || e.isCancelled()) {
			return;
		}
		
		ListingsHome home = (ListingsHome) GUIManager.openGUIs.get(e.getPlayer());
		auc = home.auc;
		player = e.getPlayer();
		
		
		if(e.getSlot().equals(GUIItems.getConsumablesButton())) {
			e.setCancelled(true);
			//Opens the consumables list
			GUIManager.openResultsPage(player, ResultsType.LISTINGS, "consumables", auc);
		}else if(e.getSlot().equals(GUIItems.getArmorsButton())) {
			e.setCancelled(true);
			//Opens the armors list
			GUIManager.openResultsPage(player, ResultsType.LISTINGS, "armors", auc);
		}else if(e.getSlot().equals(GUIItems.getWeaponsButton())) {
			e.setCancelled(true);
			//Opens the weapons list
			GUIManager.openResultsPage(player, ResultsType.LISTINGS, "weapons", auc);
		}else if(e.getSlot().equals(GUIItems.getToolsButton())) {
			e.setCancelled(true);
			//Opens the tools list
			GUIManager.openResultsPage(player, ResultsType.LISTINGS, "tools", auc);
		}else if(e.getSlot().equals(GUIItems.getBlocksButton())) {
			e.setCancelled(true);
			//Opens the blocks list
			GUIManager.openResultsPage(player, ResultsType.LISTINGS, "blocks", auc);
		}else if(e.getSlot().equals(GUIItems.getBooksButton())) {
			e.setCancelled(true);
			//Opens the books list
			GUIManager.openResultsPage(player, ResultsType.LISTINGS, "books", auc);
		}else if(e.getSlot().equals(GUIItems.getResourcesButton())) {
			e.setCancelled(true);
			//Opens the books list
			GUIManager.openResultsPage(player, ResultsType.LISTINGS, "resources", auc);
		}else if(e.getSlot().equals(GUIItems.getRedstoneButton())) {
			e.setCancelled(true);
			//Opens the books list
			GUIManager.openResultsPage(player, ResultsType.LISTINGS, "redstone", auc);
		}else if(e.getSlot().equals(GUIItems.getOrganicsButton())) {
			e.setCancelled(true);
			//Opens the organics list
			GUIManager.openResultsPage(player, ResultsType.LISTINGS, "organics", auc);
		}else if(e.getSlot().equals(GUIItems.getMiscButton())) {
			e.setCancelled(true);
			//Opens the organics list
			GUIManager.openResultsPage(player, ResultsType.LISTINGS, "misc", auc);
		}else if(e.getSlot().equals(GUIItems.getBackButton())) {
			e.setCancelled(true);
			//Goes back to Main Menu
			GUIManager.openGrandAuction(player, auc);
		}
	}
}
