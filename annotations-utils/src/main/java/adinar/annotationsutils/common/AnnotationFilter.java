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
    private Class clazz;

    private Entry<Class> classAnns;
    private List<Entry<Field>> fieldAnns;
    private List<Entry<Method>> methAnns;

    private List<Class<? extends Annotation>> allowedAnnotations;

    public AnnotationFilter(Class clazz) {
        this.clazz = clazz;
        fieldAnns = new ArrayList<>();
        methAnns = new ArrayList<>();
    }

    public AnnotationFilter addAnnotation(Class<? extends Annotation> ann) {
        allowedAnnotations.add(ann);

        return this;
    }

    public void filter() {
        for (Field f : clazz.getDeclaredFields()) {
            fieldAnns.add(extractAnnsFrom(f));
        }

        for (Method m : clazz.getDeclaredMethods()) {
            methAnns.add(extractAnnsFrom(m));
        }

        classAnns = extractAnnsFrom(clazz);
    }

    private<T extends AnnotatedElement> Entry<T> extractAnnsFrom(T obj) {
        Entry<T> entry = new Entry<>(obj);
        for (Annotation a : obj.getDeclaredAnnotations()) {
            if (allowedAnnotations.contains(a.getClass())) {
                entry.addAnn(a);
            }
        }

        return entry;
    }

    public static class Entry<T extends AnnotatedElement> {
        /** Field, Method or Class. */
        private T obj;
        private Map<Class<? extends Annotation>, Annotation> anns;

        public Entry(T obj) {
            this.obj = obj;
            anns = new HashMap<>();
        }

        public void addAnn(Annotation ann) {
            anns.put(ann.getClass(), ann);
        }

        public<S extends Annotation> S getAnn(Class<S> clazz) {
            return clazz.cast(anns.get(clazz));
        }

        /** Value is extracted from method or field. Accessibility is changed to allow private
          * members read, that's a bit hacky, if you don't like it just don't use it with private
          * members.
          * @param itemInstance: Object to get value from. */
        public Object getValue(Object itemInstance) {
            AccessibleObject accObj = (AccessibleObject) obj;
            boolean access = accObj.isAccessible();
            accObj.setAccessible(true);
            Object result = null;
            if (accObj.getClass() == Method.class) {
                try {
                    result = ((Method)accObj).invoke(itemInstance);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Should never happen!
                }
            }

            if (accObj.getClass() == Field.class) {
                try {
                    result = ((Field)accObj).get(itemInstance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Should never happen!
                }
            }
            accObj.setAccessible(access);

            return result;
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

    public List<Entry> getAllAnnotated() {
        List<Entry> all = new ArrayList<>();
        all.addAll(fieldAnns);
        all.addAll(methAnns);

        return all;
    }
}
