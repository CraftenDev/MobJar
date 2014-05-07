package de.maiksite.bukkit.JarOfMob;

import de.maiksite.bukkit.JarOfMob.jars.Jar;
import de.maiksite.bukkit.JarOfMob.persistence.JarException;
import de.maiksite.bukkit.JarOfMob.persistence.JarPersistence;
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
    private void playerDropItem(PlayerDropItemEvent event) {
        Jar droppedJar = jarFromItem(event.getItemDrop().getItemStack());
        if (droppedJar != null) {
            droppedJar.onDrop(event);
        }
    }

    @EventHandler
    private void playerInteract(PlayerInteractEvent event) {
        //_AIR-events are canceled by default, so ignore that
        if (event.getAction() != Action.LEFT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_AIR && event.isCancelled())
            return;
        event.setCancelled(false);

        Action action = event.getAction();
        Jar droppedJar = jarFromItem(event.getItem());
        if (droppedJar != null) {
            if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                droppedJar.onOpenJar(event);
            }
        }
    }

    @EventHandler
    private void playerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled())
            return;

        Jar jar = jarFromItem(event.getPlayer().getItemInHand());
        if (jar != null) {
            jar.onRightClickEntity(event);
        }
    }

    @EventHandler
    private void stopConsuming(PlayerItemConsumeEvent event) {
        Jar jar = jarFromItem(event.getPlayer().getItemInHand());
        if (jar != null) {
            jar.onDrinkJar(event);
        }
    }

    private Jar jarFromItem(ItemStack item) {
        try {
            return JarOfMobPlugin.getJars().getJar(Jar.getIdFromItemStack(item));
        } catch (JarException e) {
            e.printStackTrace();
            return null;
        }
    }
}
