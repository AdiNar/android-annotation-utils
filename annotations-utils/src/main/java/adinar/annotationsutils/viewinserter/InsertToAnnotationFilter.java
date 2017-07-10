package adinar.annotationsutils.viewinserter;


import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import adinar.annotationsutils.common.AnnotationFilter;
import adinar.annotationsutils.common.ClassEntry;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

public class InsertToAnnotationFilter extends
        AnnotationFilter<InsertToFieldEntry, InsertToMethodEntry, ClassEntry> {
    public static final String TAG = "InsertToAnnotationFilter";

    public InsertToAnnotationFilter(Class clazz) {
        super(clazz);
        addAnnotation(InsertTo.class);
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
