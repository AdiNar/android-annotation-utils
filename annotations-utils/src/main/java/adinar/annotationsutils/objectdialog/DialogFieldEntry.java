package adinar.annotationsutils.objectdialog;

import android.content.Context;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import adinar.annotationsutils.objectdialog.annotations.DialogField;

/**  */
class DialogFieldEntry<T> {
    private Field field;
    private String getFieldName;
    private double order;
    private Method customValueOf;
    private View viewInDialog;
    private T object;

    private static Map<Class, Method> valueOfCache;

    static {
        valueOfCache = new HashMap<>();
    }

    /**  @param f: Field to manage. */
    public DialogFieldEntry(Field f) {
        field = f;
        getFieldName = field.getName();

        applyAnnotation(f.getAnnotation(DialogField.class));
    }

    private void applyAnnotation(DialogField ann) {
        this.order = ann.order();
    }

    /**  @param valueOf: Value from Dialog View must be converted to value kept by Field.
     *                   Usually method Class.valueOf(String) is called, to override this
     *                   behaviour pass custom method here. */
    public DialogFieldEntry(Field f, Method valueOf) {
        this(f);
        customValueOf = valueOf;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getFieldValue() {
        try {
            return String.valueOf(field.get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setFieldValue(String value) {
        Method meth = customValueOf != null ? customValueOf : valueOfCache.get(object.getClass());
        if (meth == null) {
            try {
                meth = object.getClass().getMethod("valueOf", String.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(String.format("Class %s should implement static " +
                        "method valueOf(String)", object.getClass()));
            }
            valueOfCache.put(object.getClass(), meth);
        }

        try {
            Object convertedValue = meth.invoke(null, value);
            field.set(object, convertedValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getGetFieldName() {
        return getFieldName;
    }

    public double getOrder() {
        return order;
    }

    public View generateView(T object, Context ctx) {
        setObject(object);
        // TODO other types than simple EditText should be added here.
        DialogElementViewCreator creator = new EditTextViewCreator(this, ctx);
        viewInDialog = creator.create();

        return viewInDialog;
    }
}
