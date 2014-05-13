package de.craften.plugins.mobjar.jars;

import de.craften.plugins.mobjar.MobJarPlugin;
import de.craften.plugins.mobjar.persistence.JarException;
import de.craften.plugins.mobjar.persistence.serialization.SerializedCreature;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EmptyJar extends Jar {
    public EmptyJar(long uniqueId) {
        super(uniqueId);
    }

    @Override
    public Effect getRestoreEffect() {
        return Effect.SMOKE;
    }

    @Override
    public Creature restoreTo(Location location) {
        return null;
    }

    @Override
    public boolean canRestoreTo(Location location) {
        return false;
    }

    @Override
    public SerializedCreature getSerialized() {
        return null;
    }

    @Override
    public String getName() {
        return "Empty mob jar";
    }

    @Override
    protected ItemStack getJarItem() {
        return new ItemStack(Material.GLASS_BOTTLE, 1);
    }

    @Override
    public void onRightClickEntity(PlayerInteractEntityEvent event) {
        if (!event.getPlayer().hasPermission("mobjar.use"))
            return;

        if (jarEntity(event.getRightClicked(), event.getPlayer()))
            event.setCancelled(true);
    }

    private boolean jarEntity(Entity entity, Player player) {
        if (entity.getPassenger() != null
                && !player.hasPermission("mobjar.takemounted"))
            return false;
        if (entity instanceof Tameable && ((Tameable) entity).getOwner() != player
                && !player.hasPermission("mobjar.steal"))
            return true;

        if (entity instanceof Horse) {
            Location loc = entity.getLocation();
            Jar horseJar = new HorseJar(getUniqueId(), (Horse) entity);
            boolean removedEmpty = false;
            try {
                MobJarPlugin.getJars().removeJar(getUniqueId());
                entity.remove();
                removedEmpty = true;
                MobJarPlugin.getJars().addJar(horseJar);
                player.getInventory().removeItem(player.getItemInHand());
                player.getInventory().addItem(horseJar.getItem());
                loc.getWorld().playEffect(entity.getLocation(), getRestoreEffect(), 0);
            } catch (JarException e) {
                player.sendMessage(MobJarPlugin.PREFIX + "Could not jar that animal.");
                if (removedEmpty)
                    try {
                        MobJarPlugin.getJars().addJar(this);
                    } catch (JarException e1) {
                        player.sendMessage(MobJarPlugin.PREFIX + "Could not give back an empty jar.");
                    }
            }
            return true;
        }

        return false;
    }

    @Override
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.getPlayer().isInsideVehicle())
            jarEntity(event.getPlayer().getVehicle(), event.getPlayer());

        event.setCancelled(true);
    }
}
