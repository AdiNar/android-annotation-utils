package adinar.annotationsutils.common;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class FieldEntry extends AnnotationFilterEntryWithValue<Field> {
    public static final String TAG = "FieldEntry";

    public FieldEntry(Field field) {
        super(field);
    }

    @Override
    public Object getValue(Object itemInstance) throws
            InvocationTargetException, IllegalAccessException {
        return FieldAndMethodAccess.getFieldValue(getObj(), itemInstance);
    }

    public Class getReturnType() {
        return getObj().getType();
    }

    public void setValue(Object dstObject, Object valueFromView) throws IllegalAccessException {
        FieldAndMethodAccess.setFieldValue(getObj(), dstObject, valueFromView);
    }
}
