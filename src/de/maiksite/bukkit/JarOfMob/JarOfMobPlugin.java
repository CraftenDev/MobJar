package de.maiksite.bukkit.JarOfMob;

import de.maiksite.bukkit.JarOfMob.jars.EmptyJar;
import de.maiksite.bukkit.JarOfMob.jars.Jar;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JarOfMobPlugin extends JavaPlugin {
    public static final String PREFIX = ChatColor.GOLD + "[MobJar] " + ChatColor.RESET;
    public static final HashMap<Long, Jar> JARS = new HashMap<Long, Jar>();
    private Economy economy;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new JarListener(), this);
        getDataFolder().mkdirs();
        for (File file : getDataFolder().listFiles()) {
            try {
                Jar jar = Jar.load(file);
                JARS.put(jar.getUniqueId(), jar);
            } catch (Exception e) {
                getLogger().warning("Could not deserialize \"" + file.getName() + "\".");
            }

        }

        if (!setupEconomy() && getConfig().getDouble("jarprice") != 0)
            getLogger().info("No economy plugin found (or missing Vault), jars will be free!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("jar") && sender instanceof Player) {
            if (economy != null && getConfig().getDouble("jarprice") > 0 && !sender.hasPermission("mobjar.free")) {
                sender.sendMessage(PREFIX + String.format("That's %s, please!", economy.format(getConfig().getDouble("jarprice"))));

                EconomyResponse payment = economy.withdrawPlayer(sender.getName(), getConfig().getDouble("jarprice"));
                if (!payment.transactionSuccess()) {
                    sender.sendMessage(PREFIX + "You don't have enough money.");
                    return true;
                }
            }
            Player player = (Player) sender;
            Jar emptyJar = new EmptyJar(System.currentTimeMillis());
            player.getInventory().addItem(emptyJar.getItem());
            JARS.put(emptyJar.getUniqueId(), emptyJar);

            return true;
        }

        return false;
    }

    @Override
    public void onDisable() {
        for (Map.Entry<Long, Jar> jar : JARS.entrySet()) {
            try {
                jar.getValue().save(new File(getDataFolder(), jar.getValue().getUniqueId() + ".dat"));
            } catch (IOException e) {
                getLogger().warning("Could not serialize jar #" + jar.getValue().getUniqueId());
            }
        }
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
