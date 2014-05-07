package de.maiksite.bukkit.JarOfMob.persistence.serialization;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Serializes inventories.
 */
public class InventorySerializer {
    public static Object serialize(Inventory inventory) {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (ItemStack is : inventory.getContents()) {
            if (is != null)
                items.add(is.serialize());
            else
                items.add(null);
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    public static void deserialize(Object data, Inventory inventory) {
        try {
            List<Map<String, Object>> serializedItems = (List<Map<String, Object>>) data;
            ItemStack[] items = new ItemStack[serializedItems.size()];
            for (int i = 0; i < items.length; i++) {
                if (serializedItems.get(i) != null)
                    items[i] = ItemStack.deserialize(serializedItems.get(i));
            }
            inventory.setContents(items);
        } catch (ClassCastException e) {
            //ignore
        }
    }
}
