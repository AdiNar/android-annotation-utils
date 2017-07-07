package adinar.annotationsutils.common;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Set of helper methods to set and extract  {@link Field}s and {@link Method}s values.
 * Accessibility is changed to allow private
 * members read, that's a bit hacky, if you don't like it
 * (mostly in case of simultaneous access) just don't use it with private members.*/
public class FieldAndMethodAccess {

    public static Object getFieldValue(Field field, Object object) throws IllegalAccessException {
        boolean access = field.isAccessible();
        field.setAccessible(true);
        try {
            return field.get(object);
        } finally {
            field.setAccessible(access);
        }
    }

    public static String getStringFieldValue(Field field, Object object) throws IllegalAccessException {
        Object value = getFieldValue(field, object);
        return String.valueOf(value == null ? "" : value);
    }

    public static void setFieldValue(Field field, Object objToChange, Object value)
            throws IllegalAccessException {
        boolean access = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(objToChange, value);
        } finally {
            field.setAccessible(access);
        }
    }

    public static Object getMethodValue(Method method, Object object, Object ...args)
            throws IllegalAccessException, InvocationTargetException {
        boolean access = method.isAccessible();
        method.setAccessible(true);
        try {
            return method.invoke(object, args);
        } finally {
            method.setAccessible(access);
        }
    }

    /** Value is extracted from method or field.
     * @param accessibleObject field or no-arg method of @itemInstance to obtain.
     * @param itemInstance: Object to get value from. */
    public static Object getValue(Object accessibleObject, Object itemInstance)
            throws InvocationTargetException, IllegalAccessException {
        if (accessibleObject.getClass() == Method.class) {
            return getMethodValue((Method)accessibleObject, itemInstance);
        }

        if (accessibleObject.getClass() == Field.class) {
            return getFieldValue((Field) accessibleObject, itemInstance);
        }

        return null;
    }
}
