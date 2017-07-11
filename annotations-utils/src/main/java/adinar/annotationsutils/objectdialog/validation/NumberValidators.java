package adinar.annotationsutils.objectdialog.validation;

import java.math.BigDecimal;

/** Set of validator classes that handles numbers. */
public class NumberValidators {
    public abstract static class NumberValidator extends TextViewValidator {
        protected abstract boolean isValid(BigDecimal number);

        @Override
        protected boolean isValid(String input) {
            BigDecimal number = parseNumber(input);
            if (number == null) return false;
            return isValid(number);
        }

        private BigDecimal parseNumber(String input) {
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class GE_NumberValidator extends NumberValidator {
        @Override
        protected boolean isValid(BigDecimal number) {
            return number.signum() >= 0;
        }
    }
}
