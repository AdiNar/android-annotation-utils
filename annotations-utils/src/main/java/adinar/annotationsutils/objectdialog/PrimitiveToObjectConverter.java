package adinar.annotationsutils.objectdialog;


import java.util.HashMap;
import java.util.Map;

public class PrimitiveToObjectConverter {
    private static final Map<Class, Class> primitiveToObject;
    private static final Map<Class, Class> objectToPrimitive;

    static{
        primitiveToObject = new HashMap<>();
        primitiveToObject.put(boolean.class, Boolean.class);
        primitiveToObject.put(int.class, Integer.class);
        primitiveToObject.put(double.class, Double.class);
        primitiveToObject.put(float.class, Float.class);
        primitiveToObject.put(char.class, Character.class);
        primitiveToObject.put(short.class, Short.class);

        objectToPrimitive = new HashMap<>();
        objectToPrimitive.put(Boolean.class, boolean.class);
        objectToPrimitive.put(Integer.class, int.class);
        objectToPrimitive.put(Double.class, double.class);
        objectToPrimitive.put(Float.class, float.class);
        objectToPrimitive.put(Character.class, char.class);
        objectToPrimitive.put(Short.class, short.class);
    }

    public static Class getObjectClass(Class<?> type) {
        Class result = primitiveToObject.get(type);
        return result == null ? type : result;
    }

    public static Class getPrimitiveClass(Class<?> type) {
        Class result = objectToPrimitive.get(type);
        return result == null ? type : result;
    }

    /** If class is primitive or has primitive version, return the second one. */
    public static Class getOtherClass(Class<?> type) {
        Class objectClass = getObjectClass(type);
        if (objectClass == type) {
            return getPrimitiveClass(type);
        } else {
            return objectClass;
        }
    }
}
