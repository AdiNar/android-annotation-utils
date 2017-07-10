package adinar.annotationsutils.common;


import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DefaultAnnotationFilter extends
        AnnotationFilter<FieldEntry, MethodEntry, ClassEntry> {
    public static final String TAG = "DefaultAnnotationFilter";

    public DefaultAnnotationFilter(Class clazz) {
        super(clazz);
    }

    @NonNull
    @Override
    protected ClassEntry getClassEntryInstance(Class clazz) {
        return new ClassEntry(clazz);
    }

    @NonNull
    @Override
    protected MethodEntry getMethodEntryInstance(Method m) {
        return new MethodEntry(m);
    }

    @NonNull
    @Override
    protected FieldEntry getFieldEntryInstance(Field f) {
        return new FieldEntry(f);
    }

}
