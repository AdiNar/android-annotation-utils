package adinar.annotationsutils.objectdialog;


import java.util.HashMap;
import java.util.Map;

class PrimitiveToObjectConverter {
    private static final Map<Class, Class> primitiveToObject;

    static{
        primitiveToObject = new HashMap<>();
        primitiveToObject.put(boolean.class, Boolean.class);
        primitiveToObject.put(int.class, Integer.class);
        primitiveToObject.put(double.class, Double.class);
        primitiveToObject.put(float.class, Float.class);
        primitiveToObject.put(char.class, Character.class);
        primitiveToObject.put(short.class, Short.class);
    }

    public static Class getObjectClass(Class<?> type) {
        Class result = primitiveToObject.get(type);
        return result == null ? type : result;
    }
}
