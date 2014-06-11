import de.craften.plugins.mobjar.persistence.serialization.SerializedWolf;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Wolf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
public class SerializedWolfTest {
    @Test
    public void testSerializeWolf() {
        Wolf wolf = givenAWolf();
        SerializedWolf serializedWolf = new SerializedWolf(wolf);

        wolf = mock(Wolf.class);
        serializedWolf.applyOn(wolf);

        verifyEqualsGivenWolf(wolf);
    }

    private Wolf givenAWolf() {
        Wolf wolf = mock(Wolf.class);
        when(wolf.getCustomName()).thenReturn("Some wild wolf");
        when(wolf.isCustomNameVisible()).thenReturn(true);
        when(wolf.getCollarColor()).thenReturn(DyeColor.GREEN);

        return wolf;
    }

    private void verifyEqualsGivenWolf(Wolf wolf) {
        verify(wolf).setCustomName(eq("Some wild wolf"));
        verify(wolf).setCustomNameVisible(eq(true));
        verify(wolf).setCollarColor(eq(DyeColor.GREEN));
    }
}
