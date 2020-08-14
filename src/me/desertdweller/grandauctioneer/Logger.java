package me.desertdweller.grandauctioneer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.plugin.Plugin;

public class Logger {
	private static ArrayList<String> actions = new ArrayList<String>();
	static Plugin plugin = GrandAuctioneer.getPlugin(GrandAuctioneer.class);
	
	static public void addLog(String string) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
		Date date = new Date();
		String time = formatter.format(date);
		actions.add(time + " - " + string);
	}
	
	static public void dump() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy//MM//dd");  
		Date date = new Date();
		String day = formatter.format(date);
 		File logFolder = new File("plugins//GrandAuctioneer//logs//" + day);
 		if(!logFolder.exists()) {
 			logFolder.mkdirs();
 		}
 		File currentLog = new File(logFolder + "//log.txt");
 		try {
			if(currentLog.createNewFile()) {
				System.out.println("[Grand Auctioneer] Created new log");
			}
 			FileWriter writer = new FileWriter(currentLog, true);
 			BufferedWriter bufferedWriter = new BufferedWriter(writer);
 			for(String action : actions) {
 				bufferedWriter.write(action);
 				bufferedWriter.newLine();
 			}
 			bufferedWriter.close();
 		}catch(IOException e) {
 				e.printStackTrace();
 		}
	}
}
