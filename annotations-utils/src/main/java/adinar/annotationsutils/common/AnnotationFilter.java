package adinar.annotationsutils.common;


import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AnnotationFilter<
        FE extends FieldEntry,
        ME extends MethodEntry> {
    private static final String TAG = "AnnotationFilter";
    private Class clazz;

    /* Although only one class annotation can be present,
       superclasses also can contain such annotation. */
    private List<ClassEntry> classAnns;

    private List<FE> fieldAnns;
    private List<ME> methAnns;

    private List<Class<? extends Annotation>> allowedAnnotations;
    private Set<Class> superclassesToInclude;

    public AnnotationFilter(Class clazz) {
        this.clazz = clazz;
        fieldAnns = new ArrayList<>();
        methAnns = new ArrayList<>();
        classAnns = new ArrayList<>();
        allowedAnnotations = new ArrayList<>();
        superclassesToInclude = new HashSet<>();
    }
    
    public void addSuperclassToInclude(Class toInclude) {
        Log.d(TAG, "toInclude " + toInclude);
        this.superclassesToInclude.add(toInclude);
    }

    public AnnotationFilter addAnnotation(Class<? extends Annotation> ann) {
        allowedAnnotations.add(ann);

        return this;
    }

    public AnnotationFilter filter() {
        return innerFilter(clazz);
    }
    
    public AnnotationFilter innerFilter(Class filterClass) {

        Log.d(TAG, "innerFilter: " + filterClass);

        applyFilteringFromSuperClassIfNeeded(filterClass);

        for (Field f : filterClass.getDeclaredFields()) {
            FE entry = getFieldEntryInstance(f);
            extractAnns(f, entry);
            if (!entry.isEmpty()) {
                fieldAnns.add(entry);
            }
        }

        for (Method m : filterClass.getDeclaredMethods()) {
            ME entry = getMethodEntryInstance(m);
            extractAnns(m, entry);
            if (!entry.isEmpty()) {
                methAnns.add(entry);
            }
        }

        ClassEntry entry = getClassEntryInstance(filterClass);
        classAnns.add(entry);
        extractAnns(filterClass, entry);

        return this;
    }

    private void applyFilteringFromSuperClassIfNeeded(Class filterClass) {
        if (superclassesToInclude.contains(filterClass.getSuperclass())) {
            innerFilter(filterClass.getSuperclass());
        }
    }

    @NonNull
    protected abstract ClassEntry getClassEntryInstance(Class c);

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

    public List<ClassEntry> getClassAnns() {
        return classAnns;
    }

    public List<? extends AnnotationFilterEntryWithValue> getAllAnnotated() {
        List<AnnotationFilterEntryWithValue> all = new ArrayList<>();
        all.addAll(fieldAnns);
        all.addAll(methAnns);

        return all;
    }
}
