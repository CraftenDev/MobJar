package de.craften.plugins.mobjar.persistence.serialization;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class SerializedCreature {
    protected final ConfigurationSection data;

    protected SerializedCreature() {
        data = new YamlConfiguration();
    }

    protected SerializedCreature(ConfigurationSection serializedData) {
        data = serializedData;
    }

    public final ConfigurationSection serialize() {
        return data;
    }
}
