package de.craften.plugins.mobjar.persistence.serialization;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;

public abstract class SerializedCreature<T extends Creature> {
    protected final ConfigurationSection data;

    private SerializedCreature() {
        data = new YamlConfiguration();
    }

    public SerializedCreature(T creature) {
        this();
        serialize(creature);
    }

    protected SerializedCreature(ConfigurationSection serializedData) {
        data = serializedData;
    }

    public final ConfigurationSection asConfigurationSection() {
        return data;
    }

    public abstract void applyOn(T creature);

    protected abstract void serialize(T creature);
}
