package de.craften.plugins.mobjar.util;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class WorldUtil {
    /**
     * Gets the entity with the given UUID and type in the given world or null if such an entity doesn't exist.
     *
     * @param world       world
     * @param uniqueId    UUID of the entity
     * @param entityClass type of the entity
     * @param <T>         type of the entity
     * @return entity with the given UUID and type in the given world or null if such an entity doesn't exist
     */
    public static <T extends Entity> T getEntityByUuid(World world, UUID uniqueId, Class<T> entityClass) {
        for (T entity : world.getEntitiesByClass(entityClass)) {
            if (entity.getUniqueId().equals(uniqueId)) {
                return entity;
            }
        }
        return null;
    }
}
