package adinar.annotationsutils.viewinserter;


import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import adinar.annotationsutils.common.AnnotationFilter;
import adinar.annotationsutils.common.ClassEntry;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;
import adinar.annotationsutils.viewinserter.annotations.InsertToClass;

public class InsertToAnnotationFilter extends
        AnnotationFilter<InsertToFieldEntry, InsertToMethodEntry> {
    public static final String TAG = "InsertToAnnotationFltr";

    public InsertToAnnotationFilter(Class clazz) {
        super(clazz);
        Log.d(TAG, "InsertToFilter init");
        addAnnotation(InsertTo.class);
        addSuperClasses(clazz);
    }

    /** Adds superclasses that are allowed by {@link InsertToClass#withSuper()}. Breaks on first
     *  disallowing in superclasses chain. */
    private void addSuperClasses(Class clazz) {
        Log.d(TAG, "add super class " + clazz + "?");
        Class superClass = clazz.getSuperclass();
        if (superClass != null) { // otherwise it's Object.
            InsertToClass ann = (InsertToClass) clazz.getAnnotation(InsertToClass.class);
            if (shouldSuperClassBeCalled(ann)) {
                addSuperClasses(superClass); // recursively add next superclasse.
                Log.d(TAG, "Oh yeah, adding!");
                addSuperclassToInclude(superClass);
            }
        }
    }

    /** When @ann is null return value depends on
     * {@link InsertToClass#withSuper()}, so it should be checked then by reflection. */
    private boolean shouldSuperClassBeCalled(InsertToClass ann) {
        try {
            boolean defaultValue = (boolean) InsertToClass.class
                    .getMethod("withSuper").getDefaultValue();

            if (defaultValue) return ann == null || ann.withSuper();
            return ann != null && ann.withSuper();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e); // There should always be such a method.
        }
    }

    @NonNull
    @Override
    protected ClassEntry getClassEntryInstance(Class c) {
        return new ClassEntry(c);
    }

    @NonNull
    @Override
    protected InsertToMethodEntry getMethodEntryInstance(Method m) {
        return new InsertToMethodEntry(m);
    }

    @NonNull
    @Override
    protected InsertToFieldEntry getFieldEntryInstance(Field f) {
        return new InsertToFieldEntry(f);
    }
}
