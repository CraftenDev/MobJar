package de.craften.plugins.mobjar.persistence;

import de.craften.plugins.mobjar.jars.EmptyJar;
import de.craften.plugins.mobjar.jars.HorseJar;
import de.craften.plugins.mobjar.jars.Jar;
import de.craften.plugins.mobjar.persistence.serialization.SerializedHorse;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages jars using files.
 */
public class JarFileManager implements JarPersistence {
    private final File directory;
    private Map<Long, Jar> jarSave = new HashMap<Long, Jar>();

    public JarFileManager(File saveDirectory) {
        directory = saveDirectory;
    }

    @Override
    public boolean hasJar(long id) {
        return new File(directory, Long.toString(id)).exists();
    }

    @Override
    public Jar getJar(long id) throws JarException {
        if (jarSave.containsKey(id))
            return jarSave.get(id);

        File jarFile = new File(directory, Long.toString(id));
        if (jarFile.exists()) {
            try {
                YamlConfiguration fc = new YamlConfiguration();
                fc.load(jarFile);
                if (fc.getString("type").equals("horse")) {
                    return new HorseJar(id, new SerializedHorse(fc.getConfigurationSection("horse")));
                } else if (fc.getString("type").equals("empty")) {
                    return new EmptyJar(id);
                } else {
                    throw new JarException("Unknown jar type");
                }
            } catch (IOException e) {
                throw new JarException("Loading the jar failed", e);
            } catch (InvalidConfigurationException e) {
                throw new JarException("Loading the jar failed", e);
            }
        } else {
            throw new JarNotFoundException(id);
        }
    }

    @Override
    public void addJar(Jar jar) throws JarException {
        File jarFile = new File(directory, Long.toString(jar.getUniqueId()));

        try {
            FileConfiguration fc = new YamlConfiguration();

            if (jar instanceof HorseJar)
                fc.set("type", "horse");
            else if (jar instanceof EmptyJar)
                fc.set("type", "empty");
            else
                throw new JarException("Unknown jar type");

            if (jar.getSerialized() != null)
                fc.set("data", jar.getSerialized().serialize());
            
            fc.save(jarFile);
        } catch (IOException e) {
            throw new JarException(e);
        }

        jarSave.put(jar.getUniqueId(), jar);
    }

    @Override
    public void removeJar(long id) throws JarException {
        jarSave.remove(id);

        File jarFile = new File(directory, Long.toString(id));
        if (jarFile.exists()) {
            if (!jarFile.delete()) {
                throw new JarException("Could not delete jar");
            }
        }
    }
}
