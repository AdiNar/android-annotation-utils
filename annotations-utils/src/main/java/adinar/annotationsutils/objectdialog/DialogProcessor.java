package adinar.annotationsutils.objectdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.LruCache;

/** Main class containing methods to deal with dialogs. */
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

    /** Creates dialog based on object's class annotations, applies listener
     *  for dialog results and shows dialog.
     *  @return Created dialog object, remember that it's already shown. */
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

    /** Caches class dialog data extracted from it's annotations. */
    private DialogClassData prepareDataForClass(Class<T> clazz) {
        if (cache.get(clazz) == null) {
            cache.put(clazz, new DialogClassData<>(clazz, ctx));
        }

        return cache.get(clazz);
    }
}
