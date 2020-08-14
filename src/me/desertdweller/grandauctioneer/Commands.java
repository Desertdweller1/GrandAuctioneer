package me.desertdweller.grandauctioneer;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.desertdweller.grandauctioneer.datastorage.DataHandler;
import me.desertdweller.grandauctioneer.gui.CreateAd;
import me.desertdweller.grandauctioneer.gui.GUIManager;
import me.desertdweller.grandauctioneer.gui.ResultsType;
import me.desertdweller.grandauctioneer.gui.Search;
import me.desertdweller.grandauctioneer.items.AuctionItem;
import me.desertdweller.grandauctioneer.items.Auctioneer;
import me.desertdweller.grandauctioneer.items.AuctioneerHandler;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);
		if(cmd.getName().equalsIgnoreCase("ga") && sender.isOp()) {
			sender.sendMessage(ChatColor.YELLOW + "/ga - Shows all commands for the Grand Auctioneer plugin, (this you dummy)");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "/additem listing <player> <auctioneer> <price> - Forces the item you are holding to become part of the given player's listings.");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "/additem retrieval <player> <auctioneer> <held|gembag> <price> - Force the item/gembag to go into given player's retrieval page.");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "/auclist - Gives a list of all auctioneers.");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "/dumplogs - Dumps all logs into stored up into the day's logs immediately, rather than waiting for server shutdown.");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "/savedata - Saves all GA data onto file.");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "/loaddata - Loads all GA data from file.");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "/openauc <player> <auctioneer> - Opens the given auctioneer for the given player");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "/createauc <name> <x> <y> <z> - This creates an auctioneer at the given position.");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "/moveauc <auctioneer> <x> <y> <z> - This moves the auctioneer's position.");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "/deleteauc <auctioneer> - This deletes the auctioneer (ALONG WITH EVERY ITEM IT IS HOLDING!)");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.YELLOW + "All of these commands require op.");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("additem") && sender.isOp()) {
			if(args.length < 1)
				return false;
			if(args[0].equalsIgnoreCase("listing")) {
				if(args.length < 4)
					return false;
				Player player = Bukkit.getPlayer(args[1]);
				Auctioneer auctioneer = AuctioneerHandler.getAuctioneer(args[2]);
				
				if(sender instanceof Player && sender.isOp()) {
					if(player == null) {
						sender.sendMessage(ChatColor.RED + "Cannot find that player, only use online players.");
						return true;
					}
					if(auctioneer == null) {
						sender.sendMessage(ChatColor.RED + "Cannot find that auctioneer.");
						return true;
					}
					ItemStack item = ((HumanEntity) sender).getInventory().getItem(((HumanEntity) sender).getInventory().getHeldItemSlot());
					if(item == null) {
						System.out.println(player.getInventory().getItem(player.getInventory().getHeldItemSlot()));
						sender.sendMessage(ChatColor.RED + "You are not holding a proper item");
						return true;
					}
					auctioneer.addItem(item, player.getUniqueId(), Integer.parseInt(args[3]));
					sender.sendMessage(ChatColor.YELLOW + "Added item");
					return true;
				}
			
			}else if(args[0].equalsIgnoreCase("retrieval")) {
				if(args.length < 5)
					return false;
				Player player = Bukkit.getPlayer(args[1]);
				Auctioneer auctioneer = AuctioneerHandler.getAuctioneer(args[2]);
				
				if(player == null) {
					sender.sendMessage(ChatColor.RED + "Cannot find that player, only use online players.");
					return true;
				}
				if(auctioneer == null) {
					sender.sendMessage(ChatColor.RED + "Cannot find that auctioneer.");
					return true;
				}
				
				if(args[3].equalsIgnoreCase("held")) {
					ItemStack item = ((HumanEntity) sender).getInventory().getItem(((HumanEntity) sender).getInventory().getHeldItemSlot());
					if(item == null) {
						System.out.println(player.getInventory().getItem(player.getInventory().getHeldItemSlot()));
						sender.sendMessage(ChatColor.RED + "You are not holding a proper item");
						return true;
					}
					
					auctioneer.addItemToHeld(new AuctionItem(item, player.getUniqueId(), System.currentTimeMillis(), Integer.parseInt(args[4]), true, auctioneer));
					sender.sendMessage(ChatColor.YELLOW + "Added item");
					return true;
					
				}else if(args[3].equalsIgnoreCase("gembag")) {
					auctioneer.addItemToHeld(Auctioneer.getCurrencyPayment(Integer.parseInt(args[4]), player.getUniqueId(), auctioneer, ChatColor.YELLOW + "Gems"));
					sender.sendMessage(ChatColor.YELLOW + "Added item");
					return true;
				}
			}
		}else if(cmd.getName().equalsIgnoreCase("removeitems") && sender.isOp()) {
			if(args.length < 3)
				return false;
			Player player = null;
			Auctioneer auc = null;
			if(!args[0].equalsIgnoreCase("all"))
				player = Bukkit.getPlayer(args[0]);
			if(!args[1].equalsIgnoreCase("all"))
				auc = AuctioneerHandler.getAuctioneer(args[1]);
			if(player == null) {
				if(auc == null) {
					if(args[2].equalsIgnoreCase("all")) {
						AuctioneerHandler.clearAuctions();
						AuctioneerHandler.clearHeld();
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}else if(args[2].equalsIgnoreCase("listing")) {
						AuctioneerHandler.clearAuctions();
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}else if(args[2].equalsIgnoreCase("retrieval")) {
						AuctioneerHandler.clearHeld();
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}
				}else {
					if(args[2].equalsIgnoreCase("all")) {
						auc.removeAllFromAuction();
						auc.removeAllFromHeld();
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}else if(args[2].equalsIgnoreCase("listing")) {
						auc.removeAllFromAuction();
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}else if(args[2].equalsIgnoreCase("retrieval")) {
						auc.removeAllFromHeld();
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}
				}
			}else {
				if(auc == null) {
					if(args[2].equalsIgnoreCase("all")) {
						AuctioneerHandler.clearPlayerAuctions(player);
						AuctioneerHandler.clearPlayerHeld(player);
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}else if(args[2].equalsIgnoreCase("listing")) {
						AuctioneerHandler.clearPlayerAuctions(player);
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}else if(args[2].equalsIgnoreCase("retrieval")) {
						AuctioneerHandler.clearPlayerHeld(player);
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}
				}else {
					if(args[2].equalsIgnoreCase("all")) {
						auc.removePlayerAuctions(player);
						auc.removePlayerHeld(player);
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}else if(args[2].equalsIgnoreCase("listing")) {
						auc.removePlayerAuctions(player);
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}else if(args[2].equalsIgnoreCase("retrieval")) {
						auc.removePlayerHeld(player);
						sender.sendMessage(ChatColor.YELLOW + "Done.");
						return true;
					}
				}
			}
			
		}else if(cmd.getName().equalsIgnoreCase("openauc") && sender.isOp()){
			if(args.length < 2) {
				return false;
			}
			Player p = Bukkit.getPlayer(args[0]);
			if(p == null)
				return false;
			Auctioneer auc = AuctioneerHandler.getAuctioneer(args[1]);
			if(auc == null)
				return false;
			GUIManager.openGrandAuction(p, auc);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("auclist") && sender.isOp()) {
			for(String name : AuctioneerHandler.getAuctioneerNames()) {
				sender.sendMessage(ChatColor.YELLOW + name);
			}
			return true;
		}else if(cmd.getName().equalsIgnoreCase("dumplogs") && sender.isOp()) {
			Logger.dump();
			sender.sendMessage(ChatColor.GRAY + "Dumped Logs");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("savedata") && sender.isOp()) {
			DataHandler.save();
			sender.sendMessage(ChatColor.GRAY + "Saved.");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("loaddata") && sender.isOp()) {
			DataHandler.load();
			sender.sendMessage(ChatColor.GRAY + "Loaded.");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("createauc") && sender.isOp()) {
			if(args.length != 5)
				return false;
			AuctioneerHandler.newAuctioneer(new Location(GrandAuctioneer.getMultiverseCore().getMVWorldManager().getMVWorld(args[4]).getCBWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])),args[0]);
			sender.sendMessage(ChatColor.GREEN + "Auctioneer: " + args[0] + ", successfully created!");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("moveauc") && sender.isOp()) {
			if(args.length != 5)
				return false;
			AuctioneerHandler.moveAuctioneer(new Location(GrandAuctioneer.getMultiverseCore().getMVWorldManager().getMVWorld(args[4]).getCBWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])),AuctioneerHandler.getAuctioneer(args[0]));
			sender.sendMessage(ChatColor.GREEN + "Auctioneer: " + args[0] + ", successfully moved.");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("deleteauc") && sender.isOp()) {
			if(args.length != 1)
				return false;
			AuctioneerHandler.deleteAuctioneer(AuctioneerHandler.getAuctioneer(args[0]));
			sender.sendMessage(ChatColor.GREEN + "Auctioneer: " + args[0] + ", successfully deleted.");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("gasearch") && sender instanceof Player) {
			Player p = (Player) sender;
			if(!GUIManager.openGUIs.containsKey(p) || !(GUIManager.openGUIs.get(p) instanceof Search)) {
				return false;
			}
			Search search = (Search) GUIManager.openGUIs.get(p);
			GUIManager.openResultsPage(p, ResultsType.SEARCH, args[0], search.getAuctioneer());
			return true;
		}else if(cmd.getName().equalsIgnoreCase("ganame") && sender instanceof Player) {
			Player p = (Player) sender;
			String msg = "";
			for(String arg : args) {
				msg = msg + arg + " ";
			} 
			
			if(!GUIManager.openGUIs.containsKey(p) || !(GUIManager.openGUIs.get(p) instanceof CreateAd)) {
				return false;
			}
			
			CreateAd admenu = (CreateAd) GUIManager.openGUIs.get(p);
			String coloredText = ChatColor.translateAlternateColorCodes('&', msg);
			
			
			if(admenu.getMode() == "setTitle") {
				if(msg.length() > plugin.getConfig().getInt("maxAdTitleChars")) {
					p.sendMessage(ChatColor.RED+"That was " + msg.length() + " characters long, and needs to be under " + plugin.getConfig().getInt("maxAdTitleChars") + ".");
				}else {
					admenu.setTitle(coloredText);
				}
			}else if(admenu.getMode() == "setLore") {
				admenu.setLore(new ArrayList<String>());
				admenu.getLore().add("");
				Boolean failed = false;
				int lineID = 0;
				String[] words = coloredText.split(" ");
				for(int i = 0; i < words.length; i++) {
					if(words[i].length() > plugin.getConfig().getInt("maxAdLoreLineChars")) {
						p.sendMessage(ChatColor.RED+"All words must be under " + plugin.getConfig().getInt("maxAdLoreLineChars") + " characters long.");
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
			}
			admenu.loadGUI();
			admenu.getInv().openForPlayer(p);
			return true;
		}
		return false;
	}
	
}
