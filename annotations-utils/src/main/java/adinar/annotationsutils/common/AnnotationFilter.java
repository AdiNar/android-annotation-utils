package adinar.annotationsutils.common;


import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationFilter {
    private static final String TAG = "AnnotationFilter";
    private Class clazz;

    private Entry<Class> classAnns;
    private List<Entry<Field>> fieldAnns;
    private List<Entry<Method>> methAnns;

    private List<Class<? extends Annotation>> allowedAnnotations;

    public AnnotationFilter(Class clazz) {
        this.clazz = clazz;
        fieldAnns = new ArrayList<>();
        methAnns = new ArrayList<>();
        allowedAnnotations = new ArrayList<>();
    }

    public AnnotationFilter addAnnotation(Class<? extends Annotation> ann) {
        allowedAnnotations.add(ann);

        return this;
    }

    public AnnotationFilter filter() {
        for (Field f : clazz.getDeclaredFields()) {
            Entry entry = extractAnnsFrom(f);
            if (!entry.isEmpty()) {
                fieldAnns.add(entry);
            }
        }

        for (Method m : clazz.getDeclaredMethods()) {
            Entry entry = extractAnnsFrom(m);
            if (!entry.isEmpty()) {
                methAnns.add(entry);
            }
        }

        classAnns = extractAnnsFrom(clazz);

        return this;
    }

    private<T extends AnnotatedElement> Entry<T> extractAnnsFrom(T obj) {
        Entry<T> entry = new Entry<>(obj);
        for (Annotation a : obj.getAnnotations()) {
            if (allowedAnnotations.contains(a.annotationType())) {
                entry.addAnn(a);
            }
        }

        return entry;
    }

    public static class Entry<T extends AnnotatedElement> {
        /** Field, Method or Class. */
        private T obj;
        /** Instances of {@link Annotation}s that @obj fields contains, filtered. */
        private Map<Class<? extends Annotation>, Annotation> anns;

        public Entry(T obj) {
            this.obj = obj;
            anns = new HashMap<>();
        }

        public void addAnn(Annotation ann) {
            anns.put(ann.annotationType(), ann);
        }

        public<S extends Annotation> S getAnn(Class<S> clazz) {
            return clazz.cast(anns.get(clazz));
        }

        /** Value is extracted from method or field.
          * @param itemInstance: Object to get value from. */
        public Object getValue(Object itemInstance) {
            try {
                return FieldAndMethodAccess.getValue(obj, itemInstance);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return null;
        }

        public T getObj() {
            return obj;
        }

        public boolean isEmpty() {
            return anns.isEmpty();
        }
    }

    public List<Entry<Field>> getFields() {
        return fieldAnns;
    }

    public List<Entry<Method>> getMethods() {
        return methAnns;
    }

    public Entry<Class> getClassAnns() {
        return classAnns;
    }

    public List<Entry<? extends AccessibleObject>> getAllAnnotated() {
        List<Entry<? extends AccessibleObject>> all = new ArrayList<>();
        all.addAll(fieldAnns);
        all.addAll(methAnns);

        return all;
    }
}
