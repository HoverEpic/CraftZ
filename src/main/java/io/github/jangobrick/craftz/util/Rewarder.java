package io.github.jangobrick.craftz.util;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.jangobrick.craftz.CraftZ;

public class Rewarder {

    public static Economy economy = null;

    public static boolean setup() {

        try {

            RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null)
                economy = economyProvider.getProvider();

        } catch (NoClassDefFoundError err) {
        }

        return (economy != null);

    }

    public static void give(OfflinePlayer player, double amount) {

        if (economy == null || amount == 0)
            return;

        if (amount > 0)
            economy.depositPlayer(player, amount);
        else
            economy.withdrawPlayer(player, -amount);

    }

    public static String formatMoney(double money) {

        if (economy == null)
            return "" + money;

        return economy.format(money);

    }

    public static enum RewardType {

        KILL_ZOMBIE("Config.players.rewards.amount-kill-zombie", "Messages.rewards.message"),
        KILL_PLAYER("Config.players.rewards.amount-kill-player", "Messages.rewards.message"),
        HEAL_PLAYER("Config.players.rewards.amount-heal-player", "Messages.rewards.message");

        public static final String enableNotificationsEntry = "Config.players.rewards.enable-notifications";

        public final String configEntry;
        public final String messageEntry;

        private RewardType(String configEntry, String messageEntry) {
            this.configEntry = configEntry;
            this.messageEntry = messageEntry;
        }

        public double getReward() {
            return ConfigManager.getConfig("config").getDouble(configEntry);
        }

        public boolean getNotificationsEnabled() {
            return ConfigManager.getConfig("config").getBoolean(enableNotificationsEntry);
        }

        public String getNotification() {
            return CraftZ.getMsg(messageEntry);
        }

        public String formatNotification() {
            return getNotification().replace("%m", formatMoney(getReward()));
        }

        public void reward(OfflinePlayer player) {

            give(player, getReward());

            Player p = player.getPlayer();
            if (p != null && getNotificationsEnabled())
                p.sendMessage(ChatColor.GOLD + formatNotification());

        }

    }

}
