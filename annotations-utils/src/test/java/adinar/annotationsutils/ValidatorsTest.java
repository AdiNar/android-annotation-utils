package adinar.annotationsutils;


import org.junit.Assert;
import org.junit.Test;

import adinar.annotationsutils.objectdialog.validation.Validator;
import adinar.annotationsutils.objectdialog.validation.ValidatorBuilder;

public class ValidatorsTest {

    @Test
    public void testValidatorChain() {
        ValidatorBuilder allValid = new ValidatorBuilder(),
                allInvalid = new ValidatorBuilder(),
                oneInvalidAtBeginning = new ValidatorBuilder(),
                oneInvalidInTheMiddle = new ValidatorBuilder(),
                oneInvalidAtTheEnd = new ValidatorBuilder();

        int size = 10;

        oneInvalidAtBeginning.add(new SingleValueValidator(false));

        for (int i=0; i<size; i++) {
            allInvalid.add(new SingleValueValidator(false));
            allValid.add(new SingleValueValidator(true));
            oneInvalidAtBeginning.add(new SingleValueValidator(true));
            oneInvalidInTheMiddle.add(new SingleValueValidator(!(i == size / 2)));
            oneInvalidAtTheEnd.add(new SingleValueValidator(true));
        }

        oneInvalidAtTheEnd.add(new SingleValueValidator(false));

        Assert.assertTrue(allValid.build().isValid());
        Assert.assertFalse(allInvalid.build().isValid());
        Assert.assertFalse(oneInvalidAtBeginning.build().isValid());
        Assert.assertFalse(oneInvalidInTheMiddle.build().isValid());
        Assert.assertFalse(oneInvalidAtTheEnd.build().isValid());
    }

    private class SingleValueValidator extends Validator {

        private boolean value;

        public SingleValueValidator(boolean value) {

            this.value = value;
        }
        @Override
        protected boolean isValidSingle() {
            return value;
        }

        @Override
        protected void setErrorMessageInView() {}

        @Override
        protected void hideErrorMessageInView() {}
    }
}
