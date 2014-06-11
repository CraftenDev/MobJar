package de.craften.plugins.mobjar.jars;

import de.craften.plugins.mobjar.persistence.serialization.SerializedCreature;
import de.craften.plugins.mobjar.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * A jar that contains a mob.
 */
public abstract class Jar<T extends Creature> {
    /**
     * The unique ID of this jar.
     */
    private final long uniqueId;

    private final SerializedCreature<T> serializedCreature;

    public Jar(long uniqueId, SerializedCreature<T> serializedCreature) {
        this.uniqueId = uniqueId;
        this.serializedCreature = serializedCreature;
    }

    /**
     * Gets the ID of the jar with the given lore.
     *
     * @param lore Lore of a jar
     * @return ID of the jar or null if the lore is invalid
     */
    public static Long getIdFromLore(List<String> lore) {
        try {
            return Long.parseLong(lore.get(0).replaceAll(ChatColor.COLOR_CHAR + "", "").substring(1));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the ID of the jar that is represented by the given item. If the item is not a jar, null is returned.
     *
     * @param itemStack Item that represents a jar
     * @return Jar that is represented by the given item, null if the item is not a jar
     */
    public static Long getIdFromItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null)
            return null;

        return getIdFromLore(itemStack.getItemMeta().getLore());
    }

    /**
     * Checks if the item is a jar.
     *
     * @param item Item to check
     * @return true if the given item is a jar, false if not
     */
    public static boolean isJar(ItemStack item) {
        return getIdFromLore(item.getItemMeta().getLore()) != null;
    }

    /**
     * Gets the unique ID of this jar.
     *
     * @return Unique ID of this jar
     */
    public long getUniqueId() {
        return uniqueId;
    }

    /**
     * Gets the effect to play when the creature in this jar is restored.
     *
     * @return The effect to play when the creature in this jar is restored
     */
    public Effect getRestoreEffect() {
        return Effect.POTION_BREAK;
    }

    /**
     * Restores the jar's content to the given location.
     *
     * @param location Location to restore the content to
     * @return Creature that was just restored
     */
    public abstract T restoreTo(Location location);

    /**
     * Checks if the creature in this jar can be safely spawned at the given location.
     *
     * @param location Location to check
     * @return true if the creature in this jar can be safely spawned at the given location
     */
    public abstract boolean canRestoreTo(Location location);

    /**
     * Gets the serialized creature that is in this jar.
     *
     * @return The serialized creature that is in this jar
     */
    public final SerializedCreature<T> getSerialized() {
        return serializedCreature;
    }

    /**
     * Returns this jar's beautiful, human readable name.
     *
     * @return This jar's beautiful, human readable name
     */
    public abstract String getName();

    /**
     * Gets an item that represents this jar.
     * You may only call this once as the item should be unique!
     *
     * @return Item that represents this jar
     */
    public final ItemStack getItem() {
        ItemStack theJar = getJarItem();
        ItemMeta meta = theJar.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add(StringUtil.convertToInvisibleString("#" + getUniqueId()));
        meta.setLore(lore);
        meta.setDisplayName(getName());
        theJar.setItemMeta(meta);
        return theJar;
    }

    /**
     * Gets the item that represents this jar, without the lore.
     * Should be a potion.
     *
     * @return Item that represents this jar
     */
    protected abstract ItemStack getJarItem();

    /**
     * This method is called when a player opens this jar.
     *
     * @param event Event as raised by bukkit
     */
    public void onLeftClick(PlayerInteractEvent event) {
    }

    /**
     * This method is called when a player right-clicks an entity holding this jar.
     *
     * @param event Event as raised by bukkit
     */
    public void onRightClickEntity(PlayerInteractEntityEvent event) {
    }

    /**
     * This method is called when a player drops this jar.
     *
     * @param event Event as raised by bukkit
     */
    public void onDrop(PlayerDropItemEvent event) {
    }

    /**
     * This method is called when a player drinks this jar.
     *
     * @param event Event as raised by bukkit
     */
    public void onDrink(PlayerItemConsumeEvent event) {
        event.setCancelled(true);
    }
}
