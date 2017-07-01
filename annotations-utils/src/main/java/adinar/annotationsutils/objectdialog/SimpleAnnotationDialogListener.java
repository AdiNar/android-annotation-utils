package adinar.annotationsutils.objectdialog;

/** Listener for most typical situation when only acceptance event matters. */
public abstract class SimpleAnnotationDialogListener<T> implements AnnotationDialogListener<T> {

    @Override
    public void onDialogCancelled() {}

    @Override
    public void onDialogDismissed() {}

    @Override
    public void onDialogNeutral() {}
}
