package adinar.annotationsutils.objectdialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.List;

import adinar.annotationsutils.R;

/** Bridge between AlertDialog.Builder and DialogClassData. */
class DialogDataManager<T> {
    private static final String TAG = "DialogDataManager";
    private DialogClassData<T> data;
    private AlertDialog.Builder builder;
    private T object;

    public DialogDataManager(T object, DialogClassData<T> data, AlertDialog.Builder builder) {
        this.data = data;
        this.builder = builder;
        this.object = object;
    }

    public void setTitle() {
        builder.setTitle(data.getTitleEntry().getTitleFrom(object));
    }


    public void setView(Context ctx) {
        List<DialogFieldEntry<T>> entries = data.getFields();

        LinearLayout view = (LinearLayout) LayoutInflater.from(ctx)
                .inflate(R.layout.dialog_layout, null);

        for (DialogFieldEntry<T> e : entries) {
            view.addView(e.generateView(object, ctx));
        }

        builder.setView(view);
    }
}
