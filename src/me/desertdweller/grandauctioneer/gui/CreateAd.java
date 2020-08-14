package me.desertdweller.grandauctioneer.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.desertdweller.grandauctioneer.GrandAuctioneer;
import me.desertdweller.grandauctioneer.items.AdItem;
import me.desertdweller.grandauctioneer.items.Auctioneer;
import me.desertdweller.grandauctioneer.items.AuctioneerHandler;
import me.desertdweller.guiapi.ClickableClickedEvent;
import me.desertdweller.guiapi.GUIInventory;
import me.desertdweller.guiapi.InventoryVector;
import me.desertdweller.guiapi.Slot;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class CreateAd implements Listener{
	static Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);
	static Economy econ;
	private GUIInventory inv;
	private InventoryVector vec;
	private Player player;
	private Auctioneer auc;
	private Material display;
	private ArrayList<String> lore;
	private String title;
	private String mode = "menu";
	private Boolean editing = false;
	private long time = 0;
	

	public CreateAd(Player player, Auctioneer auc, InventoryVector vec, Boolean editing, long time) {
		econ = GrandAuctioneer.getEconomy();
		this.player = player;
		this.auc = auc;
		this.vec = vec;
		this.editing = editing;
		this.time = time;
		
		if(player == null || auc == null) {
			return;
		}
		this.auc = auc;
		//Creates a GUI inventory from my API, and sets 'inv' to it.
		inv = new GUIInventory(1, "Create Ad", plugin, true);
		loadGUI();
		inv.openForPlayer(player);
	}
	
	public void loadGUI() {
		inv.fill(GUIItems.getBasicBackground());
		if(display == null) {
			inv.setSlot(new InventoryVector(0,0), GUIItems.getSelectDisplayItem());
		}else {
			ItemStack item = new ItemStack(display);
			ItemMeta meta = item.getItemMeta();
			meta.setLore(getLore());
			meta.setDisplayName(getTitle());
			item.setItemMeta(meta);
			inv.setSlot(new InventoryVector(0,0), new Slot(item, false));
		}
		inv.setSlot(new InventoryVector(2,0), GUIItems.getSetTitle());
		inv.setSlot(new InventoryVector(3,0), GUIItems.getSetLore());
		inv.setSlot(new InventoryVector(7,0), GUIItems.getBackButton());
		if(!editing)
			inv.setSlot(new InventoryVector(8,0), GUIItems.getConfirmAd());
		if(editing)
			inv.setSlot(new InventoryVector(8,0), GUIItems.getConfirmEdit());
		inv.render();
	}
	
	@EventHandler
	public static void onClickableClicked(ClickableClickedEvent e) {
		if(!GUIManager.openGUIs.containsKey(e.getPlayer()) || !(GUIManager.openGUIs.get(e.getPlayer()) instanceof CreateAd) || e.isCancelled()) {
			return;
		}
		e.setCancelled(true);
		
		CreateAd admenu = (CreateAd) GUIManager.openGUIs.get(e.getPlayer());
		
		if(e.getSlot().equals(GUIItems.getSetTitle())) {
			admenu.mode = "setTitle";
			e.getPlayer().closeInventory();
			GUIManager.openGUIs.put(admenu.player, admenu);
		}else if(e.getSlot().equals(GUIItems.getSetLore())) {
			admenu.mode = "setLore";
			e.getPlayer().closeInventory();
			GUIManager.openGUIs.put(admenu.player, admenu);
		}else if(e.getSlot().equals(GUIItems.getBackButton())) {
			GUIManager.openGrandAuction(admenu.player, admenu.auc);
		}else if(e.getSlot().equals(GUIItems.getConfirmAd())) {
			if(admenu.getTitle() != null && admenu.display != null && admenu.getLore() != null) {
				if(!econ.has(e.getPlayer(), 100)) {
					e.getPlayer().sendMessage(ChatColor.RED+"You do not have 100" + plugin.getConfig().getString("currencySuffix") + " on you.");
					e.setCancelled(true);
					return;
				}
				econ.withdrawPlayer(e.getPlayer(), 100);
				ItemStack item = new ItemStack(admenu.display);
				ItemMeta meta = item.getItemMeta();
				meta.setLore(admenu.getLore());
				meta.setDisplayName(admenu.getTitle());
				item.setItemMeta(meta);
				AuctioneerHandler.setAd(admenu.vec, new AdItem(e.getPlayer().getUniqueId(), item, System.currentTimeMillis()));
				GUIManager.openGrandAuction(admenu.player, admenu.auc);
			}else {
				e.getPlayer().sendMessage(ChatColor.RED+"Your ad is not complete! Make sure to add lore, a title, and to give it a display.");
			}
		}else if(e.getSlot().equals(GUIItems.getConfirmEdit())) {
			if(admenu.getTitle() != null && admenu.display != null && admenu.getLore() != null) {
				ItemStack item = new ItemStack(admenu.display);
				ItemMeta meta = item.getItemMeta();
				meta.setLore(admenu.getLore());
				meta.setDisplayName(admenu.getTitle());
				item.setItemMeta(meta);
				AuctioneerHandler.setAd(admenu.vec, new AdItem(e.getPlayer().getUniqueId(), item, admenu.time));
				GUIManager.openGrandAuction(admenu.player, admenu.auc);
			}else {
				e.getPlayer().sendMessage(ChatColor.RED+"Your ad is not complete! Make sure to add lore, a title, and to give it a display.");
			}
		}
	}
	
	@EventHandler
	public static void onPlayerChat(AsyncPlayerChatEvent e) {
		if(!GUIManager.openGUIs.containsKey(e.getPlayer()) || !(GUIManager.openGUIs.get(e.getPlayer()) instanceof CreateAd) || e.isCancelled()) {
			return;
		}
		
		CreateAd admenu = (CreateAd) GUIManager.openGUIs.get(e.getPlayer());
		String coloredText = ChatColor.translateAlternateColorCodes('&', e.getMessage());
		
		
		if(admenu.mode == "setTitle") {
			if(e.getMessage().length() > plugin.getConfig().getInt("maxAdTitleChars")) {
				e.getPlayer().sendMessage(ChatColor.RED+"That was " + e.getMessage().length() + " characters long, and needs to be under " + plugin.getConfig().getInt("maxAdTitleChars") + ".");
			}else {
				admenu.setTitle(coloredText);
			}
			admenu.mode = "menu";
		}else if(admenu.mode == "setLore") {
			admenu.setLore(new ArrayList<String>());
			admenu.getLore().add("");
			Boolean failed = false;
			int lineID = 0;
			String[] words = coloredText.split(" ");
			for(int i = 0; i < words.length; i++) {
				if(words[i].length() > plugin.getConfig().getInt("maxAdLoreLineChars")) {
					e.getPlayer().sendMessage(ChatColor.RED+"All words must be under " + plugin.getConfig().getInt("maxAdLoreLineChars") + " characters long.");
					failed = true;
				}else {
					if(words[i].length() + admenu.getLore().get(lineID).length() > plugin.getConfig().getInt("maxAdLoreLineChars")) {
						lineID += 1;
						if(lineID >= plugin.getConfig().getInt("maxAdLoreLines")) {
							failed = true;
						}
						admenu.getLore().add(words[i]);
					}else {
						admenu.getLore().set(lineID, admenu.getLore().get(lineID).concat(" " + words[i]));
					}
				}
			}
			
			if(failed) 
				admenu.setLore(new ArrayList<String>());
			admenu.mode = "menu";
		}
		e.setCancelled(true);
		admenu.loadGUI();
		admenu.inv.openForPlayer(e.getPlayer());
		
	}
	
	@EventHandler
	public static void onPlayerMove(PlayerMoveEvent e) {
		if(!GUIManager.openGUIs.containsKey(e.getPlayer()) || !(GUIManager.openGUIs.get(e.getPlayer()) instanceof CreateAd) || e.isCancelled()) {
			return;
		}
		
		e.getPlayer().sendMessage(ChatColor.RED + "Use /ganame to set the text before you leave!");
		e.setCancelled(true);
	}
	
	@EventHandler
	public static void onInvClick(InventoryClickEvent e) {
		if(!GUIManager.openGUIs.containsKey(e.getWhoClicked()) || !(GUIManager.openGUIs.get(e.getWhoClicked()) instanceof CreateAd) || e.isCancelled()) {
			return;
		}
		
		CreateAd admenu = (CreateAd) GUIManager.openGUIs.get(e.getWhoClicked());
		
		if(e.getWhoClicked().getInventory() == e.getClickedInventory() && admenu.player.equals((Player) e.getWhoClicked())) {
			if(e.getCurrentItem() != null)
				admenu.display = e.getCurrentItem().getType();
		}
		admenu.loadGUI();
		
		e.setCancelled(true);
	}
	
	public String getMode() {
		return mode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<String> getLore() {
		return lore;
	}

	public void setLore(ArrayList<String> lore) {
		this.lore = lore;
	}
	
	public GUIInventory getInv() {
		return inv;
	}
}
