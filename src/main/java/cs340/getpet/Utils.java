package cs340.getpet;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {
    @SafeVarargs
    public static <K, V> Map<K, V> linkedMapOfEntries(Map.Entry<K, V>... entries) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>(entries.length);

        for (Map.Entry<K, V> entry : entries)
            map.put(entry.getKey(), entry.getValue());

        return map;
    }
}
