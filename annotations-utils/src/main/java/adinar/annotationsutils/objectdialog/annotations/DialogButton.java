package adinar.annotationsutils.objectdialog.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DialogButton {
    enum ButtonType {
        POSITIVE, NEGATIVE, NEUTRAL
    }

    ButtonType type() default ButtonType.POSITIVE;

    // 0 will be replaced with "OK" for POSITIVE button text because R.string.* cannot
    // be provided here.
    int textId() default 0;

    // needed for neutral buttons
    int buttonId() default -1;
}
