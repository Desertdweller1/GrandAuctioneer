package me.desertdweller.grandauctioneer.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.desertdweller.grandauctioneer.GrandAuctioneer;
import me.desertdweller.guiapi.Slot;
import net.md_5.bungee.api.ChatColor;

public class GUIItems {
	static Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);

	
	static public Slot getFiller() {
		Slot slot = new Slot(new ItemStack(Material.GRAY_STAINED_GLASS_PANE),false);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(" ");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getJobBoardBackground() {
		Slot slot = new Slot(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Click to reserve..");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getPersonalListingsButton() {
		Slot slot = new Slot(new ItemStack(Material.GOLD_INGOT),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Personal Listings");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getViewListingsButton() {
		Slot slot = new Slot(new ItemStack(Material.DIAMOND),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "View Listings");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getYourItemsButton() {
		Slot slot = new Slot(new ItemStack(Material.EMERALD),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.DARK_GREEN + "Retrieve Items");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getSearchButton() {
		Slot slot = new Slot(new ItemStack(Material.ACTIVATOR_RAIL),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Search Items");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getBasicBackground() {
		Slot slot = new Slot(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE),false);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(" ");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getBackButton() {
		Slot slot = new Slot(new ItemStack(Material.RED_STAINED_GLASS_PANE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.RED + "<- Back");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getNextPageButton() {
		Slot slot = new Slot(new ItemStack(Material.GREEN_STAINED_GLASS_PANE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Next Page ->");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getPreviousPageButton() {
		Slot slot = new Slot(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "<- Previous Page");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getConsumablesButton() {
		Slot slot = new Slot(new ItemStack(Material.APPLE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.DARK_GREEN + "Consumables");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getArmorsButton() {
		Slot slot = new Slot(new ItemStack(Material.IRON_CHESTPLATE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Armors");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getWeaponsButton() {
		Slot slot = new Slot(new ItemStack(Material.IRON_SWORD),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.DARK_AQUA + "Weapons");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getToolsButton() {
		Slot slot = new Slot(new ItemStack(Material.IRON_PICKAXE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Tools");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getBlocksButton() {
		Slot slot = new Slot(new ItemStack(Material.STONE_BRICKS),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Blocks");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getBooksButton() {
		Slot slot = new Slot(new ItemStack(Material.BOOK),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Books");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getResourcesButton() {
		Slot slot = new Slot(new ItemStack(Material.DIAMOND),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + "Resources");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getRedstoneButton() {
		Slot slot = new Slot(new ItemStack(Material.REDSTONE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Redstone");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getOrganicsButton() {
		Slot slot = new Slot(new ItemStack(Material.OAK_SAPLING),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Organics");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getMiscButton() {
		Slot slot = new Slot(new ItemStack(Material.BONE_MEAL),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "Misc");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getAddListingButton() {
		Slot slot = new Slot(new ItemStack(Material.GREEN_STAINED_GLASS_PANE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Add a Listing");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getJobBoardSign() {
		Slot slot = new Slot(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE),false);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + "Player Advertisements");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getJobBoardToggle() {
		Slot slot = new Slot(new ItemStack(Material.SIGN),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + "Toggle Player Advertisements");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	static public Slot getSelectDisplayItem() {
		Slot slot = new Slot(new ItemStack(Material.PAPER),false);
		ItemMeta meta = slot.getItem().getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Choose an item out of your");
		lore.add(ChatColor.YELLOW + "inventory to set as the ad");
		lore.add(ChatColor.YELLOW + "display.");
		meta.setLore(lore);
		
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getSetLore() {
		Slot slot = new Slot(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Click to set the description.");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getSetTitle() {
		Slot slot = new Slot(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Click to set the title.");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getConfirmAd() {
		Slot slot = new Slot(new ItemStack(Material.GREEN_STAINED_GLASS_PANE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Confirm to reserve for a week, with a cost of 100" + plugin.getConfig().getString("currencySuffix") + "?.");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
	
	static public Slot getConfirmEdit() {
		Slot slot = new Slot(new ItemStack(Material.GREEN_STAINED_GLASS_PANE),true);
		ItemMeta meta = slot.getItem().getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Confirm");
		slot.getItem().setItemMeta(meta);
		return slot;
	}
}
