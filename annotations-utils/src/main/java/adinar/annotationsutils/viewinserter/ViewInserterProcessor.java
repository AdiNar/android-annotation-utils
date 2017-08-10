package adinar.annotationsutils.viewinserter;


import android.view.View;

public class ViewInserterProcessor {
    private static final String TAG = "ViewInserterProcessor";

    public static<T> void insertInto(View view, T object) {
        insertInto(view, object, null, 0);
    }

    /** This approach assumes that @object's tag is not used anywhere.
     *  Useful for list elements etc where tag is normally used. */
    public static<T> void insertInto(View view, T object,
                                     OnInsertedViewClickListener<T> listener, int objectId) {
        ViewInserterHolder<T> holder = (ViewInserterHolder<T>) view.getTag();
        if (holder == null) {
            holder = new ViewInserterHolder<>(view, (Class<T>) object.getClass());
            view.setTag(holder);
        }

        insertInto(holder, object, listener, objectId);
    }

    private static<T> void insertInto(ViewInserterHolder<T> holder, T object,
                                      OnInsertedViewClickListener<T> listener, int objectId) {
        holder.insertData(object, listener, objectId);
    }

    public static<T> void saveFrom(View sourceView, T destinationObject) {
        ViewInserterHolder<T> holder = ViewInserterHolder.fromView(sourceView, destinationObject);
        holder.extractDataTo(destinationObject);
    }
}
