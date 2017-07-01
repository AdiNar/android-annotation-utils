package adinar.annotationsutils.objectdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/** Abstract class that creates view out of
 * {@link adinar.annotationsutils.objectdialog.DialogClassData}.
 * Different implementations contain different view types. */
abstract class DialogElementViewCreator {
    private Context ctx;

    public DialogElementViewCreator(Context ctx) {
        this.ctx = ctx;
    }

    public View create() {
        View view = LayoutInflater.from(ctx).inflate(getViewId(), null);
        fillView(view);
        return view;
    }

    protected abstract void fillView(View view);
    protected abstract int getViewId();
}
