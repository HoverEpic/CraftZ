package craftZ;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardHelper {
	
	private static ScoreboardManager manager;
	
	private static HashMap<String, Scoreboard> boards = new HashMap<String, Scoreboard>();
	
	
	
	public static void setup() {
		manager = Bukkit.getScoreboardManager();
	}
	
	
	
	
	
	public static void createPlayer(Player p) {
		
		Scoreboard board = manager.getNewScoreboard();
		Objective stats = board.registerNewObjective("stats", "dummy");
		stats.setDisplayName("Stats");
		stats.getScore(Bukkit.getOfflinePlayer("Blood level")).setScore(0);
		stats.getScore(Bukkit.getOfflinePlayer("Zombies killed")).setScore(0);
		stats.getScore(Bukkit.getOfflinePlayer("Players killed")).setScore(0);
		stats.getScore(Bukkit.getOfflinePlayer("Minutes survived")).setScore(0);
		
		boards.put(p.getName(), board);
		
	}
	
	
	
	
	
	public static void update() {
		
		ArrayList<String> toRemove = new ArrayList<String>();
		
		for (String pn : boards.keySet()) {
			
			if (Bukkit.getPlayer(pn) == null) {
				toRemove.add(pn);
				continue;
			}
			
			Player p = Bukkit.getPlayer(pn);
			Scoreboard board = boards.get(pn);
			Objective stats = board.getObjective("stats");
			
			stats.getScore(Bukkit.getOfflinePlayer("Blood level")).setScore((int) (p.getHealth() * 600));
			stats.getScore(Bukkit.getOfflinePlayer("Zombies killed")).setScore(PlayerManager.getData(pn).zombiesKilled);
			stats.getScore(Bukkit.getOfflinePlayer("Players killed")).setScore(PlayerManager.getData(pn).playersKilled);
			stats.getScore(Bukkit.getOfflinePlayer("Minutes survived")).setScore(PlayerManager.getData(pn).minutesSurvived);
			
			if (CraftZ.instance.getConfig().getBoolean("Config.players.use-scoreboard-for-stats")) {
				
				if (stats.getDisplaySlot() != DisplaySlot.SIDEBAR)
					stats.setDisplaySlot(DisplaySlot.SIDEBAR);
				
				if (p.getScoreboard() != board)
					p.setScoreboard(board);
				
			} else {
				
				if (board.getObjective(DisplaySlot.SIDEBAR) == stats)
					board.clearSlot(DisplaySlot.SIDEBAR);
				
			}
			
		}
		
		
		
		for (String pn : toRemove)
			removePlayer(pn);
		
	}
	
	
	
	
	
	public static void removePlayer(String p) {
		boards.remove(p);
	}
	
}