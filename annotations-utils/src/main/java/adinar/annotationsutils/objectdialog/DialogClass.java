package adinar.annotationsutils.objectdialog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** In order to create dialog from class it has to define this annotation.
  * Title should be specified here or by DialogTitle annotation inside class. */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface DialogClass {
    String title() default "";
    int titleId() default 0;
}
