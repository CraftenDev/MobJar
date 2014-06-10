package de.craften.plugins.mobjar.persistence.serialization;

import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Wolf;

public class SerializedWolf extends SerializedCreature<Wolf> {
    public SerializedWolf(Wolf wolf) {
        super(wolf);
    }

    public SerializedWolf(ConfigurationSection serialized) {
        super(serialized);
    }

    @Override
    public void applyOn(Wolf creature) {
        creature.setCustomName(data.getString("customName"));
        creature.setCustomNameVisible(data.getBoolean("showCustomName"));
        creature.setCollarColor(DyeColor.valueOf(data.getString("collarColor")));
    }

    @Override
    protected void serialize(Wolf creature) {
        data.set("customName", creature.getCustomName());
        data.set("showCustomName", creature.isCustomNameVisible());
        data.set("collarColor", creature.getCollarColor());
    }
}
