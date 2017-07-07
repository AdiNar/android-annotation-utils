package adinar.annotationsutils.viewinserter.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotations that allows ViewInserterProcessor to make view holder and insert values into view. */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InsertTo {
    // id to look for, findViewById(id()).
    int id();
    // method to call on view from above call - findViewById(id()).<method()>
    String method() default "setText";
    // class which contains method(), by default processor will look for it
    // in spotted view and its superclasses.
    Class methodClass() default void.class;
    // class of argument taken by method(). Important because processor needs exact argument class,
    // not it subclass or parent.
    Class argumentClass() default void.class;
    // set argumentClass() to CharSequence.class, and converts field / method by toString() method
    // for String.class argument should provide argumentClass()
    boolean asString() default false;


    // {@link ViewInserterProcessor} allows to save data from view to object, @saveWith specifies
    // method used on view to retrieve data, if empty getText() will be used.
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface AllowSave {
       boolean allowed() default true;
       String methodName() default "";
       Class methodArgument() default void.class;
    }

    AllowSave save() default @AllowSave(allowed=false);
}
