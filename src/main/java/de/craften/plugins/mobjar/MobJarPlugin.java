package de.craften.plugins.mobjar;

import de.craften.plugins.mobjar.jars.EmptyJar;
import de.craften.plugins.mobjar.jars.Jar;
import de.craften.plugins.mobjar.persistence.FileJarPersistence;
import de.craften.plugins.mobjar.persistence.JarException;
import de.craften.plugins.mobjar.persistence.JarPersistence;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MobJarPlugin extends JavaPlugin {
    public static final String PREFIX = ChatColor.GOLD + "[MobJar] " + ChatColor.RESET;
    private static JarPersistence jars;
    private Economy economy;

    public static JarPersistence getJars() {
        return jars;
    }

    @Override
    public void onEnable() {
        File jarDir = new File(getDataFolder(), "jars");
        if (!jarDir.exists())
            jarDir.mkdirs();
        jars = new FileJarPersistence(jarDir);

        if (!setupEconomy() && getConfig().getDouble("jarprice") != 0)
            getLogger().warning("No economy plugin found (or missing Vault), jars will be free!");

        Bukkit.getPluginManager().registerEvents(new JarListener(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("jar") && sender instanceof Player) {
            boolean didPay = false;

            if (economy != null && getConfig().getDouble("jarprice") > 0 && !sender.hasPermission("mobjar.free")) {
                sender.sendMessage(PREFIX + String.format("That's %s, please!", economy.format(getConfig().getDouble("jarprice"))));

                EconomyResponse payment = economy.withdrawPlayer(sender.getName(), getConfig().getDouble("jarprice"));
                if (!payment.transactionSuccess()) {
                    sender.sendMessage(PREFIX + "You don't have enough money.");
                    return true;
                } else {
                    didPay = true;
                }
            }

            Player player = (Player) sender;
            Jar emptyJar = new EmptyJar(System.currentTimeMillis());
            try {
                jars.addJar(emptyJar);
                player.getInventory().addItem(emptyJar.getItem());
            } catch (JarException e) {
                getLogger().warning("Could not save a jar!");
                if (didPay) {
                    economy.depositPlayer(sender.getName(), getConfig().getDouble("jarprice"));
                    player.sendMessage(PREFIX + "Sorry, could not create a jar, the payment was reversed.");
                } else {
                    player.sendMessage(PREFIX + "Sorry, could not create a jar.");
                }
            }

            return true;
        }

        return false;
    }

    private boolean setupEconomy() {
        try {
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
            return (economy != null);
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }
}
