package de.craften.plugins.mobjar.jars;

import de.craften.plugins.mobjar.persistence.serialization.SerializedWolf;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Wolf;
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
        return location.getBlock().isEmpty() && location.getBlock().getRelative(BlockFace.UP, 1).isEmpty();
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
}
