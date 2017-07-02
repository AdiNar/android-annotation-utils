package adinar.annotationsutils.viewinserter;

import android.view.View;

/** Listener for {@link ViewInserterHolder}'s view click events. */
public interface OnInsertedViewClickListener<T> {
    void onItemClick(T item, int itemId, View v);
}
