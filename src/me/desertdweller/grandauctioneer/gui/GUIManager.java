package me.desertdweller.grandauctioneer.gui;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.desertdweller.grandauctioneer.items.Auctioneer;
import me.desertdweller.guiapi.InventoryVector;

public class GUIManager implements Listener{
	public static HashMap<Player, Object> openGUIs = new HashMap<Player, Object>();
	
	//Methods for opening GA inventories. These handle closing other inventories and the hashmap on their own.
	public static void openGrandAuction(Player p, Auctioneer auc) {
		p.closeInventory();
		openGUIs.put(p, new MainMenu(p,auc));
	}
	
	public static void openListingsPage(Player p, Auctioneer auc) {
		p.closeInventory();
		openGUIs.put(p, new ListingsHome(p, auc));
	}
	
	public static void openResultsPage(Player p, ResultsType mode, String keyword, Auctioneer auc) {
		p.closeInventory();
		openGUIs.put(p, new ResultsPage(p, mode, keyword, auc));
	}
	
	public static void openAddListingPage(Player p, Auctioneer auc) {
		p.closeInventory();
		openGUIs.put(p, new AddListingPage(p, auc));
	}
	
	public static void openSearchPage(Player p, Auctioneer auc) {
		p.closeInventory();
		openGUIs.put(p, new Search(p, auc));
	}
	
	public static void openCreateAdPage(Player p, Auctioneer auc, InventoryVector vec, Boolean edit, long time) {
		p.closeInventory();
		openGUIs.put(p, new CreateAd(p, auc, vec, edit, time));
	}
	
	public static void refreshGUIs() {
		for(Object gUI : openGUIs.values()) {
			if(gUI instanceof MainMenu) {
				((MainMenu) gUI).loadGUI();
				
			}else if(gUI instanceof ResultsPage) {
				((ResultsPage) gUI).loadGUI();
			}
		}
	}
	
	@EventHandler
	public static void onInvClose(InventoryCloseEvent e) {
		if(openGUIs.containsKey(e.getPlayer())) {
			openGUIs.remove(e.getPlayer());
		}
	}
}
