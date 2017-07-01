package adinar.annotationsutils.objectdialog;

/** Callback for typical dialog actions. */
interface AnnotationDialogListener<T> {
    /** Called on dialog's positive button click after the input is validated.
      * @param object: This is an object with data from dialog injected. This is also the same
      *                object that was passed to DialogProcessor TODO class.
      **/
    void onDialogAccepted(T object);
    /** Called on dialog's negative button click. */
    void onDialogCancelled();
    /** Called on click outside dialog or back button. */
    void onDialogDismissed();
    /** Called on dialog's neutral button click. */
    void onDialogNeutral();
}
