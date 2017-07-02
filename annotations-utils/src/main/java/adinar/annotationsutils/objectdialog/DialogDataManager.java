package adinar.annotationsutils.objectdialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import adinar.annotationsutils.R;
import adinar.annotationsutils.objectdialog.validation.ValidatorBuilder;

/** Bridge between AlertDialog.Builder and DialogClassData. */
class DialogDataManager<T> {
    private static final String TAG = "DialogDataManager";
    private DialogClassData<T> data;
    private AlertDialog.Builder builder;
    private ValidatorBuilder validatorBuilder;
    private T object;

    public DialogDataManager(T object, DialogClassData<T> data, AlertDialog.Builder builder) {
        this.data = data;
        this.builder = builder;
        this.object = object;
        validatorBuilder = new ValidatorBuilder();
    }

    public void setTitle() {
        builder.setTitle(data.getTitleEntry().getTitleFrom(object));
    }


    public void setView(Context ctx) {
        List<DialogFieldEntry<T>> entries = data.getFields();

        LinearLayout view = (LinearLayout) LayoutInflater.from(ctx)
                .inflate(R.layout.dialog_layout, null);

        for (DialogFieldEntry<T> e : entries) {
            view.addView(e.generateView(object, ctx, validatorBuilder));
        }

        builder.setView(view);
    }

    public void setButtons(final AnnotationDialogListener<T> listener) {
        new DialogButtonsBuilder<>(builder, data, listener).build();
    }

    public void setPositiveButtonListener(final AlertDialog dialog,
                                          final AnnotationDialogListener<T> listener) {

        Button dialogButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (dialogButton != null)
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validatorBuilder.build().isValidChain()) {
                            for (DialogFieldEntry<T> e : data.getFields()) {
                                e.setFieldValueFromView();
                            }

                            listener.onDialogAccepted(object);
                            dialog.hide();
                        }
                    }
                });
    }
}
