package me.desertdweller.grandauctioneer.items;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.desertdweller.grandauctioneer.GrandAuctioneer;
import me.desertdweller.grandauctioneer.gui.GUIItems;
import me.desertdweller.guiapi.Slot;
import net.md_5.bungee.api.ChatColor;

public class AdItem {
	static Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);
	private UUID player;
	private ItemStack item;
	private long timeListed;
	
	public AdItem(UUID player, ItemStack item, long timeListed) {
		this.player = player;
		this.item = item;
		this.timeListed = timeListed;
	}
	
	public Slot getDisplayItem(Player p) {
		if(player == null || item == null || timeListed == 0) {
			return GUIItems.getJobBoardBackground();
		}
		if(p.getUniqueId().equals(player)) {
			ItemStack clone = item.clone();
			ItemMeta meta = clone.getItemMeta();
			long timeSincePost = System.currentTimeMillis() - timeListed;
			ArrayList<String> loreList = (ArrayList<String>) meta.getLore();
			loreList.add(ChatColor.GRAY + "This ad will expire in " + (plugin.getConfig().getInt("adExpireLimitHrs") - (timeSincePost/1000/60/60)) + " hours from now.");
			loreList.add(ChatColor.RED + "This ad is yours, click to edit.");
			meta.setLore(loreList);
			clone.setItemMeta(meta);
			return new Slot(clone,true);
		}else {
			ItemStack clone = item.clone();
			ItemMeta meta = clone.getItemMeta();
			long timeSincePost = System.currentTimeMillis() - timeListed;
			ArrayList<String> loreList = (ArrayList<String>) meta.getLore();
			loreList.add(ChatColor.GRAY + "This ad was posted " + timeSincePost/1000/60/60 + " hours ago.");
			meta.setLore(loreList);
			clone.setItemMeta(meta);
			return new Slot(clone,false);
		}
	}
	
	public UUID getPlayer() {
		return player;
	}

	public void setPlayer(UUID player) {
		this.player = player;
	}
	
	public ItemStack getItem() {
		return item;
	}

	public long getTimeListed() {
		return timeListed;
	}

	public void setTimeListed(long timeListed) {
		this.timeListed = timeListed;
	}
}
