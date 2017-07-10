package adinar.annotationsutils.common;


import java.lang.reflect.Field;

public class FieldEntry extends AnnotationFilterEntry<Field> {
    public static final String TAG = "FieldEntry";

    public FieldEntry(Field field) {
        super(field);
    }

    public Class getReturnType() {
        return getObj().getType();
    }

    @Override
    public void setValue(Object dstObject, Object valueFromView) throws IllegalAccessException {
        FieldAndMethodAccess.setFieldValue(getObj(), dstObject, valueFromView);
    }
}
