package adinar.annotationsutils.objectdialog.validation;


import android.support.annotation.NonNull;
import android.view.View;

public abstract class Validator<T extends View> {
    protected T view;

    public final boolean isValid() {
        boolean result = false;

        if(!isValidSingle()) {
            setErrorMessageInView();
        } else {
            result = true;
            hideErrorMessageInView();
        }

        return (next == null || next.isValid()) && result;
    }

    protected abstract boolean isValidSingle();
    protected abstract void setErrorMessageInView();
    protected abstract void hideErrorMessageInView();

    private Validator next;

    // Empty constructor is needed by builders, use with setView().
    public Validator() {}
    public Validator(T view) {
        setView(view);
    }

    Validator getNext() {
        return next;
    }

    /** A bit tricky, when next is already set then go to the end of this list.
     *  This allows to concatenate validators list. */
    public void setNext(Validator next) {
        if (this.next != null) this.next.setNext(next);
        else this.next = next;
    }

    public void setView(@NonNull T view) {
        this.view = view;
    }

    public T getView() {
        return view;
    }
}
