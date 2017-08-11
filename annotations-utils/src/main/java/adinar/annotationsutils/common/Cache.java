package adinar.annotationsutils.common;


import android.util.LruCache;

public class Cache {
    private static final int DEFAULT_LRU_SIZE = 1000000;

    private static LruCache<Object, Object> cache = new LruCache<>(DEFAULT_LRU_SIZE);

    private Cache() {}

    public static<T, R> R getCached(T key, Supplier<R> supplier) {
        R value = (R) cache.get(key);
        if (value == null) {
            value = supplier.get();
            cache.put(key, value);
        }

        return value;
    }

    public static<T, R> R getCachedOrNull(T key) {
        return (R) cache.get(key);
    }

    public interface Supplier<T> {
        T get();
    }
}
