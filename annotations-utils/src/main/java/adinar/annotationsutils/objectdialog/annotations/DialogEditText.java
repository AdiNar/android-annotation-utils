package adinar.annotationsutils.objectdialog.annotations;

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

    @interface ETValidator {
        Class<? extends TextViewValidator> clazz();
        int errorMsgId() default 0;
    }

    ETValidator[] validators() default {};
}
