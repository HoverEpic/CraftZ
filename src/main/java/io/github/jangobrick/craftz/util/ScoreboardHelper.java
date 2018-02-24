package io.github.jangobrick.craftz.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardHelper {

    private static ScoreboardManager manager;

    private static HashMap<UUID, Scoreboard> boards = new HashMap<UUID, Scoreboard>();

    public static void setup() {
        manager = Bukkit.getScoreboardManager();
    }

    public static void createPlayer(Player p) {

        Scoreboard board = manager.getNewScoreboard();
        Objective stats = board.registerNewObjective("stats", "dummy");
        stats.setDisplayName("Stats");
        stats.getScore("Blood level").setScore(0);
        stats.getScore("Zombies killed").setScore(0);
        stats.getScore("Players killed").setScore(0);
        stats.getScore("Minutes survived").setScore(0);

        boards.put(p.getUniqueId(), board);

    }

    public static void update() {

        ArrayList<UUID> toRemove = new ArrayList<UUID>();

        for (UUID id : boards.keySet()) {

            if (PlayerManager.p(id) == null) {
                toRemove.add(id);
                continue;
            }

            Player p = PlayerManager.p(id);
            Scoreboard board = boards.get(id);
            Objective stats = board.getObjective("stats");

            stats.getScore("Blood level").setScore((int) (((Damageable) p).getHealth() * 600));
            stats.getScore("Zombies killed").setScore(PlayerManager.getData(id).zombiesKilled);
            stats.getScore("Players killed").setScore(PlayerManager.getData(id).playersKilled);
            stats.getScore("Minutes survived").setScore(PlayerManager.getData(id).minutesSurvived);

            if (ConfigManager.getConfig("config").getBoolean("Config.players.use-scoreboard-for-stats")) {

                if (stats.getDisplaySlot() != DisplaySlot.SIDEBAR)
                    stats.setDisplaySlot(DisplaySlot.SIDEBAR);

                if (p.getScoreboard() != board)
                    p.setScoreboard(board);

            } else if (board.getObjective(DisplaySlot.SIDEBAR) == stats)
                board.clearSlot(DisplaySlot.SIDEBAR);

        }

        for (UUID id : toRemove)
            removePlayer(id);

    }

    public static void removePlayer(UUID id) {

        if (boards.containsKey(id))
            boards.get(id).clearSlot(DisplaySlot.SIDEBAR);

        boards.remove(id);

    }

}
