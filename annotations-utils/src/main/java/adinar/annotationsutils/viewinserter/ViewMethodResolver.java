package adinar.annotationsutils.viewinserter;


import android.util.LruCache;

import java.lang.reflect.Method;

public class ViewMethodResolver {
    private static final ViewMethodResolver ourInstance = new ViewMethodResolver();
    private static final int MAX_LRU_CACHE = 1000000;
    private static final String TAG = "ViewMethodResolver";

    public static ViewMethodResolver getInstance() {
        return ourInstance;
    }

    private ViewMethodResolver() {
    }

    private LruCache<MethodEntry, Method> cache = new LruCache<>(MAX_LRU_CACHE);

    private class MethodEntry {
        String methodName;
        Class argumentClass;
        Class methodClass;

        public MethodEntry(String methodName, Class argumentClass, Class methodClass) {
            this.methodName = methodName;
            this.argumentClass = argumentClass;
            this.methodClass = methodClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodEntry that = (MethodEntry) o;

            if (!methodName.equals(that.methodName)) return false;
            if (!argumentClass.equals(that.argumentClass)) return false;
            return methodClass.equals(that.methodClass);
        }

        @Override
        public int hashCode() {
            int result = methodName.hashCode();
            result = 31 * result + argumentClass.hashCode();
            result = 31 * result + methodClass.hashCode();
            return result;
        }
    }

    private class MethodRecursiveSearch {
        private String methodName;
        private Class argumentClass;
        private Class orgMethodClass;

        public MethodRecursiveSearch(String methodName, Class argumentClass, Class methodClass) {
            this.methodName = methodName;
            this.argumentClass = argumentClass;
            this.orgMethodClass = methodClass;
        }

        public Method getMethod(Class methodClass) {
            if (methodClass == null) {
                throw new NoSuchMethodExceptionRuntime(
                        String.format("Method %s(%s) was not found in " +
                                "%s and it's superclasses. " +
                                "Ensure method argument type is exactly matching! %s",
                        methodName,
                        String.valueOf(argumentClass),
                        String.valueOf(orgMethodClass),
                        argumentClass == CharSequence.class ?
                                "CharSequence is default type, you need to change it." : ""));
            }

            MethodEntry entry = new MethodEntry(methodName, argumentClass, methodClass);

            if (cache.get(entry) != null) {
                return cache.get(entry);
            }

            Method meth;
            try {
                meth = methodClass.getDeclaredMethod(methodName, argumentClass);
            } catch (NoSuchMethodException e) {
                meth = getMethod(methodClass.getSuperclass());
            }

            cache.put(entry, meth);
            return meth;
        }
    }
    public Method getMethodFor(String methodName, Class argumentClass, Class methodClass) {
        MethodRecursiveSearch mrs = new MethodRecursiveSearch(methodName, argumentClass, methodClass);
        return mrs.getMethod(methodClass);
    }

    /** Exception thrown when no method with given signature was found. May be caused by
     *  misspell or wrong argument Class (ex. if CharSequence is expected String cannot
     *  be directly passed.) */
    public static class NoSuchMethodExceptionRuntime extends RuntimeException {
        public NoSuchMethodExceptionRuntime(String format) {
            super(format);
        }
    }
}
