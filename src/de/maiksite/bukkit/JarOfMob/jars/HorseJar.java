package de.maiksite.bukkit.JarOfMob.jars;

import de.maiksite.bukkit.JarOfMob.JarOfMobPlugin;
import de.maiksite.bukkit.JarOfMob.serialization.HorseSerializer;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Horse;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.Map;

/**
 * A jar that contains a horse.
 */
public class HorseJar extends Jar {
    /**
     * Details about the horse in this jar.
     *
     * @serial
     */
    private Map<String, Object> horseData;

    public HorseJar(long id, Horse originalHorse) {
        super(id);
        horseData = HorseSerializer.serialize(originalHorse);
        originalHorse.remove();
    }

    @Override
    public Effect getRestoreEffect() {
        return Effect.POTION_BREAK;
    }

    @Override
    public Creature restoreTo(Location location) {
        Horse horse = location.getWorld().spawn(location, Horse.class);
        HorseSerializer.deserialize(horseData, horse);
        return horse;
    }

    @Override
    public boolean canRestoreTo(Location location) {
        return location.getBlock().isEmpty() && location.getBlock().getRelative(BlockFace.UP, 1).isEmpty();
    }

    @Override
    public String getJarName() {
        return "Liquid Horse";
    }

    @Override
    protected ItemStack getJarItem() {
        Potion potion = new Potion(PotionType.WATER);
        return potion.toItemStack(1);
    }

    @Override
    public void onOpenJar(PlayerInteractEvent event) {
        Location restoreLoc = event.getPlayer().getLocation().add(event.getPlayer().getLocation().getDirection().multiply(2));
        if (canRestoreTo(restoreLoc)) {
            Horse horse = (Horse) restoreTo(restoreLoc);
            horse.setOwner(event.getPlayer());
            restoreLoc.getWorld().playEffect(restoreLoc, getRestoreEffect(), 0);

            JarOfMobPlugin.JARS.remove(getUniqueId());
            event.getPlayer().getInventory().removeItem(event.getItem());

            Jar emptyJar = new EmptyJar(getUniqueId());
            event.getPlayer().getInventory().addItem(emptyJar.getItem());
            JarOfMobPlugin.JARS.put(getUniqueId(), emptyJar);
        } else {
            event.getPlayer().sendMessage(JarOfMobPlugin.PREFIX + "Not enough space to open the jar.");
        }
        event.setCancelled(true);
    }

    @Override
    public void onRightClickEntity(PlayerInteractEntityEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onDrinkJar(PlayerItemConsumeEvent event) {
        if (event.getPlayer().getVehicle() == null) {
            Location restoreLoc = event.getPlayer().getLocation();
            Horse horse = (Horse) restoreTo(restoreLoc);
            horse.setPassenger(event.getPlayer());
            horse.setOwner(event.getPlayer());
            restoreLoc.getWorld().playEffect(restoreLoc, getRestoreEffect(), 0);

            JarOfMobPlugin.JARS.remove(getUniqueId());
            event.getPlayer().getInventory().removeItem(event.getItem());

            Jar emptyJar = new EmptyJar(getUniqueId());
            event.getPlayer().getInventory().addItem(emptyJar.getItem());
            JarOfMobPlugin.JARS.put(getUniqueId(), emptyJar);
        }
        event.setCancelled(true);
    }
}
