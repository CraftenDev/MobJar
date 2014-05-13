package de.craften.plugins.mobjar.persistence.serialization;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

/**
 * A horse that can be saved as and loaded from a string.
 */
public class SerializedHorse extends SerializedCreature {
    public SerializedHorse(Horse horse) {
        super();
        serializeHorse(horse);
    }

    public SerializedHorse(ConfigurationSection serialized) {
        super(serialized);
    }

    @SuppressWarnings("unchecked")
    public void applyTo(Horse horse) {
        horse.setColor(Horse.Color.valueOf(data.getString("color")));
        horse.setStyle(Horse.Style.valueOf(data.getString("style")));
        horse.setAge(data.getInt("age"));
        horse.setJumpStrength(data.getDouble("jumpStrength"));
        horse.setMaxDomestication(data.getInt("maxDomestication"));
        horse.setDomestication(data.getInt("domestication"));
        horse.setVariant(Horse.Variant.valueOf(data.getString("variant")));

        if (data.contains("owner"))
            horse.setOwner(Bukkit.getOfflinePlayer(data.getString("owner")));

        if (data.contains("chest")) {
            horse.setCarryingChest(true);
            InventorySerializer.deserialize(data.get("chest"), horse.getInventory());
        } else {
            horse.setCarryingChest(false);
        }

        if (data.contains("armor")) {
            horse.getInventory().setArmor(ItemStack.deserialize(data.getConfigurationSection("armor").getValues(true)));
        }
        if (data.contains("saddle")) {
            horse.getInventory().setSaddle(ItemStack.deserialize(data.getConfigurationSection("saddle").getValues(true)));
        }
    }

    private void serializeHorse(Horse horse) {
        data.set("color", horse.getColor().name());
        data.set("style", horse.getStyle().name());
        data.set("age", horse.getAge());
        data.set("jumpStrength", horse.getJumpStrength());
        data.set("maxDomestication", horse.getMaxDomestication());
        data.set("domestication", horse.getDomestication());
        data.set("variant", horse.getVariant().name());

        if (horse.isTamed())
            data.set("owner", horse.getOwner().getName());

        if (horse.getInventory().getArmor() != null) {
            data.createSection("armor", horse.getInventory().getArmor().serialize());
        }

        if (horse.getInventory().getSaddle() != null)
            data.createSection("saddle", horse.getInventory().getSaddle().serialize());

        if (horse.isCarryingChest()) {
            data.set("chest", InventorySerializer.serialize(horse.getInventory()));
        }
    }
}
