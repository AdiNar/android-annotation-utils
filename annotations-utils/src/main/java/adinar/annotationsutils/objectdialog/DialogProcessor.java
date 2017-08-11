package adinar.annotationsutils.objectdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.util.Pair;

import adinar.annotationsutils.common.Cache;

/** Main class containing methods to deal with dialogs. */
public class DialogProcessor<T> {
    private Context ctx;
    private Dialog dialog;

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
    private DialogClassData prepareDataForClass(final Class<T> clazz) {
        return Cache.getCached(new Pair<>(DialogProcessor.class, clazz), new Cache.Supplier<DialogClassData>() {
            @Override
            public DialogClassData get() {
                return new DialogClassData<>(clazz, ctx);
            }
        });
    }
}
