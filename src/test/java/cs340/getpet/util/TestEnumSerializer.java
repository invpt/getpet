package cs340.getpet.util;

import java.util.IdentityHashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cs340.getpet.persistence.Animal.Color;
import cs340.getpet.persistence.Animal.Gender;
import cs340.getpet.persistence.Animal.Size;
import cs340.getpet.persistence.Animal.Species;

// There's not much to test here, so I just added tests for every single possibility.
public class TestEnumSerializer {
    @Test
    public void testSpeciesSerialization() {
        testEnumSerialization(Map.of(Species.CAT, "cat", Species.DOG, "dog"), Species.class);
    }

    @Test
    public void testGenderSerialization() {
        testEnumSerialization(Map.of(Gender.MALE, "m", Gender.FEMALE, "f"), Gender.class);
    }

    @Test
    public void testColorSerialization() {
        testEnumSerialization(Map.of(Color.BLACK, "black", Color.WHITE, "white", Color.BROWN, "brown", Color.DARK_GRAY, "dGray", Color.LIGHT_GRAY, "lGray"), Color.class);
    }

    @Test
    public void testSizeSerialization() {
        testEnumSerialization(Map.of(Size.SMALL, "small", Size.MEDIUM, "medium", Size.LARGE, "large"), Size.class);
    }

    @Test
    public void testSpeciesDeserialization() {
        testEnumDeserialization(Map.of("cat", Species.CAT, "dog", Species.DOG), Species.class);
    }

    @Test
    public void testGenderDeserialization() {
        testEnumDeserialization(Map.of("m", Gender.MALE, "f", Gender.FEMALE), Gender.class);
    }

    @Test
    public void testColorDeserialization() {
        testEnumDeserialization(Map.of("black", Color.BLACK, "white", Color.WHITE, "brown", Color.BROWN, "dGray", Color.DARK_GRAY, "lGray", Color.LIGHT_GRAY), Color.class);
    }

    @Test
    public void testSizeDeserialization() {
        testEnumDeserialization(Map.of("small", Size.SMALL, "medium", Size.MEDIUM, "large", Size.LARGE), Size.class);
    }

    private <T extends Enum<T>> void testEnumSerialization(Map<T, String> expected, Class<T> clazz) {
        expected = new IdentityHashMap<>(expected);

        for (T t : expected.keySet()) {
            String serialized = EnumSerializer.toString(t, clazz);
            Assertions.assertEquals(serialized, expected.get(t));
        }
    }

    private <T extends Enum<T>> void testEnumDeserialization(Map<String, T> expected, Class<T> clazz) {
        for (String s : expected.keySet()) {
            T deserialized = EnumSerializer.fromString(s, clazz);
            Assertions.assertEquals(deserialized, expected.get(s));
        }
    }
}
