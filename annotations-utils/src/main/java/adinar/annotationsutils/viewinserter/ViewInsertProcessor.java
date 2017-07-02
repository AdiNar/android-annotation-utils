package adinar.annotationsutils.viewinserter;


import android.view.View;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import adinar.annotationsutils.common.AnnotationFilter;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

public class ViewInsertProcessor {
    private static Map<Class, AnnotationFilter> cache;

    static {
        cache = new HashMap<>();
    }

    public static<T> void insertInto(View view, T object) {
        AnnotationFilter filter = getFilterFor(object.getClass());

        for (AnnotationFilter.Entry<Field> e : filter.getFields()) {
            InsertTo ann = e.getAnn(InsertTo.class);
            view.findViewById(ann.id());
        }
    }

    private static AnnotationFilter getFilterFor(Class<?> clazz) {
        AnnotationFilter f = cache.get(clazz);
        if (f == null) {
            f = new AnnotationFilter(clazz).addAnnotation(InsertTo.class);
            cache.put(clazz, f);
        }

        return f;
    }
}
