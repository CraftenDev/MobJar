package de.craften.plugins.mobjar.jars;

import de.craften.plugins.mobjar.MobJarPlugin;
import de.craften.plugins.mobjar.persistence.JarException;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EmptyJar extends Jar {
    public EmptyJar(long uniqueId) {
        super(uniqueId, null);
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

    /**
     * Tries to jar the given entity.
     *
     * @param entity Entity to jar
     * @param player Player that wants to put the entity into a jar
     * @return True if the entity was put into a jar, false if not
     */
    private boolean jarEntity(Entity entity, Player player) {
        if (entity.getPassenger() != null
                && !player.hasPermission("mobjar.takemounted"))
            return false;
        if (entity instanceof Tameable && ((Tameable) entity).getOwner() != null && !((Tameable) entity).getOwner().getUniqueId().equals(player.getUniqueId())
                && !player.hasPermission("mobjar.steal"))
            return false;

        Jar jar;
        if (entity instanceof Horse) {
            jar = new HorseJar(getUniqueId(), (Horse) entity);
        } else if (entity instanceof Wolf) {
            jar = new WolfJar(getUniqueId(), (Wolf) entity);
        } else {
            return false;
        }

        boolean removedEmpty = false;
        try {
            MobJarPlugin.getJars().removeJar(getUniqueId());
            Location loc = entity.getLocation();
            entity.remove();
            removedEmpty = true;
            MobJarPlugin.getJars().addJar(jar);
            player.getInventory().removeItem(player.getItemInHand());
            player.getInventory().addItem(jar.getItem());
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

    @Override
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.getPlayer().isInsideVehicle())
            jarEntity(event.getPlayer().getVehicle(), event.getPlayer());

        event.setCancelled(true);
    }
}
