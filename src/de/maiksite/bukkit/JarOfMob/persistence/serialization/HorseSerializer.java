package de.maiksite.bukkit.JarOfMob.persistence.serialization;

import org.bukkit.Bukkit;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Serializes and deserializes horses.
 */
public class HorseSerializer {
    public static Map<String, Object> serialize(Horse horse) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("color", horse.getColor().name());
        data.put("style", horse.getStyle().name());
        data.put("age", horse.getAge());
        data.put("jumpStrength", horse.getJumpStrength());
        data.put("maxDomestication", horse.getMaxDomestication());
        data.put("domestication", horse.getDomestication());
        data.put("variant", horse.getVariant().name());

        if (horse.isTamed())
            data.put("owner", horse.getOwner().getName());

        if (horse.getInventory().getArmor() != null)
            data.put("armor", horse.getInventory().getArmor().serialize());
        if (horse.getInventory().getSaddle() != null)
            data.put("saddle", horse.getInventory().getSaddle().serialize());

        if (horse.isCarryingChest()) {
            data.put("chest", InventorySerializer.serialize(horse.getInventory()));
        }

        return data;
    }

    @SuppressWarnings("unchecked")
    public static void deserialize(Map<String, Object> data, Horse horse) {
        horse.setColor(Horse.Color.valueOf((String) data.get("color")));
        horse.setStyle(Horse.Style.valueOf((String) data.get("style")));
        horse.setAge((Integer) data.get("age"));
        horse.setJumpStrength((Double) data.get("jumpStrength"));
        horse.setMaxDomestication((Integer) data.get("maxDomestication"));
        horse.setDomestication((Integer) data.get("domestication"));
        horse.setVariant(Horse.Variant.valueOf((String) data.get("variant")));

        if (data.containsKey("owner"))
            horse.setOwner(Bukkit.getOfflinePlayer((String) data.get("owner")));

        if (data.containsKey("chest")) {
            horse.setCarryingChest(true);
            InventorySerializer.deserialize(data.get("chest"), horse.getInventory());
        }

        if (data.containsKey("armor")) {
            horse.getInventory().setArmor(ItemStack.deserialize((Map<String, Object>) data.get("armor")));
        }
        if (data.containsKey("saddle")) {
            horse.getInventory().setSaddle(ItemStack.deserialize((Map<String, Object>) data.get("saddle")));
        }
    }
}
