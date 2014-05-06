package de.maiksite.bukkit.JarOfMob;

import de.maiksite.bukkit.JarOfMob.jars.Jar;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class JarListener implements Listener {
    @EventHandler
    private void playerDropItem(PlayerDropItemEvent event) {
        Jar droppedJar = Jar.getJar(event.getItemDrop().getItemStack());
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
        Jar droppedJar = Jar.getJar(event.getItem());
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

        Jar jar = Jar.getJar(event.getPlayer().getItemInHand());
        if (jar != null) {
            jar.onRightClickEntity(event);
        }
    }

    @EventHandler
    private void stopConsuming(PlayerItemConsumeEvent event) {
        Jar jar = Jar.getJar(event.getPlayer().getItemInHand());
        if (jar != null) {
            jar.onDrinkJar(event);
        }
    }

    @EventHandler
    private void destroyJar(ItemDespawnEvent event) {
        Jar jar = Jar.getJar(event.getEntity().getItemStack());
        if (jar != null) {
            JarOfMobPlugin.JARS.remove(jar.getUniqueId());
            Bukkit.getLogger().info("Jar deleted.");
        }
    }
}
