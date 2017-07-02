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
    int textId();

    // needed for neutral buttons
    int buttonId() default -1;
}
