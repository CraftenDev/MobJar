import de.craften.plugins.mobjar.persistence.serialization.SerializedHorse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class})
public class SerializedHorseTest {
    @Test
    public void testSerializeHorseWithoutAnything() {
        HorseInventory inventoryMock = mock(HorseInventory.class);
        when(inventoryMock.getArmor()).thenReturn(null);
        when(inventoryMock.getSaddle()).thenReturn(null);

        Horse horse = givenAHorse();
        when(horse.isCarryingChest()).thenReturn(false);
        when(horse.getInventory()).thenReturn(inventoryMock);

        SerializedHorse serializedHorse = new SerializedHorse(horse);
        horse = mock(Horse.class);
        serializedHorse.applyOn(horse);

        verifyEqualsGivenHorse(horse);
        verify(horse).setCarryingChest(eq(false));
    }

    @Test
    public void testSerializeHorseWithArmor() {
        givenBukkit();

        final ItemStack expectedArmor = new ItemStack(Material.GOLD_BARDING, 1);
        final ItemStack expectedSaddle = new ItemStack(Material.SADDLE, 1);

        HorseInventory inventoryMock = mock(HorseInventory.class);
        when(inventoryMock.getArmor()).thenReturn(expectedArmor);
        when(inventoryMock.getSaddle()).thenReturn(expectedSaddle);

        Horse horse = givenAHorse();
        when(horse.isCarryingChest()).thenReturn(false);
        when(horse.getInventory()).thenReturn(inventoryMock);

        SerializedHorse serializedHorse = new SerializedHorse(horse);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ItemStack armor = (ItemStack) invocationOnMock.getArguments()[0];
                assertNotNull(armor);
                //equals() won't work as bukkit checks equality by instance, so toString() will work as it returns the
                //material and the amount of an item
                assertEquals(armor.toString(), expectedArmor.toString());
                return null;
            }
        }).when(inventoryMock).setArmor(any(ItemStack.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ItemStack saddle = (ItemStack) invocationOnMock.getArguments()[0];
                assertNotNull(saddle);
                //equals() won't work as bukkit checks equality by instance, so toString() will work as it returns the
                //material and the amount of an item
                assertEquals(saddle.toString(), expectedSaddle.toString());
                return null;
            }
        }).when(inventoryMock).setSaddle(any(ItemStack.class));

        horse = mock(Horse.class);
        when(horse.getInventory()).thenReturn(inventoryMock);
        serializedHorse.applyOn(horse);

        verifyEqualsGivenHorse(horse);
    }

    @Test
    public void testSerializeHorseWithChest() {
        givenBukkit();

        ItemStack[] expectedInventory = new ItemStack[]{
                new ItemStack(Material.APPLE, 1)
        };

        HorseInventory inventoryMock = mock(HorseInventory.class);
        when(inventoryMock.getArmor()).thenReturn(null);
        when(inventoryMock.getSaddle()).thenReturn(null);
        when(inventoryMock.getContents()).thenReturn(expectedInventory);

        Horse horse = givenAHorse();
        when(horse.isCarryingChest()).thenReturn(true);
        when(horse.getInventory()).thenReturn(inventoryMock);

        SerializedHorse serializedHorse = new SerializedHorse(horse);

        final ItemStack[] horseInventory = new ItemStack[expectedInventory.length];
        when(inventoryMock.getContents()).thenReturn(horseInventory);
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ItemStack[] inv = (ItemStack[]) args[0];
                System.arraycopy(inv, 0, horseInventory, 0, horseInventory.length);
                return null;
            }
        }).when(inventoryMock).setContents(any(ItemStack[].class));
        horse = mock(Horse.class);
        when(horse.getInventory()).thenReturn(inventoryMock);
        serializedHorse.applyOn(horse);

        verifyEqualsGivenHorse(horse);
        verify(horse).setCarryingChest(eq(true));
        verify(inventoryMock, times(1)).setContents(any(ItemStack[].class));

        for (int i = 0; i < horseInventory.length; i++) {
            //equals() won't work as bukkit checks equality by instance, so toString() will work as it returns the
            //material and the amount of an item
            assertTrue(horseInventory[i].toString().equals(expectedInventory[i].toString()));
        }
    }

    private void givenBukkit() {
        mockStatic(Bukkit.class);
        ItemFactory itemFactory = mock(ItemFactory.class);
        ItemMeta mockedMeta = mock(ItemMeta.class);
        when(itemFactory.getItemMeta(any(Material.class))).thenReturn(mockedMeta);
        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
    }

    private Horse givenAHorse() {
        Horse horse = mock(Horse.class);
        when(horse.getColor()).thenReturn(Horse.Color.BROWN);
        when(horse.getVariant()).thenReturn(Horse.Variant.DONKEY);
        when(horse.getStyle()).thenReturn(Horse.Style.BLACK_DOTS);
        when(horse.getDomestication()).thenReturn(21);
        when(horse.getMaxDomestication()).thenReturn(42);
        when(horse.getJumpStrength()).thenReturn(3.14159);
        when(horse.getCustomName()).thenReturn("Some wild horse");
        when(horse.isCustomNameVisible()).thenReturn(true);

        return horse;
    }

    private void verifyEqualsGivenHorse(Horse horse) {
        verify(horse).setColor(eq(Horse.Color.BROWN));
        verify(horse).setVariant(eq(Horse.Variant.DONKEY));
        verify(horse).setStyle(eq(Horse.Style.BLACK_DOTS));
        verify(horse).setDomestication(eq(21));
        verify(horse).setMaxDomestication(eq(42));
        verify(horse).setJumpStrength(eq(3.14159));
        verify(horse).setCustomName(eq("Some wild horse"));
        verify(horse).setCustomNameVisible(eq(true));
    }
}
