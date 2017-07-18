package adinar.annotationsutils.viewinserter;

import android.view.View;

/** Listener for {@link ViewInserterHolder}'s view click events. Used mostly by other library
 *  parts, for 'personal' use it's better to simply set {@link View.OnClickListener}. */
public interface OnInsertedViewClickListener<T> {
    void onItemClick(T item, int itemId, View v);
}
