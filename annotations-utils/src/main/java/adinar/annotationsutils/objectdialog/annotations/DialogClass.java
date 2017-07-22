package adinar.annotationsutils.objectdialog.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** In order to create dialog from class it has to define this annotation.
  * Title should be specified here or by DialogTitle annotation inside class. */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DialogClass {
    String title() default "";
    int titleId() default 0;

    /** Buttons that will be applied to dialog, first button on list will be the most left. */
    DialogButton[] buttons() default {};
}
