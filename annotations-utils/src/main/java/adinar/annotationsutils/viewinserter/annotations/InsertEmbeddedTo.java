package adinar.annotationsutils.viewinserter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotation to handle nested classes insertion into view.  */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InsertEmbeddedTo {
    /** If id() > 0 field will be inserted to findViewById(id()), otherwise plain in view.  */
    int id() default 0;
}
