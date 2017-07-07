package adinar.annotationsutils.viewinserter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import adinar.annotationsutils.common.AnnotationFilter;
import adinar.annotationsutils.objectdialog.PrimitiveToObjectConverter;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

public class ViewInserterHolder<T> extends RecyclerView.ViewHolder {
    private static final String TAG = "ViewInserterHolder";
    private SparseArray<View> idToViewMap = new SparseArray<>();
    private static Map<Class, AnnotationFilter> cache;
    private AnnotationFilter filter;

    static {
        cache = new HashMap<>();
    }

    public ViewInserterHolder(View view, AnnotationFilter filter) {
        super(view);
        this.filter = filter;
        matchIdsWithViews(view, filter);
    }

    public ViewInserterHolder(View view, Class<T> clazz) {
        this(view, getAnnotationFilter(clazz));
    }

    private static<T> AnnotationFilter getAnnotationFilter(Class<T> clazz) {
        AnnotationFilter filter = cache.get(clazz);
        if (filter == null) {
            filter = new AnnotationFilter(clazz).addAnnotation(InsertTo.class).filter();
            cache.put(clazz, filter);
        }
        return filter;
    }

    /** Look for all ids used in annotations and cache their views. */
    private void matchIdsWithViews(View view, AnnotationFilter filter) {
        for (AnnotationFilter.Entry e : filter.getAllAnnotated()) {
            InsertTo ann = (InsertTo) e.getAnn(InsertTo.class);
            View annView = view.findViewById(ann.id());
            if (annView != null) idToViewMap.put(ann.id(), annView);
        }
    }

    /** Inserts data with no callback listener. */
    public void insertData(T item) {
        insertData(item, null, 0);
    }

    /** Inserts data from @object to view that this holder corresponds to.
     *  @param listener: Listener for view clicks.
     *  @param itemId: Use it to distinguish items when listener is used to listen
     *                 multiple objects. */
    public void insertData(final T item,
                           final OnInsertedViewClickListener<T> listener,
                           final int itemId) {

        for (AnnotationFilter.Entry<? extends AccessibleObject> e : filter.getAllAnnotated()) {
            InsertTo ann = e.getAnn(InsertTo.class);
            View dst = idToViewMap.get(ann.id());

            if (dst == null) {
                // TODO some more descriptive info should be logged here but actually
                // there's no direct access to names or so.
                Log.i(TAG, String.format("View does not contain id %d.", ann.id()));
                continue;
            }

            Class argumentClass = getArgumentClass(e, ann);
            Class methodClass = getMethodClass(ann);

            Method meth = getMethodAndCheckHeuristics(ann, argumentClass, methodClass);

            Object value = e.getValue(item);

            if (ann.asString()) {
                value = value.toString();
            }

            try {
                meth.invoke(dst, value);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        }

        if (listener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, itemId, v);
                }
            });
        }
    }

    private Method getMethodAndCheckHeuristics(InsertTo ann, Class argumentClass, Class methodClass) {
        Method meth;
        // @meth is a view method like EditText.getText(), value of item field / method
        // will be applied to it.
        try {
            meth = getMethodFor(ann, argumentClass, methodClass);
        } catch (ViewMethodResolver.NoSuchMethodExceptionRuntime e1) {
            // Heuristic, many Android Views methods that take text
            // takes CharSequence as argument.
            if (argumentClass == String.class) {
                meth = getMethodFor(ann, CharSequence.class, methodClass);
            } else {
                // Another heuristic, because Integer.class != int.class we should check it...
                Class objectClass = PrimitiveToObjectConverter.getObjectClass(argumentClass);
                if (objectClass != argumentClass) {
                    meth = getMethodFor(ann, objectClass, methodClass);
                } else {
                    throw e1;
                }
            }
        }
        return meth;
    }

    private Method getMethodFor(InsertTo ann, Class argumentClass, Class methodClass) {
        Method meth = ViewMethodResolver.getInstance().getMethodFor(
                ann.method(),
                argumentClass,
                methodClass
        );
        return meth;
    }

    private Class getMethodClass(InsertTo ann) {
        Class methodClass = ann.methodClass();
        if (methodClass == void.class) methodClass = idToViewMap.get(ann.id()).getClass();
        return methodClass;
    }

    private Class getArgumentClass(AnnotationFilter.Entry<? extends AccessibleObject> e, InsertTo ann) {
        Class argumentClass = ann.argumentClass();
        if (ann.asString()) {
            argumentClass = String.class;
        }
        if (argumentClass == void.class) argumentClass = getReturnType(e);
        return argumentClass;
    }

    private Class getReturnType(AnnotationFilter.Entry<? extends AccessibleObject> e) {
        AccessibleObject ao = e.getObj();
        if (ao instanceof Field) {
            return ((Field)ao).getType();
        }
        if (ao instanceof Method) {
            return ((Method)ao).getReturnType();
        }

        return null; // never happens.
    }
}
