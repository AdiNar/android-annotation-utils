package adinar.annotationsutils.viewinserter;


import android.support.v4.util.LruCache;

import java.lang.reflect.Method;

import adinar.annotationsutils.objectdialog.PrimitiveToObjectConverter;

public class MethodResolver {
    private static final int MAX_LRU_CACHE = 1000000;
    private static final String TAG = "MethodResolver";

    private MethodResolver() {
    }

    private static LruCache<MethodEntry, Method> cache = new LruCache<>(MAX_LRU_CACHE);

    private static class MethodEntry {
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

    private static class MethodRecursiveSearch {
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
                if (argumentClass == void.class) {
                    meth = methodClass.getDeclaredMethod(methodName);
                } else {
                    meth = methodClass.getDeclaredMethod(methodName, argumentClass);
                }
            } catch (NoSuchMethodException e) {
                meth = getMethod(methodClass.getSuperclass());
            }

            cache.put(entry, meth);
            return meth;
        }
    }
    public static Method getMethodFor(String methodName, Class argumentClass, Class methodClass) {
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

    public static Method getMethodAndCheckHeuristics(String methodName,
                                                     Class argumentClass, Class methodClass) {
        Method meth;
        // @meth is a view method like EditText.getText(), value of item field / method
        // will be applied to it.
        try {
            meth = getMethodFor(methodName, argumentClass, methodClass);
        } catch (MethodResolver.NoSuchMethodExceptionRuntime e1) {
            // Heuristic, many Android Views methods that take text
            // takes CharSequence as argument.
            if (argumentClass == String.class) {
                meth = getMethodFor(methodName, CharSequence.class, methodClass);
            } else {
                // Another heuristic, because Integer.class != int.class we should check it...
                Class objectClass = PrimitiveToObjectConverter.getObjectClass(argumentClass);
                if (objectClass != argumentClass) {
                    meth = getMethodFor(methodName, objectClass, methodClass);
                } else {
                    throw e1;
                }
            }
        }
        return meth;
    }
}
