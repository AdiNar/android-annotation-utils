package adinar.annotationsutils.common;


import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AnnotationFilter<
        FE extends FieldEntry,
        ME extends MethodEntry,
        CE extends ClassEntry> {
    private static final String TAG = "AnnotationFilter";
    private Class clazz;

    private CE classAnns;
    private List<FE> fieldAnns;
    private List<ME> methAnns;

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
            FE entry = getFieldEntryInstance(f);
            extractAnns(f, entry);
            if (!entry.isEmpty()) {
                fieldAnns.add(entry);
            }
        }

        for (Method m : clazz.getDeclaredMethods()) {
            ME entry = getMethodEntryInstance(m);
            extractAnns(m, entry);
            if (!entry.isEmpty()) {
                methAnns.add(entry);
            }
        }

        CE entry = getClassEntryInstance(clazz);
        extractAnns(clazz, entry);

        return this;
    }

    @NonNull
    protected abstract CE getClassEntryInstance(Class c);

    @NonNull
    protected abstract ME getMethodEntryInstance(Method m);

    @NonNull
    protected abstract FE getFieldEntryInstance(Field f);

    private<T extends AnnotatedElement> void extractAnns(T obj, AnnotationFilterEntry entry) {
        for (Annotation a : obj.getAnnotations()) {
            if (allowedAnnotations.contains(a.annotationType())) {
                entry.addAnn(a);
            }
        }
    }

    public List<FE> getFields() {
        return fieldAnns;
    }

    public List<ME> getMethods() {
        return methAnns;
    }

    public CE getClassAnns() {
        return classAnns;
    }

    public List<AnnotationFilterEntry<? extends AccessibleObject>> getAllAnnotated() {
        List<AnnotationFilterEntry<? extends AccessibleObject>> all = new ArrayList<>();
        all.addAll(fieldAnns);
        all.addAll(methAnns);

        return all;
    }
}
