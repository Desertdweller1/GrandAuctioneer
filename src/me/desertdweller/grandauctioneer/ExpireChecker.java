package me.desertdweller.grandauctioneer;

import java.util.ArrayList;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.desertdweller.grandauctioneer.gui.GUIManager;
import me.desertdweller.grandauctioneer.items.AdItem;
import me.desertdweller.grandauctioneer.items.AuctionItem;
import me.desertdweller.grandauctioneer.items.Auctioneer;
import me.desertdweller.grandauctioneer.items.AuctioneerHandler;

public class ExpireChecker extends BukkitRunnable{
	static Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);

	@Override
	public void run() {
		System.out.println("[GrandAuctioneer] Checking for expired items..");
		for(Auctioneer curAuc : AuctioneerHandler.getAuctioneers()) {
			//Checks listed items
			ArrayList<AuctionItem> tempArray = new ArrayList<AuctionItem>();
			for(AuctionItem item : curAuc.getItemList()) {
				if(item.getTimeOfListing() + plugin.getConfig().getInt("maxTimeAsListing")*3600000 < System.currentTimeMillis()) {
					tempArray.add(item);
				}
			}
			for(AuctionItem item : tempArray) {
				item.cancelListing();
				Logger.addLog(item.getItem().getItemMeta().getDisplayName() + " (" + item.getItem().getType() + ") from UUID " + item.getListerUUID() + " at auctioneer " + item.getAuctioneer().getName() + " has expired, and been placed in their retrieval.");
			}
			
			//Checks retrieval items
			tempArray = new ArrayList<AuctionItem>();
			for(AuctionItem item : curAuc.getHeldItems()) {
				if(item.getTimeOfListing() + plugin.getConfig().getInt("maxTimeInRecieval")*3600000 < System.currentTimeMillis()) {
					tempArray.add(item);
				}
			}
			for(AuctionItem item : tempArray) {
				item.retrieve();
				Logger.addLog(item.getItem().getItemMeta().getDisplayName() + " (" + item.getItem().getType() + ") from UUID " + item.getListerUUID() + " at auctioneer " + item.getAuctioneer().getName() + " has expired, and been deleted.");
			}
			curAuc.getHeldItems().removeAll(tempArray);
		}
		
		ArrayList<Boolean> adArray = new ArrayList<Boolean>();
		for(AdItem item : AuctioneerHandler.getAdList()) {
			if(item.getTimeListed() != 0 && item.getTimeListed() + plugin.getConfig().getInt("adExpireLimitHrs")*3600000 < System.currentTimeMillis()) {
				adArray.add(true);
			}else {
				adArray.add(false);
			}
		}
		for(int i = 0; i < adArray.size(); i++) {
			if(adArray.get(i)) {
				AuctioneerHandler.getAdList().set(i, new AdItem(null,null,0));
			}
		}
		
		GUIManager.refreshGUIs();
		
	}

}
