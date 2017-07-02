package adinar.annotationsutils.objectdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.LruCache;

public class DialogProcessor<T> {
    private Context ctx;
    private Dialog dialog;

    private static LruCache<Class, DialogClassData> cache;

    private static final int DEFAULT_LRU_SIZE = 1000;

    static {
        cache = new LruCache<>(DEFAULT_LRU_SIZE);
    }

    public DialogProcessor(Context ctx) {
        this.ctx = ctx;
    }

    public Dialog createDialogFrom(T object, AnnotationDialogListener<T> listener) {
        DialogClassData<T> data = prepareDataForClass((Class<T>) object.getClass());

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        DialogDataManager<T> man = new DialogDataManager<>(object, data, builder);

        man.setTitle();
        man.setView(ctx);
        man.setButtons(listener);

        dialog = builder.create();

        // Dialog must be shown before the next line with setting positive listener.
        dialog.show();
        man.setPositiveButtonListener((AlertDialog) dialog, listener);

        return dialog;
    }

    /** Call this in activity onBackPressed to hide dialog. */
    public void onBackPressed() {
        if (dialog != null && dialog.isShowing()) dialog.hide();
    }

    private DialogClassData prepareDataForClass(Class<T> clazz) {
        if (cache.get(clazz) == null) {
            cache.put(clazz, new DialogClassData<>(clazz, ctx));
        }

        return cache.get(clazz);
    }
}
