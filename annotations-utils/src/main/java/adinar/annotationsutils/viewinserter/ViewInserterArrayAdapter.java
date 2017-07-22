package adinar.annotationsutils.viewinserter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/** Adapter that inserts data into views according to
 * {@link adinar.annotationsutils.viewinserter.annotations.InsertTo}.
 * It's only a wrapper on {@link ViewInserterHolder}, if code below doesn't fit
 * your needs just use it directly. */
public class ViewInserterArrayAdapter<T> extends ArrayAdapter<T> {
    private int layoutId;

    /**  */
    public ViewInserterArrayAdapter(Context ctx, int layoutId, List<T> data) {
        super(ctx, layoutId, data);
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        T dataModel = getItem(position);
        ViewInserterHolder<T> viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutId, null);
            if (dataModel == null) return convertView;
            viewHolder = new ViewInserterHolder<>(convertView, (Class<T>) dataModel.getClass());
            convertView.setTag(viewHolder);
        } else {
            if (dataModel == null) return convertView;
            viewHolder = (ViewInserterHolder<T>) convertView.getTag();
        }

        viewHolder.insertData(dataModel);

        return convertView;
    }
}
