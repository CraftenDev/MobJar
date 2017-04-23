package de.craften.plugins.mobjar.persistence.serialization;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Tameable;

import java.util.UUID;

public abstract class SerializedTameable<T extends Creature & Tameable> extends SerializedCreature<T> {
    public SerializedTameable(T creature) {
        super(creature);
    }

    protected SerializedTameable(ConfigurationSection serializedData) {
        super(serializedData);
    }

    @Override
    public void applyOn(T creature) {
        creature.setTamed(data.getBoolean("tamed", false));

        if (data.contains("ownerUUID")) {
            creature.setOwner(Bukkit.getOfflinePlayer(UUID.fromString(data.getString("ownerUUID"))));
        } else if (data.contains("owner")) { // compatibility with older versions of this plugin
            creature.setOwner(Bukkit.getOfflinePlayer(data.getString("owner")));
        }
    }

    @Override
    protected void serialize(T creature) {
        data.set("tamed", creature.isTamed());

        if (creature.getOwner() != null)
            data.set("ownerUUID", creature.getOwner().getUniqueId().toString());
    }
}
