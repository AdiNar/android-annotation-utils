package adinar.annotationsutils.viewinserter.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Helper annotation to set some class dependent stuff, not needed to work, just an addition. */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InsertToClass {
    /** Whether to include superClass annotations. */
    boolean withSuper() default true;
}
