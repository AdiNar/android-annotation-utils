package adinar.annotationsutils.common;


import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;

public abstract class AnnotationFilterEntryWithValue<T extends AnnotatedElement> extends
        AnnotationFilterEntry<T> {

    public AnnotationFilterEntryWithValue(T obj) {
        super(obj);
    }

    /** Value is extracted from method or field.
     * @param itemInstance: Object to get value from. */
    public abstract Object getValue(Object itemInstance) throws
            InvocationTargetException, IllegalAccessException;

    public abstract Class getReturnType();
}
