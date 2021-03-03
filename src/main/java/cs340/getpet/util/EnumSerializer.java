package cs340.getpet.util;

import com.google.gson.annotations.SerializedName;
import cs340.getpet.persistence.Animal;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

// partially copied code from GSON to get value of @SerializedName annotation
public final class EnumSerializer {
    private static final List<Class<? extends Enum<?>>> CLASSES = List.of(Animal.Species.class, Animal.Gender.class, Animal.Color.class, Animal.Size.class);
    private static final Map<Object, Map<Object, String>> CLASS_TO_CONSTANT_TO_NAME = new IdentityHashMap<>();
    private static final Map<Object, Map<String, Object>> CLASS_TO_NAME_TO_CONSTANT = new IdentityHashMap<>();

    static {
        for (Class<? extends Enum<?>> clazz : CLASSES)
            try {
                Map<Object, String> constantToName = new IdentityHashMap<>();
                Map<String, Object> nameToConstant = new HashMap<>();

                for (Enum<?> constant : clazz.getEnumConstants()) {
                    String name = constant.name();
                    SerializedName annotation = clazz.getField(name).getAnnotation(SerializedName.class);
                    if (annotation != null) {
                        name = annotation.value();
                    }
                    constantToName.put(constant, name);
                    nameToConstant.put(name, constant);
                }

                CLASS_TO_CONSTANT_TO_NAME.put(clazz, constantToName);
                CLASS_TO_NAME_TO_CONSTANT.put(clazz, nameToConstant);
            } catch (NoSuchFieldException e) {
                throw new AssertionError(e);
            }
    }

    public static <T extends Enum<T>> String toString(T t, Class<T> clazz) {
        return CLASS_TO_CONSTANT_TO_NAME.get(clazz).get(t);
    }

    public static <T extends Enum<T>> T fromString(String s, Class<T> clazz) {
        return (T) CLASS_TO_NAME_TO_CONSTANT.get(clazz).get(s);
    }
}