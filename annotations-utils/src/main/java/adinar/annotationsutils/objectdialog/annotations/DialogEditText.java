package adinar.annotationsutils.objectdialog.annotations;

import android.text.InputType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import adinar.annotationsutils.objectdialog.validation.TextViewValidator;

/**
 * Field that should be displayed in dialog has to be annotated with this.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DialogEditText {
    /** Items in dialog will be ordered by this value. Lower = higher priority. */
    double order();

    int labelId() default 0;
    int hintId() default 0;

    /** One of {@link InputType}. */
    int inputType() default InputType.TYPE_CLASS_TEXT;

    /** EditText validator interface, will be applied to view on save action or
     *  on every char tapped by user when {@link #isInteractive} is on. */
    @interface ETValidator {
        Class<? extends TextViewValidator> clazz();
        int errorMsgId() default 0;
        boolean isInteractive() default false;
    }

    ETValidator[] validators() default {};
}
