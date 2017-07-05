package adinar.annotationsutils.viewinserter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import adinar.annotationsutils.common.AnnotationFilter;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

public class ViewInserterHolder<T> extends RecyclerView.ViewHolder {
    private static final String TAG = "ViewInserterHolder";
    private SparseArray<View> idToViewMap = new SparseArray<>();
    private AnnotationFilter filter;

    public ViewInserterHolder(View view, AnnotationFilter filter) {
        super(view);
        this.filter = filter;
        matchIdsWithViews(view, filter);
    }

    public ViewInserterHolder(View view, Class<T> clazz) {
        this(view, new AnnotationFilter(clazz).addAnnotation(InsertTo.class));
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

        for (AnnotationFilter.Entry<Field> e : filter.getFields()) {
            InsertTo ann = e.getAnn(InsertTo.class);
            View dst = idToViewMap.get(ann.id());

            if (dst == null) {
                // TODO some more descriptive info should be logged here but actually
                // there's no direct access to names or so.
                Log.i(TAG, String.format("View does not contain id %d.", ann.id()));
                continue;
            }

            // @meth is a view method like EditText.getText(), value of item field / method
            // will be applied to it.
            Method meth = ViewMethodResolver.getInstance().getMethodFor(
                    ann.method(),
                    ann.argumentClass(),
                    ann.methodClass()
            );

            Object value = e.getValue(item);

            try {
                meth.invoke(dst, ann.argumentClass().cast(value));
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
}
