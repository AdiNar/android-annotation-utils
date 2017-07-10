package adinar.annotationsutils.common;


import java.lang.reflect.InvocationTargetException;

public class ClassEntry extends AnnotationFilterEntry<Class> {
    public static final String TAG = "ClassEntry";

    public ClassEntry(Class clazz) {
        super(clazz);
    }

    @Override
    public Object getValue(Object itemInstance) throws
            InvocationTargetException, IllegalAccessException {
        throw new RuntimeException("getValue not defined for ClassEntry!");
    }

    @Override
    public Class getReturnType() {
        throw new RuntimeException("getReturnType not defined for ClassEntry!");
    }

    @Override
    public void setValue(Object dstObject, Object valueFromView) throws IllegalAccessException {
        throw new RuntimeException("setValue not defined for ClassEntry!");
    }
}
