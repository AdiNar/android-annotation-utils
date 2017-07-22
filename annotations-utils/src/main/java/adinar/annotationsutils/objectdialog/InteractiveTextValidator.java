package adinar.annotationsutils.objectdialog;


import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import adinar.annotationsutils.objectdialog.validation.TextViewValidator;

/** Wrapper for {@link TextViewValidator} that is triggered on every text change.
 *  Also behaves as wrapped validator, input will be validated when user clicks save button. */
class InteractiveTextValidator extends TextViewValidator {
    private static final String TAG = "InteractiveTextVal";
    private final TextViewValidator val;
    private TextWatcher watcher;

    public InteractiveTextValidator(final TextViewValidator val) {
        this.val = val;

        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                val.isValid();
            }
        };
    }

    public boolean isValidSingle() {
        return val.isValidSingle();
    }

    public void setErrorMessageInView() {
        val.setErrorMessageInView();
    }

    public void hideErrorMessageInView() {
        val.hideErrorMessageInView();
    }

    public void setErrorMessageString(String errorMessageString) {
        val.setErrorMessageString(errorMessageString);
    }

    @Override
    public String getErrorMessage() {
        return val.getErrorMessage();
    }

    @Override
    public void setView(@NonNull TextView tv) {
        Log.d(TAG, "setView");
        if (val.getView() != null) {
            val.getView().removeTextChangedListener(watcher);
        }

        val.setView(tv);

        val.getView().addTextChangedListener(watcher);
    }

    @Override
    public TextView getView() {
        return val.getView();
    }

    @Override
    protected boolean isValid(String input) {
        // Not exactly the same, but assumes that input comes from view.getText(), which is the
        // same as below.
        return val.isValidSingle();
    }
}
