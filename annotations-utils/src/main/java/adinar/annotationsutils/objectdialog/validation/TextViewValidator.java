package adinar.annotationsutils.objectdialog.validation;


import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        view.setError(getErrorMessage());
    }

    public void hideErrorMessageInView() {
        view.setError(null);
    }

    protected abstract boolean isValid(String input);

    public void setErrorMessageString(String errorMessageString) {
        this.errorMessage = errorMessageString;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static class NonEmptyValidator extends TextViewValidator {
        @Override
        protected boolean isValid(String input) {
            return input != null && !input.isEmpty();
        }
    }

    public static class ExcludedCharsValidator extends TextViewValidator {
        private final Set<Character> excluded;

        public ExcludedCharsValidator(String excluded) {
            this.excluded = new HashSet<>();

            for (char a : excluded.toCharArray()) {
                this.excluded.add(a);
            }
        }

        public ExcludedCharsValidator(List<Character> excluded) {
            this.excluded = new HashSet<>(excluded);
        }

        public ExcludedCharsValidator(Set<Character> excluded) {
            this.excluded = excluded;
        }

        @Override
        protected boolean isValid(String input) {
            for (char ch : input.toCharArray()) {
                if (excluded.contains(ch)) {
                    return false;
                }
            }

            return true;
        }
    }
}