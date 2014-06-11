package de.craften.plugins.mobjar.jars;

import de.craften.plugins.mobjar.MobJarPlugin;
import de.craften.plugins.mobjar.persistence.JarException;
import de.craften.plugins.mobjar.persistence.serialization.SerializedWolf;
import org.bukkit.Location;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

/**
 * A jar for wolves.
 */
public class WolfJar extends Jar<Wolf> {
    public WolfJar(long id, Wolf originalHorse) {
        super(id, new SerializedWolf(originalHorse));
    }

    public WolfJar(long id, SerializedWolf data) {
        super(id, data);
    }

    @Override
    public Wolf restoreTo(Location location) {
        Wolf wolf = location.getWorld().spawn(location, Wolf.class);
        getSerialized().applyOn(wolf);
        return wolf;
    }

    @Override
    public boolean canRestoreTo(Location location) {
        return location.getBlock().isEmpty();
    }

    @Override
    public String getName() {
        return "Liquid Wolf";
    }

    @Override
    protected ItemStack getJarItem() {
        Potion potion = new Potion(PotionType.WATER);
        return potion.toItemStack(1);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.isCancelled())
            return;
        event.setCancelled(true);
        Location restoreLoc = event.getClickedBlock().getLocation().add(0, 1, 0);

        if (canRestoreTo(restoreLoc)) {
            Wolf wolf = restoreTo(restoreLoc);
            wolf.setOwner(event.getPlayer());
            restoreLoc.getWorld().playEffect(restoreLoc, getRestoreEffect(), 0);

            event.getPlayer().getInventory().removeItem(event.getItem());

            Jar emptyJar = new EmptyJar(getUniqueId());
            try {
                MobJarPlugin.getJars().addJar(emptyJar);
            } catch (JarException e) {
                event.getPlayer().sendMessage(MobJarPlugin.PREFIX + "Could not open the jar.");
            }
            event.getPlayer().getInventory().addItem(emptyJar.getItem());
        } else {
            event.getPlayer().sendMessage(MobJarPlugin.PREFIX + "Not enough space to open the jar.");
        }
    }
}
