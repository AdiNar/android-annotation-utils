package adinar.annotationsutils.objectdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.LruCache;

public class DialogProcessor<T> {
    private Context ctx;
    private T object;
    private Dialog dialog;

    private static LruCache<Class, DialogClassData> cache;

    private static final int DEFAULT_LRU_SIZE = 100;

    static {
        cache = new LruCache<>(DEFAULT_LRU_SIZE);
    }

    public DialogProcessor(Context ctx) {
        this.ctx = ctx;
    }

    public Dialog createDialogFrom(T object, AnnotationDialogListener<T> listener) {
        DialogClassData<T> data = prepareDataForClass((Class<T>) object.getClass());

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        DialogDataManager<T> man = new DialogDataManager<T>(object, data, builder);

        man.setTitle();
        //buttonManager.setButtons(builder); // TODO
        man.setView(ctx);

        dialog = builder.create();

        //buttonManager.setListeners(dialog, listener);

        return dialog;
    }



    private DialogClassData prepareDataForClass(Class<T> clazz) {
        if (cache.get(clazz) == null) {
            cache.put(clazz, new DialogClassData<>(clazz, ctx));
        }

        return cache.get(clazz);
    }
}
