package de.maiksite.bukkit.JarOfMob.persistence;

/**
 * Exception that is thrown when a jar can't be saved.
 */
public class JarException extends Exception {
    public JarException(String message, Exception innerException) {
        super(message, innerException);
    }

    public JarException(Exception innerException) {
        super(innerException);
    }

    public JarException(String message) {
        super(message);
    }
}
