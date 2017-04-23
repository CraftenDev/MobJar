package de.craften.plugins.mobjar;

import de.craften.plugins.mobjar.jars.Jar;
import de.craften.plugins.mobjar.persistence.JarException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class JarListener implements Listener {
    @EventHandler
    public void playerDropItem(PlayerDropItemEvent event) {
        Jar droppedJar = jarFromItem(event.getItemDrop().getItemStack());
        if (droppedJar != null) {
            droppedJar.onDrop(event);
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        //_AIR-events are canceled by default, so ignore that
        if (event.getAction() != Action.LEFT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_AIR && event.isCancelled())
            return;
        event.setCancelled(false);

        Action action = event.getAction();
        Jar usedJar = jarFromItem(event.getItem());
        if (usedJar != null) {
            if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                usedJar.onLeftClick(event);
            } else {
                event.setCancelled(true); //disable any other interaction with jars, i.e. filling empty jars with water
            }
        }
    }

    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled())
            return;

        Jar jar = jarFromItem(event.getPlayer().getItemInHand());
        if (jar != null) {
            jar.onRightClickEntity(event);
        }
    }

    @EventHandler
    public void stopConsuming(PlayerItemConsumeEvent event) {
        Jar jar = jarFromItem(event.getItem());
        if (jar != null) {
            jar.onDrink(event);
        }
    }

    public Jar jarFromItem(ItemStack item) {
        try {
            Long id = Jar.getIdFromItemStack(item);
            if (id == null)
                return null;
            return MobJarPlugin.getJars().getJar(id);
        } catch (JarException e) {
            e.printStackTrace();
            return null;
        }
    }
}
