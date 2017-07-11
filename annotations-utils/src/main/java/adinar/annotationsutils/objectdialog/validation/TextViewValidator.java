package adinar.annotationsutils.objectdialog.validation;


import android.widget.EditText;
import android.widget.TextView;

/** Validator for {@link TextView} and {@link EditText}, contains input extraction
 *  and handles error message. */
public abstract class TextViewValidator extends Validator<TextView> {
    private static final String TAG = "TextViewValidator";
    private String errorMessage;

    public TextViewValidator() {}

    public boolean isValidSingle() {
        return isValid(view.getText().toString());
    }

    public void setErrorMessageInView() {
        view.setError(errorMessage);
    }

    protected abstract boolean isValid(String input);

    public void setErrorMessageString(String errorMessageString) {
        this.errorMessage = errorMessageString;
    }

    public static class NonEmptyValidator extends TextViewValidator {
        @Override
        protected boolean isValid(String input) {
            return input != null && !input.isEmpty();
        }
    }
}