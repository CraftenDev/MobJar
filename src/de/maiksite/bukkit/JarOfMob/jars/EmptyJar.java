package de.maiksite.bukkit.JarOfMob.jars;

import de.maiksite.bukkit.JarOfMob.JarOfMobPlugin;
import de.maiksite.bukkit.JarOfMob.persistence.JarException;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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
    public String getJarName() {
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
                JarOfMobPlugin.getJars().removeJar(getUniqueId());
                removedEmpty = true;
                JarOfMobPlugin.getJars().addJar(horseJar);
                player.getInventory().remove(player.getItemInHand());
                player.setItemInHand(horseJar.getItem());
                entity.remove();
                loc.getWorld().playEffect(entity.getLocation(), getRestoreEffect(), 0);
            } catch (JarException e) {
                player.sendMessage(JarOfMobPlugin.PREFIX + "Could not jar that animal.");
                if (removedEmpty)
                    try {
                        JarOfMobPlugin.getJars().addJar(this);
                    } catch (JarException e1) {
                        player.sendMessage(JarOfMobPlugin.PREFIX + "Could not give back an empty jar.");
                    }
            }
            return true;
        }

        return false;
    }

    @Override
    public void onOpenJar(PlayerInteractEvent event) {
        if (event.getPlayer().isInsideVehicle())
            jarEntity(event.getPlayer().getVehicle(), event.getPlayer());

        event.setCancelled(true);
    }

    @Override
    public void onDrinkJar(PlayerItemConsumeEvent event) {
        event.setCancelled(true);
    }

    @Override
    public Map<String, Object> getCreatureData() {
        return null;
    }
}
