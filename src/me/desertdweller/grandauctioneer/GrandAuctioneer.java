package me.desertdweller.grandauctioneer;


import java.io.File;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.onarandombox.MultiverseCore.MultiverseCore;

import me.desertdweller.grandauctioneer.datastorage.DataHandler;
import me.desertdweller.grandauctioneer.gui.AddListingPage;
import me.desertdweller.grandauctioneer.gui.CreateAd;
import me.desertdweller.grandauctioneer.gui.GUIManager;
import me.desertdweller.grandauctioneer.gui.ListingsHome;
import me.desertdweller.grandauctioneer.gui.MainMenu;
import me.desertdweller.grandauctioneer.gui.ResultsPage;
import me.desertdweller.grandauctioneer.gui.Search;
import me.desertdweller.grandauctioneer.items.AuctioneerHandler;
import me.desertdweller.guiapi.GUIInventory;
import net.milkbowl.vault.economy.Economy;

public class GrandAuctioneer extends JavaPlugin{
    private static Economy econ = null;
    public static File folder = new File("plugins//GrandAuctioneer");
    private static Server server;
	@SuppressWarnings("unused")
	private BukkitTask expireChecker;
	
	@Override 
	public void onEnable() {
		server = getServer();
		if(!folder.exists()) 
			folder.mkdir();
		expireChecker = new ExpireChecker().runTaskTimer(this,0,36000);
		getCommand("ganame").setExecutor(new Commands());
		getCommand("gasearch").setExecutor(new Commands());
		getCommand("openauc").setExecutor(new Commands());
		getCommand("savedata").setExecutor(new Commands());
		getCommand("loaddata").setExecutor(new Commands());
		getCommand("dumplogs").setExecutor(new Commands());
		getCommand("additem").setExecutor(new Commands());
		getCommand("removeitems").setExecutor(new Commands());
		getCommand("auclist").setExecutor(new Commands());
		getCommand("ga").setExecutor(new Commands());
		getCommand("createauc").setExecutor(new Commands());
		getCommand("moveauc").setExecutor(new Commands());
		getCommand("deleteauc").setExecutor(new Commands());
		if(!setupEconomy()) {
	        throw new RuntimeException("Vault not found!");
		}
		GUIInventory.Init_(this);
		saveDefaultConfig();
		getConfig();
		getServer().getPluginManager().registerEvents(new CreateAd(null, null, null, null, 0),this);
		getServer().getPluginManager().registerEvents(new Search(null, null),this);
		getServer().getPluginManager().registerEvents(new MainMenu(null, null),this);
		getServer().getPluginManager().registerEvents(new AddListingPage(null, null),this);
		getServer().getPluginManager().registerEvents(new ListingsHome(null, null),this);
		getServer().getPluginManager().registerEvents(new ResultsPage(null, null, null, null),this);
		getServer().getPluginManager().registerEvents(new AuctioneerHandler(), this);
		getServer().getPluginManager().registerEvents(new GUIManager(), this);
		DataHandler.load();
	}
	
	@Override
	public void onDisable() {
		Logger.dump();
		DataHandler.save();
	}
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
	}

	public static MultiverseCore getMultiverseCore() {
        Plugin plugin = server.getPluginManager().getPlugin("Multiverse-Core");
 
        if (plugin instanceof MultiverseCore && plugin != null) {
            return (MultiverseCore) plugin;
        }
 
        throw new RuntimeException("MultiVerse not found!");
    }
	
	public static Economy getEconomy() {
		return econ;
	}
}
