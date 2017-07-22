package adinar.annotationsutils.common;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodEntry extends AnnotationFilterEntryWithValue<Method> {
    public static final String TAG = "MethodEntry";

    public MethodEntry(Method method) {
        super(method);
    }

    @Override
    public Object getValue(Object itemInstance) throws
            InvocationTargetException, IllegalAccessException {
        return FieldAndMethodAccess.getMethodValue(getObj(), itemInstance);
    }

    @Override
    public Class getReturnType() {
        return getObj().getReturnType();
    }
}
