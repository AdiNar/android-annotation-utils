package adinar.annotationsutils.objectdialog;

import android.content.Context;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import adinar.annotationsutils.objectdialog.validation.Validator;
import adinar.annotationsutils.objectdialog.validation.ValidatorBuilder;

/**  */
abstract class DialogFieldEntry<T> {
    protected final Context ctx;
    private final Field field;
    private final String fieldName;
    protected double order;

    /** In order to put value in dialog it has to be stringified, if there's no valueOf method
     *  in class, it can be provided here. */
    private Method customValueOf;
    private View viewInDialog;
    protected Validator validator;

    /** Object to get data from (and save to). */
    private T object;

    private static Map<Class, Method> valueOfCache;

    static {
        valueOfCache = new HashMap<>();
    }

    /**  @param f: Field to manage. */
    public DialogFieldEntry(Field f, Context ctx) {
        field = f;
        fieldName = field.getName();
        this.ctx = ctx.getApplicationContext();

        applyAnnotation(f.getAnnotation(getAnnotationClass()));
    }

    public abstract Class<? extends Annotation> getAnnotationClass();

    protected abstract void applyAnnotation(Annotation ann);

    /**  @param valueOf: Value from Dialog View must be converted to value kept by Field.
     *                   Usually method Class.valueOf(String) is called, to override this
     *                   behaviour pass custom method here. */
    public DialogFieldEntry(Field f, Method valueOf, Context ctx) {
        this(f, ctx);
        customValueOf = valueOf;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getFieldValue() {
        try {
            boolean access = field.isAccessible();
            field.setAccessible(true);
            String res = String.valueOf(field.get(object));
            field.setAccessible(access);
            return res;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** Used to save data from dialog to object. */
    public void setFieldValue(String value) {
        Method meth = customValueOf != null ? customValueOf : valueOfCache.get(object.getClass());
        if (meth == null && field.getType() != String.class) {
            try {
                meth = field.getType().getMethod("valueOf", String.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(String.format("Class %s should implement static " +
                        "method valueOf(String)", field.getType()));
            }
            valueOfCache.put(field.getType(), meth);
        }

        try {
            Object convertedValue = field.getType() == String.class ?
                    value :
                    meth.invoke(null, value);
            boolean access = field.isAccessible(); // TODO replace all occurences of this with sth nice
            field.setAccessible(true);
            field.set(object, convertedValue);
            field.setAccessible(access);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getFieldName() {
        return fieldName;
    }
    public double getOrder() {
        return order;
    }
    public View getView() {
        return viewInDialog;
    }

    public final View generateView(T object, Context ctx, ValidatorBuilder builder) {
        setObject(object);
        DialogElementViewCreator creator = getDialogElementViewCreator(ctx);
        viewInDialog = creator.create();

        createValidator();

        if (validator != null) builder.add(validator);

        return viewInDialog;
    }

    protected abstract void createValidator();
    public abstract DialogElementViewCreator getDialogElementViewCreator(Context ctx);
    public abstract void setFieldValueFromView();
}
