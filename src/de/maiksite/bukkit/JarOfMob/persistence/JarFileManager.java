package de.maiksite.bukkit.JarOfMob.persistence;

import de.maiksite.bukkit.JarOfMob.jars.Jar;

import java.io.*;

/**
 * Manages jars using files.
 */
public class JarFileManager implements JarPersistence {
    private final File directory;

    public JarFileManager(File saveDirectory) {
        directory = saveDirectory;
    }

    @Override
    public boolean hasJar(long id) {
        return new File(directory, Long.toString(id)).exists();
    }

    @Override
    public Jar getJar(long id) throws JarException {
        File jarFile = new File(directory, Long.toString(id));
        if (jarFile.exists()) {
            try {
                FileInputStream fin = new FileInputStream(jarFile);
                ObjectInputStream ois = new ObjectInputStream(fin);
                Jar jar = (Jar) ois.readObject();
                ois.close();
                return jar;
            } catch (IOException e) {
                throw new JarException("Loading the jar failed", e);
            } catch (ClassNotFoundException e) {
                throw new JarException("Could not deserialize the jar, this is a bug!", e);
            }
        } else {
            return null;
        }
    }

    @Override
    public void addJar(Jar jar) throws JarException {
        File jarFile = new File(directory, Long.toString(jar.getUniqueId()));

        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream(jarFile);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
            oos.close();
        } catch (FileNotFoundException e) {
            throw new JarException(e);
        } catch (IOException e) {
            throw new JarException(e);
        }
    }

    @Override
    public void removeJar(long id) throws JarException {

    }
}
