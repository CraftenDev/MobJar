package de.craften.plugins.mobjar.jars;

import de.craften.plugins.mobjar.MobJarPlugin;
import de.craften.plugins.mobjar.persistence.JarException;
import de.craften.plugins.mobjar.persistence.serialization.SerializedHorse;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Horse;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

/**
 * A jar that contains a horse.
 */
public class HorseJar extends Jar<Horse> {
    public HorseJar(long id, Horse originalHorse) {
        super(id, new SerializedHorse(originalHorse));
    }

    public HorseJar(long id, SerializedHorse data) {
        super(id, data);
    }

    @Override
    public Horse restoreTo(Location location) {
        Horse horse = location.getWorld().spawn(location, Horse.class);
        getSerialized().applyOn(horse);
        return horse;
    }

    @Override
    public boolean canRestoreTo(Location location) {
        return location.getBlock().isEmpty() && location.getBlock().getRelative(BlockFace.UP, 1).isEmpty();
    }

    @Override
    public String getName() {
        return "Liquid Horse";
    }

    @Override
    protected ItemStack getJarItem() {
        Potion potion = new Potion(PotionType.WATER);
        return potion.toItemStack(1);
    }

    @Override
    public void onLeftClick(final PlayerInteractEvent event) {
        if (event.isCancelled())
            return;
        event.setCancelled(true);

        final Location restoreLoc;
        if (event.getClickedBlock() != null) {
            restoreLoc = event.getClickedBlock().getLocation().add(0, 1, 0);
        } else {
            restoreLoc = event.getPlayer().getLocation()
                    .add(event.getPlayer().getLocation().getDirection().setY(0).multiply(2));
        }

        if (canRestoreTo(restoreLoc)) {
            tryRestoreTo(restoreLoc, new RestoreHandler<Horse>() {
                @Override
                public void onEntityRestored(Horse horse) {
                    horse.setOwner(event.getPlayer());
                    restoreLoc.getWorld().playEffect(restoreLoc, getRestoreEffect(), 0);

                    event.getPlayer().getInventory().removeItem(event.getItem());

                    Jar emptyJar = new EmptyJar(getUniqueId());
                    try {
                        MobJarPlugin.getJars().addJar(emptyJar);
                        event.getPlayer().getInventory().addItem(emptyJar.getItem());
                    } catch (JarException e) {
                        event.getPlayer().sendMessage(MobJarPlugin.PREFIX + "Could create a new empty jar.");
                    }
                }

                @Override
                public void onRestoreFailed() {
                    event.getPlayer().sendMessage(MobJarPlugin.PREFIX + "You can't open the jar here.");
                }
            });
        } else {
            event.getPlayer().sendMessage(MobJarPlugin.PREFIX + "Not enough space to open the jar.");
        }
    }
}
