import de.craften.plugins.mobjar.jars.EmptyJar;
import de.craften.plugins.mobjar.jars.Jar;
import de.craften.plugins.mobjar.persistence.JarException;
import de.craften.plugins.mobjar.persistence.JarFileManager;
import de.craften.plugins.mobjar.persistence.JarNotFoundException;
import de.craften.plugins.mobjar.persistence.JarPersistence;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

import static org.junit.Assert.*;

public class JarFileManagerTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Rule
    public ExpectedException exception = ExpectedException.none();
    JarPersistence persistence;

    @Before
    public void initialize() throws IOException {
        persistence = new JarFileManager(folder.newFolder("jars"));
    }

    @Test
    public void testHasJar() throws JarException {
        persistence.addJar(new EmptyJar(1));
        assertTrue(persistence.hasJar(1));
        assertFalse(persistence.hasJar(2));
    }

    @Test
    public void testAddJar() throws JarException {
        Jar jar = new EmptyJar(1);
        persistence.addJar(jar);

        assertTrue(persistence.hasJar(1));
        assertEquals(persistence.getJar(1), jar);
    }

    @Test
    public void testLoadUnknownJar() throws JarException {
        exception.expect(JarNotFoundException.class);
        persistence.getJar(2);
    }

    @Test
    public void testLoadJar() throws JarException {
        Jar jar = new EmptyJar(42);
        persistence.addJar(jar);

        assertEquals(persistence.getJar(42), jar);
    }

    @Test
    public void testRemoveJar() throws JarException {
        persistence.addJar(new EmptyJar(42));
        persistence.removeJar(42);
        assertFalse(persistence.hasJar(42));
    }
}
