package me.desertdweller.grandauctioneer.datastorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.desertdweller.grandauctioneer.GrandAuctioneer;
import me.desertdweller.grandauctioneer.items.AdItem;
import me.desertdweller.grandauctioneer.items.AuctionItem;
import me.desertdweller.grandauctioneer.items.Auctioneer;
import me.desertdweller.grandauctioneer.items.AuctioneerHandler;

public class DataHandler {
	static Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);
	private static File dataFile;
	private static FileConfiguration dataConfig;
	
	
	static public void load() {
		for(Auctioneer delauc : AuctioneerHandler.getAuctioneers()) {
			AuctioneerHandler.deleteAuctioneer(delauc);	
		}
		
		dataFile = new File(plugin.getDataFolder(), "auctioneers.yml");
		if (!dataFile.exists()) {
			dataFile.getParentFile().mkdirs();
			plugin.saveResource("auctioneers.yml", false);
		}
		dataConfig = new YamlConfiguration();
		try {
			dataConfig.load(dataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		System.out.println("[GrandAuctioneer] Creating Auctioneers...");
		for(String key : dataConfig.getKeys(false)) {
			if(GrandAuctioneer.getMultiverseCore().getMVWorldManager().getMVWorld(dataConfig.getString(key + ".originw")) == null) {
				System.out.println("[GrandAuctioneer] '" + plugin.getConfig().getString(key + ".originw") + "' is not an existing world.");
			}else {
				World w = GrandAuctioneer.getMultiverseCore().getMVWorldManager().getMVWorld(dataConfig.getString(key + ".originw")).getCBWorld();
				AuctioneerHandler.newAuctioneer(new Location(w, dataConfig.getInt(key + ".originx"),dataConfig.getInt(key + ".originy"), dataConfig.getInt(key + ".originz")), key);
			}
		}
		
		
		//Loads the config file where the data was stored
		dataFile = new File(plugin.getDataFolder(), "savedata.yml");
		if (!dataFile.exists()) {
			dataFile.getParentFile().mkdirs();
			plugin.saveResource("savedata.yml", false);
		}
		dataConfig = new YamlConfiguration();
		try {
			dataConfig.load(dataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		//Creates and adds the AuctionItems based off of yml save file.
		for(String aucName : dataConfig.getKeys(false)) {
			Auctioneer auc = AuctioneerHandler.getAuctioneer(aucName);
			for(String suuid : dataConfig.getConfigurationSection(aucName).getKeys(false)){
				UUID uuid = UUID.fromString(suuid);
				Player p = Bukkit.getPlayer(uuid);
				OfflinePlayer op = null;
				if(p == null) {
					op = Bukkit.getOfflinePlayer(uuid);
					System.out.println(op + " has " + dataConfig.getConfigurationSection(aucName + "." + suuid).getKeys(false).size() + " items");
				}else {
					System.out.println(p + " has " + dataConfig.getConfigurationSection(aucName + "." + suuid).getKeys(false).size() + " items");
				}
				
				for(String aucItem : dataConfig.getConfigurationSection(aucName + "." + suuid).getKeys(false)) {
					String page = dataConfig.getString(aucName + "." + suuid + "." + aucItem + ".page");
					ItemStack item = dataConfig.getItemStack(aucName + "." + suuid + "." + aucItem + ".item");
					int price = dataConfig.getInt(aucName + "." + suuid + "." + aucItem + ".price");
					long time = dataConfig.getLong(aucName + "." + suuid + "." + aucItem + ".timeListed");
					boolean expired = dataConfig.getBoolean(aucName + "." + suuid + "." + aucItem + ".expired");
					
					//Creates the AucItem based on info stored in file.
					AuctionItem finalItem;
					if(p != null) {
						finalItem = new AuctionItem(item, uuid, time, price, expired, auc);
					}else {
						finalItem = new AuctionItem(item, uuid, time, price, expired, auc);
					}
					
					//Places the AucItem in an auctioneer
					if(page.equals("retrieval")) {
						auc.addItemToHeld(finalItem);
					}else if(page.equals("listing")) {
						auc.addItem(finalItem);
					}
				}
			}
		}
		//Loads Ads that were saved
		
		dataFile = new File(plugin.getDataFolder(), "addata.yml");
		if (!dataFile.exists()) {
			dataFile.getParentFile().mkdirs();
			plugin.saveResource("addata.yml", false);
		}
		dataConfig = new YamlConfiguration();
		try {
			dataConfig.load(dataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		ArrayList<AdItem> adList = new ArrayList<AdItem>(28);
		for(int i= 0; i < 28; i++) {
			adList.add(new AdItem(null,null,0));
		}
		
		for(String item : dataConfig.getKeys(false)) {
			UUID uuid = UUID.fromString(dataConfig.getString(item + ".uuid"));
			ItemStack itemstack = dataConfig.getItemStack(item + ".item");
			long time = dataConfig.getLong(item + ".timeListed");
			adList.set(Integer.parseInt(item), new AdItem(uuid,itemstack,time));
		}
		AuctioneerHandler.setAdlist(adList);
	}
	
	static public void save() {
		//Saving Auctioneer locations
		dataFile = new File(plugin.getDataFolder(), "auctioneers.yml");
		dataFile.delete();
		if (!dataFile.exists()) {
			dataFile.getParentFile().mkdirs();
			if(plugin.getResource("auctioneers.yml") != null)
				plugin.saveResource("auctioneers.yml", false);
		}
		dataConfig = new YamlConfiguration();
		try {
			dataConfig.load(dataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		for(Auctioneer auc : AuctioneerHandler.getAuctioneers()) {
			dataConfig.createSection(auc.getName() + ".originx");
			dataConfig.createSection(auc.getName() + ".originy");
			dataConfig.createSection(auc.getName() + ".originz");
			dataConfig.createSection(auc.getName() + ".originw");
			dataConfig.set(auc.getName() + ".originx", auc.getLocation().getBlockX());
			dataConfig.set(auc.getName() + ".originy", auc.getLocation().getBlockY());
			dataConfig.set(auc.getName() + ".originz", auc.getLocation().getBlockZ());
			dataConfig.set(auc.getName() + ".originw", auc.getLocation().getWorld().getName());
		}

		try {
			dataConfig.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Saving listings and retrievals
		
		//Clears the save file
		dataFile = new File(plugin.getDataFolder(), "savedata.yml");
		dataFile.delete();
		if (!dataFile.exists()) {
			dataFile.getParentFile().mkdirs();
			if(plugin.getResource("savedata.yml") != null)
				plugin.saveResource("savedata.yml", false);
		}
		dataConfig = new YamlConfiguration();
		try {
			dataConfig.load(dataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		for(Auctioneer auc : AuctioneerHandler.getAuctioneers()) {
			String aucname = auc.getName();
			int id = 0;
			for(AuctionItem item : auc.getItemList()) {
				ItemStack stack = item.getItem();
				int price = item.getPrice();
				long time = item.getTimeOfListing();
				boolean expired = item.isExpired();
				String uuid = item.getListerUUID().toString();
				dataConfig.createSection(aucname + "." + uuid + "." + id + ".page");
				dataConfig.createSection(aucname + "." + uuid + "." + id + ".item");
				dataConfig.createSection(aucname + "." + uuid + "." + id + ".price");
				dataConfig.createSection(aucname + "." + uuid + "." + id + ".timeListed");
				dataConfig.createSection(aucname + "." + uuid + "." + id + ".expired");
				dataConfig.set(aucname + "." + uuid + "." + id + ".page", "listing");
				dataConfig.set(aucname + "." + uuid + "." + id + ".item", stack);
				dataConfig.set(aucname + "." + uuid + "." + id + ".price", price);
				dataConfig.set(aucname + "." + uuid + "." + id + ".timeListed", time);
				dataConfig.set(aucname + "." + uuid + "." + id + ".expired", expired);
				id++;
			}
			for(AuctionItem item : auc.getHeldItems()) {
				ItemStack stack = item.getItem();
				int price = item.getPrice();
				long time = item.getTimeOfListing();
				boolean expired = item.isExpired();
				String uuid = item.getListerUUID().toString();
				dataConfig.createSection(aucname + "." + uuid + "." + id + ".page");
				dataConfig.createSection(aucname + "." + uuid + "." + id + ".item");
				dataConfig.createSection(aucname + "." + uuid + "." + id + ".price");
				dataConfig.createSection(aucname + "." + uuid + "." + id + ".timeListed");
				dataConfig.createSection(aucname + "." + uuid + "." + id + ".expired");
				dataConfig.set(aucname + "." + uuid + "." + id + ".page", "retrieval");
				dataConfig.set(aucname + "." + uuid + "." + id + ".item", stack);
				dataConfig.set(aucname + "." + uuid + "." + id + ".price", price);
				dataConfig.set(aucname + "." + uuid + "." + id + ".timeListed", time);
				dataConfig.set(aucname + "." + uuid + "." + id + ".expired", expired);
				id++;
			}
		}
		try {
			dataConfig.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Saving Ad data
		
		dataFile = new File(plugin.getDataFolder(), "addata.yml");
		dataFile.delete();
		if (!dataFile.exists()) {
			dataFile.getParentFile().mkdirs();
			if(plugin.getResource("savedata.yml") != null)
				plugin.saveResource("addata.yml", false);
		}
		dataConfig = new YamlConfiguration();
		try {
			dataConfig.load(dataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		int id = 0;
		for(AdItem ad : AuctioneerHandler.getAdList()) {
			if(ad.getPlayer() != null && ad.getItem() != null && ad.getTimeListed() != 0) {
				dataConfig.createSection(id + "." + ".uuid");
				dataConfig.createSection(id + "." + ".item");
				dataConfig.createSection(id + "." + ".timelisted");
				dataConfig.createSection(id + "." + ".position");
				dataConfig.set(id + "." + ".uuid", ad.getPlayer().toString());
				dataConfig.set(id + "." + ".item", ad.getItem());
				dataConfig.set(id + "." + ".timeListed", ad.getTimeListed());
				dataConfig.set(id + "." + ".position", id);
			}
			id++;
		}
		
		try {
			dataConfig.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	static public FileConfiguration getDataConfig() {
		return dataConfig;
	}
	
}
