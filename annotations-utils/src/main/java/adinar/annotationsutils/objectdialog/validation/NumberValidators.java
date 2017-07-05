package adinar.annotationsutils.objectdialog.validation;

import java.math.BigDecimal;

/** Set of validator classes that handles numbers. */
public class NumberValidators {
    public abstract static class NumberValidator<T extends Number> extends TextViewValidator {
        protected abstract boolean isValid(Number number);

        @Override
        protected boolean isValid(String input) {
            Number number = parseNumber(input);
            if (number == null) return false;
            return isValid(number);
        }

        private Number parseNumber(String input) {
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class GE_NumberValidator<T extends Number> extends NumberValidator<T> {

        @Override
        protected boolean isValid(Number number) {
            return number.longValue() >= 0;
        }
    }
}
