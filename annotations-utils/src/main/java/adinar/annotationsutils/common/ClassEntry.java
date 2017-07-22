package adinar.annotationsutils.common;


/** Just an alias for {@link AnnotationFilterEntry} to use with Classes. */
public class ClassEntry extends AnnotationFilterEntry<Class> {
    public static final String TAG = "ClassEntry";

    public ClassEntry(Class clazz) {
        super(clazz);
    }
}
