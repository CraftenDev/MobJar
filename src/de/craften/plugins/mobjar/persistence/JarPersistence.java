package de.craften.plugins.mobjar.persistence;

import de.craften.plugins.mobjar.jars.Jar;

/**
 * Loads and saves jars persistent.
 */
public interface JarPersistence {
    /**
     * Checks if a jar with the given ID exists.
     *
     * @param id ID of a jar
     * @return True if a jar with the given ID exists, false if not
     * @throws JarException If checking if the jar exists fails
     */
    public boolean hasJar(long id) throws JarException;

    /**
     * Loads the jar with the given ID.
     *
     * @param id ID of a jar
     * @return The jar with the given ID, if it exists. Null if that jar doesn't exist.
     * @throws JarException If loading the jar fails
     */
    public Jar getJar(long id) throws JarException;

    /**
     * Saves the given jar. A jar with the same ID is overwritten.
     *
     * @param jar The jar to save
     * @throws JarException If saving the jar fails
     */
    public void addJar(Jar jar) throws JarException;

    /**
     * Removes the given jar from the storage. If that jar is not saved, this is a no-op.
     *
     * @param id ID of the jar to remove
     * @throws JarException If removing the jar fails
     */
    public void removeJar(long id) throws JarException;
}
