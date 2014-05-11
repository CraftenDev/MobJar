package de.craften.plugins.mobjar.persistence;

/**
 * Exception that is thrown when a jar is not found.
 */
public class JarNotFoundException extends JarException {
    private long jarId;

    public JarNotFoundException(long id) {
        super("Jar " + id + " not found");
        jarId = id;
    }

    public long getJarId() {
        return jarId;
    }
}
