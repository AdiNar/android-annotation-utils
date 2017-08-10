package adinar.annotationsutils.viewinserter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import adinar.annotationsutils.common.AnnotationFilter;
import adinar.annotationsutils.common.AnnotationFilterEntryWithValue;
import adinar.annotationsutils.common.Cache;
import adinar.annotationsutils.common.DefaultAnnotationFilter;
import adinar.annotationsutils.common.FieldAndMethodAccess;
import adinar.annotationsutils.common.FieldEntry;
import adinar.annotationsutils.objectdialog.DialogFieldEntry;
import adinar.annotationsutils.viewinserter.annotations.InsertEmbeddedTo;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

import static adinar.annotationsutils.common.Cache.getCached;

public class ViewInserterHolder<T> extends RecyclerView.ViewHolder {
    private static final String TAG = "ViewInserterHolder";
    private SparseArray<View> idToViewMap = new SparseArray<>();
    private InsertToAnnotationFilter filter;
    private DefaultAnnotationFilter embeddedFilter;

    private ViewInserterHolder(View view, InsertToAnnotationFilter filter,
                               DefaultAnnotationFilter embeddedFilter) {
        super(view);
        this.filter = filter;
        this.embeddedFilter = embeddedFilter;
        matchIdsWithViews(view, filter);
        matchEmbeddedIdsWithViews(view, embeddedFilter);
    }

    public ViewInserterHolder(View view, Class<T> clazz) {
        this(view, getAnnotationFilter(clazz), getEmbeddedFilter(clazz));
    }

    private static <T> DefaultAnnotationFilter getEmbeddedFilter(final Class<T> clazz) {
        return Cache.getCached(new Pair<>(new Pair<>(ViewInserterHolder.class, DefaultAnnotationFilter.class), clazz),
                new Cache.Supplier<DefaultAnnotationFilter>() {
                    @Override
                    public DefaultAnnotationFilter get() {
                        return (DefaultAnnotationFilter) new DefaultAnnotationFilter(clazz)
                                .addAnnotation(InsertEmbeddedTo.class).filter();
                    }
                });
    }

    private static<T> InsertToAnnotationFilter getAnnotationFilter(final Class<T> clazz) {
        return getCached(new Pair<>(ViewInserterHolder.class, clazz),
                new Cache.Supplier<InsertToAnnotationFilter>() {
            @Override
            public InsertToAnnotationFilter get() {
                return (InsertToAnnotationFilter) new InsertToAnnotationFilter(clazz).filter();
            }
        });
    }

    /** Look for all ids used in annotations and cache their views. */
    private void matchIdsWithViews(View view, AnnotationFilter filter) {
        List<AnnotationFilterEntryWithValue> fields = filter.getAllAnnotated();
        for (AnnotationFilterEntryWithValue e : fields) {
            InsertTo ann = (InsertTo) e.getAnn(InsertTo.class);
            View annView = view.findViewById(ann.id());
            if (annView != null) idToViewMap.put(ann.id(), annView);
        }
    }

    /** Same for embedded. */
    private void matchEmbeddedIdsWithViews(View view, DefaultAnnotationFilter filter) {
        List<? extends AnnotationFilterEntryWithValue> fields = filter.getAllAnnotated();
        for (AnnotationFilterEntryWithValue e : fields) {
            InsertEmbeddedTo ann = (InsertEmbeddedTo) e.getAnn(InsertEmbeddedTo.class);
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

        insertEmbeddedData(item, listener, itemId);

        for (AnnotationFilterEntryWithValue e : filter.getAllAnnotated()) {
            InsertTo ann = (InsertTo) e.getAnn(InsertTo.class);
            View dst = idToViewMap.get(ann.id());

            if (assertViewNotNull(ann.id(), dst)) continue;

            Class argumentClass = getArgumentClass(e, ann);
            Class methodClass = getMethodClass(ann);

            Method meth = MethodResolver
                    .getMethodAndCheckHeuristics(ann.method(), argumentClass, methodClass);

            try {
                Object value = e.getValue(item);

                if (value != null) {
                    if (ann.asString()) {
                        value = value.toString();
                    }
                    meth.invoke(dst, value);
                }
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

    private boolean assertViewNotNull(int id, View dst) {
        if (dst == null) {
            // TODO some more descriptive info should be logged here but actually
            // there's no direct access to names or so.
            Log.i(TAG, String.format("View does not contain id %d.", id));
            return true;
        }
        return false;
    }

    /** Insert data from nested classes annotated with {@link InsertEmbeddedTo}. */
    private void insertEmbeddedData(T item, OnInsertedViewClickListener<T> listener, int itemId) {
        for (FieldEntry f : embeddedFilter.getFields()) {
            InsertEmbeddedTo ann = f.getAnn(InsertEmbeddedTo.class);
            View dst = idToViewMap.get(ann.id());

            if (assertViewNotNull(ann.id(), dst)) continue;
            try {
                Object embeddedItem = FieldAndMethodAccess.getFieldValue(f.getObj(), item);
                ViewInserterProcessor.insertInto(dst, embeddedItem, null, itemId); // TODO listener not supported!
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Class getMethodClass(InsertTo ann) {
        Class methodClass = ann.methodClass();
        if (methodClass == void.class) methodClass = idToViewMap.get(ann.id()).getClass();
        return methodClass;
    }

    private Class getArgumentClass(AnnotationFilterEntryWithValue e, InsertTo ann) {
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
            InsertTo ann = e.getAnn(InsertTo.class);
            InsertTo.AllowSave save = ann.save();


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

    private void setFieldValue(T destinationObject, FieldEntry e, InsertTo ann)
            throws IllegalAccessException, InvocationTargetException {
        InsertTo.AllowSave save = ann.save();
        if (!save.saveMethodName().isEmpty()) {
            Class argumentClass = getSaveMethodArgument(e, save);

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

    private Class getSaveMethodArgument(FieldEntry e, InsertTo.AllowSave save) {
        Class argumentClass = save.saveMethodArgument();
        if (argumentClass == void.class) {
            argumentClass = e.getReturnType();
        }
        return argumentClass;
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

    /** Some basic methods, this section should be larger probably. TODO */
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
