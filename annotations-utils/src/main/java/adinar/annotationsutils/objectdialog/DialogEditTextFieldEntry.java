package adinar.annotationsutils.objectdialog;


import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.EditText;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import adinar.annotationsutils.R;
import adinar.annotationsutils.Utils;
import adinar.annotationsutils.objectdialog.annotations.DialogEditText;
import adinar.annotationsutils.objectdialog.validation.TextViewValidator;
import adinar.annotationsutils.objectdialog.validation.ValidatorBuilder;

class DialogEditTextFieldEntry<T> extends DialogFieldEntry<T> {
    private String hint, label;
    private DialogEditText.ETValidator[] rawValidators;
    private EditTextViewCreator editTextViewCreator;

    public DialogEditTextFieldEntry(Field f, Context ctx) {
        super(f, ctx);
    }

    @Override
    public Class<DialogEditText> getAnnotationClass() {
        return DialogEditText.class;
    }

    public DialogEditTextFieldEntry(Field f, Method m, Context ctx) {
        super(f, m, ctx);
    }

    @Override
    protected void createValidator() {
        EditText et = (EditText) getView().findViewById(R.id.value);
        ValidatorBuilder builder = new ValidatorBuilder();

        for (DialogEditText.ETValidator v : rawValidators) {
            try {
                TextViewValidator val = getTextViewValidator(et, v);
                builder.add(val);
            } catch (InstantiationException e) {
                throw new NoProperConstructorException(v.clazz());
            } catch (IllegalAccessException e) {
                throw new NoProperConstructorException(v.clazz());
            }
        }

        validator = builder.build();
    }

    /** Creates new instance of {@link TextViewValidator} and set it's base values. */
    @NonNull
    private TextViewValidator getTextViewValidator(EditText et, DialogEditText.ETValidator v)
            throws InstantiationException, IllegalAccessException {
        TextViewValidator val = v.clazz().newInstance();
        val.setView(et);
        val.setErrorMessageString(Utils.getString(ctx, v.errorMsgId()));
        return val;
    }

    protected void applyAnnotation(Annotation annotation) {
        DialogEditText ann = (DialogEditText) annotation;
        this.order = ann.order();
        this.rawValidators = ann.validators();

        hint = getString(ann.hintId());
        label = getString(ann.labelId());
    }

    private String getString(int id) {
        if (id == 0) return String.valueOf("");
        return ctx.getString(id);
    }

    @Override
    public DialogElementViewCreator getDialogElementViewCreator(Context ctx) {
        return editTextViewCreator = new EditTextViewCreator(this, ctx);
    }

    @Override
    public void setFieldValueFromView() {
        setFieldValue(editTextViewCreator.getValueView().getText().toString());
    }

    public String getHint() {
        return hint;
    }

    public String getLabel() {
        return label;
    }

    private static class NoProperConstructorException extends RuntimeException {
        public NoProperConstructorException(Class<? extends TextViewValidator> clazz) {
            super(String.format("%s should have public no-arg constructor!", clazz.getSimpleName()));
        }
    }
}
