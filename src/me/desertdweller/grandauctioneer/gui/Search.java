package me.desertdweller.grandauctioneer.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.desertdweller.grandauctioneer.items.Auctioneer;
import net.md_5.bungee.api.ChatColor;

public class Search implements Listener{
	Player player;
	private Auctioneer auctioneer;
	
	public Search(Player player, Auctioneer auctioneer) {
		this.player = player;
		this.auctioneer = auctioneer;
	}
	
//	//Ajax's code for cancelling a chat message in Macus's chat plugin.
//	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
//	public void grabNextChatMessage(AsyncPlayerChatEvent event){
//		String name = event.getMessage();
//		Player player = event.getPlayer();
//		// If the player is not a treaty naming player, we don't care
//		if(!chatPlayers.containsKey(player)) return;
//		
//		if(!name.equalsIgnoreCase("cancel")) {
//			mainMenu.setTreatyName(name, player);
//			
//		} else {
//			mainMenu.closeAndRemove(player);
//		}
//		event.setMessage("");
//		event.setCancelled(true);
//		chatPlayers.remove(event.getPlayer());
//	}
	
	
	@EventHandler
	public static void onPlayerMove(PlayerMoveEvent e) {
		if(!GUIManager.openGUIs.containsKey(e.getPlayer()) || !(GUIManager.openGUIs.get(e.getPlayer()) instanceof Search) || e.isCancelled()) {
			return;
		}
		
		e.getPlayer().sendMessage(ChatColor.RED + "Use /gasearch to input a search before you leave.");
		e.setCancelled(true);
	}
	
	@EventHandler
	public static void onPlayerChat(AsyncPlayerChatEvent e) {
		if(!GUIManager.openGUIs.containsKey(e.getPlayer()) || !(GUIManager.openGUIs.get(e.getPlayer()) instanceof Search)) {
			return;
		}
		
		Search search = (Search) GUIManager.openGUIs.get(e.getPlayer());
		GUIManager.openResultsPage(e.getPlayer(), ResultsType.SEARCH, e.getMessage(), search.getAuctioneer());
		e.setCancelled(true);
	}

	public Auctioneer getAuctioneer() {
		return auctioneer;
	}
	
}
