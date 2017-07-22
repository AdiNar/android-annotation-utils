package adinar.annotationsutils.common;


import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;


public class AnnotationFilterEntry<T extends AnnotatedElement> {

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

    public T getObj() {
        return obj;
    }

    public boolean isEmpty() {
        return anns.isEmpty();
    }
}