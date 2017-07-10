package adinar.annotationsutils.viewinserter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import adinar.annotationsutils.common.AnnotationFilter;
import adinar.annotationsutils.common.AnnotationFilterEntry;
import adinar.annotationsutils.common.DefaultAnnotationFilter;
import adinar.annotationsutils.common.FieldEntry;
import adinar.annotationsutils.objectdialog.DialogFieldEntry;
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
            filter = new InsertToAnnotationFilter(clazz).filter();
            cache.put(clazz, filter);
        }
        return filter;
    }

    /** Look for all ids used in annotations and cache their views. */
    private void matchIdsWithViews(View view, AnnotationFilter filter) {
        for (AnnotationFilterEntry e : filter.getAllAnnotated()) {
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

        for (AnnotationFilterEntry<? extends AccessibleObject> e : filter.getAllAnnotated()) {
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

            Method meth = MethodResolver
                    .getMethodAndCheckHeuristics(ann.method(), argumentClass, methodClass);

            try {
                Object value = e.getValue(item);

                if (ann.asString()) {
                    value = value.toString();
                }

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

    private Class getMethodClass(InsertTo ann) {
        Class methodClass = ann.methodClass();
        if (methodClass == void.class) methodClass = idToViewMap.get(ann.id()).getClass();
        return methodClass;
    }

    private Class getArgumentClass(AnnotationFilterEntry e, InsertTo ann) {
        Class argumentClass = ann.argumentClass();
        if (ann.asString()) {
            argumentClass = String.class;
        }
        if (argumentClass == void.class) argumentClass = e.getReturnType();
        return argumentClass;
    }

    public static<T> ViewInserterHolder<T> fromView(View view, T item) {
        ViewInserterHolder<T> holder = (ViewInserterHolder<T>) view.getTag();
        if (holder == null) {
            holder = (ViewInserterHolder<T>) new ViewInserterHolder<>(view, item.getClass());
            view.setTag(holder);
        }

        return holder;
    }

    public void extractDataTo(T destinationObject) {
        for (FieldEntry e : filter.getFields()) {
            InsertTo.AllowSave save = e.getSaveAnnotation();

            if (save.allowed()) {
                try {
                    setFieldValue(destinationObject, e, ann);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void setFieldValue(T destinationObject, AnnotationFilterEntry e, InsertTo ann)
            throws IllegalAccessException, InvocationTargetException {
        InsertTo.AllowSave save = ann.save();
        if (!save.saveMethodName().isEmpty()) {
            Class argumentClass = save.saveMethodArgument();
            if (argumentClass == void.class) {
                argumentClass = e.getReturnType();
            }
            Method meth = MethodResolver.getMethodFor(save.saveMethodName(),
                    argumentClass, destinationObject.getClass());
            meth.invoke(destinationObject, getValueFromView(ann));
        } else {
            Object valueFromView = getValueFromView(ann);

            if (!e.getReturnType().isAssignableFrom(valueFromView.getClass())) {
                 valueFromView = DialogFieldEntry
                        // TODO this is valueOf(String) only, provide type depentent solution.
                        .getValueOfForClass(e.getReturnType())
                        .invoke(destinationObject, valueFromView.toString());
            }

            e.setValue(destinationObject, valueFromView);
        }
    }

    private Object getValueFromView(InsertTo ann) {
        InsertTo.AllowSave save = ann.save();
        View view = idToViewMap.get(ann.id());

        Class viewClass = save.viewMethodClass();
        if (viewClass == void.class) viewClass = view.getClass();

        String methodName = save.viewMethodName();
        if (methodName.isEmpty()) {
            methodName = getDefaultMethodForClass(viewClass);
        }

        Method meth = MethodResolver.getMethodFor(methodName,
                void.class, viewClass);

        try {
            return meth.invoke(view);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** Some basic methods, this section should be larger probably. */
    private String getDefaultMethodForClass(Class viewClass) {
        if (TextView.class.isAssignableFrom(viewClass)) {
            return "getText";
        }

        if (CheckBox.class.isAssignableFrom(viewClass)) {
            return "isChecked";
        }

        return "";
    }
}
