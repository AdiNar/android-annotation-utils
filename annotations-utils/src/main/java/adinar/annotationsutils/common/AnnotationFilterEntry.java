package adinar.annotationsutils.common;


import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**  */
// This is a very ugly implementation, it was first designed as a class for AnnotatedElements as
// a whole but then some Field/Method specific methods appeared, this logic should be divided.
public abstract class AnnotationFilterEntry<T extends AnnotatedElement> {
    /** Field, Method or Class. */
    private T obj;
    /** Instances of {@link Annotation}s that @obj fields contains, filtered. */
    private Map<Class<? extends Annotation>, Annotation> anns;

    public AnnotationFilterEntry(T obj) {
        this.obj = obj;
        anns = new HashMap<>();
    }

    public void addAnn(Annotation ann) {
        anns.put(ann.annotationType(), ann);
    }

    public<S extends Annotation> S getAnn(Class<S> clazz) {
        Annotation obj = anns.get(clazz);
        return obj == null ? null : clazz.cast(obj);
    }

    /** Value is extracted from method or field.
     * @param itemInstance: Object to get value from. */
    public abstract Object getValue(Object itemInstance) throws
            InvocationTargetException, IllegalAccessException;

    public T getObj() {
        return obj;
    }

    public boolean isEmpty() {
        return anns.isEmpty();
    }

    public abstract Class getReturnType();

    public abstract void setValue(Object dstObject, Object valueFromView) throws IllegalAccessException;
}