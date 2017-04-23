package de.craften.plugins.mobjar.jars;

import org.bukkit.entity.Creature;

public interface RestoreHandler<T extends Creature> {
    void onEntityRestored(T entity);

    void onRestoreFailed();
}
