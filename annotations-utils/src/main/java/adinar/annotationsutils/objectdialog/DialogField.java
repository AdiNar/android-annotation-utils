package adinar.annotationsutils.objectdialog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field that should be displayed in dialog has to be annotated with this.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface DialogField {
    /** Items in dialog will be ordered by this value. Lower = higher priority. */
    double order();

    /** Value to be displayed as field description, literal or given by R.string.* id. */
    String name() default "";
    int nameId() default 0;
}
