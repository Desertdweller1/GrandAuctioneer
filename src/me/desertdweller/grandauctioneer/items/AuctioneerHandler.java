package me.desertdweller.grandauctioneer.items;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import me.desertdweller.grandauctioneer.GrandAuctioneer;
import me.desertdweller.guiapi.InventoryVector;

public class AuctioneerHandler implements Listener {
	static Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);
	private static ArrayList<Auctioneer> auctioneerList = new ArrayList<Auctioneer>();
	private static ArrayList<AdItem> adList;
	
	//Creates an auctioneer at the specified location
	public static void newAuctioneer(Location l, String name) {
		auctioneerList.add(new Auctioneer(l, name));
	}
	
	public static void moveAuctioneer(Location l, Auctioneer auc) {
		auc.setLocation(l);
	}
	
	public static void deleteAuctioneer(Auctioneer auc) {
		auctioneerList.remove(auc);
	}
	
	//Gets a list of every single item in the system.
	public static ArrayList<AuctionItem> getAllItems(){
		ArrayList<AuctionItem> tempArray = new ArrayList<AuctionItem>();
		for(Auctioneer curAuc : auctioneerList) {
			for(AuctionItem item : curAuc.getItemList()) {
				tempArray.add(item);
			}
		}
		return tempArray;
	}

	
	public static void setAd(InventoryVector vec, AdItem item) {
		adList.set(vec.getIntFromVector(7), item);
	}
	
	public static void setAdlist(ArrayList<AdItem> ads) {
		adList = ads;
	}
	
	public static void addAd(AdItem item) {
		adList.add(item);
	}
	
	public static void removeAd(AdItem item) {
		adList.remove(item);
	}
	
	public static ArrayList<AdItem> getAdList(){
		return adList;
	}
	
	public static void clearAuctions() {
		for(Auctioneer auc : auctioneerList) {
			auc.removeAllFromAuction();
		}
	}
	
	public static void clearHeld() {
		for(Auctioneer auc : auctioneerList) {
			auc.removeAllFromHeld();
		}
	}
	
	public static void clearPlayerAuctions(Player p) {
		for(Auctioneer auc : auctioneerList) {
			auc.removePlayerAuctions(p);
		}
	}
	
	public static void clearPlayerHeld(Player p) {
		for(Auctioneer auc : auctioneerList) {
			auc.removePlayerHeld(p);
		}
	}
	
	public static Auctioneer getAuctioneer(String name) {
		for(Auctioneer auc : auctioneerList) {
			if(auc.getName().equals(name)) {
				return auc;
			}
		}
		return null;
	}
	
	public static String[] getAuctioneerNames() {
		String[] aucs = new String[auctioneerList.size()];
		for(int i = 0; i < auctioneerList.size(); i++) {
			aucs[i] = auctioneerList.get(i).getName();
		}
		return aucs;
	}
	
	public static ArrayList<Auctioneer> getAuctioneers(){
		return auctioneerList;
	}
}
