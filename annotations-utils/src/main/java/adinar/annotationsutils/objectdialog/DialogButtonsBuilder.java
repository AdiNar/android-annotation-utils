package adinar.annotationsutils.objectdialog;


import android.app.AlertDialog;
import android.content.DialogInterface;

import adinar.annotationsutils.objectdialog.annotations.DialogButton;

/** Add buttons to {@link AlertDialog.Builder} and binds them to dialog listener. */
public class DialogButtonsBuilder<T> {
    private static final String TAG = "DialogButtonsBuilder";
    private AlertDialog.Builder builder;
    private DialogClassData<T> data;
    private AnnotationDialogListener<T> listener;

    public DialogButtonsBuilder(AlertDialog.Builder builder,
                                DialogClassData<T> data,
                                AnnotationDialogListener<T> listener) {

        this.builder = builder;
        this.data = data;
        this.listener = listener;
    }

    public void build() {
        DialogButton pos = data.getPositiveButton(), neg = data.getNegativeButton();

        setPositiveButton(pos);
        setNegativeButton(neg);

        for (final DialogButton button : data.getNeutralButtons()) {
            addNeutralButton(button);
        }
    }

    private void addNeutralButton(final DialogButton button) {
        builder.setNeutralButton(button.textId(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNeutral(button.buttonId());
            }
        });
    }

    private void setNegativeButton(DialogButton neg) {
        if (neg != null) {
            builder.setNegativeButton(neg.textId(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onDialogCancelled();
                }
            });
        }
    }

    private void setPositiveButton(final DialogButton pos) {
        if (pos != null) {
            // Listener is not set here because we want to decide whether dialog should
            // be closed on this click and such listener won't do that.
            builder.setPositiveButton(pos.textId(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
    }
}
