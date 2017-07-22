package adinar.annotationsutils;

import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import adinar.annotationsutils.objectdialog.validation.NumberValidators;
import adinar.annotationsutils.objectdialog.validation.TextViewValidator;

import static android.support.test.InstrumentationRegistry.getContext;

@RunWith(AndroidJUnit4.class)
public class ValidatorsTest {

    @Test
    public void nonEmptyValidator() {
        TextViewValidator.NonEmptyValidator validator =
                new TextViewValidator.NonEmptyValidator();

        TextView tv = new TextView(getContext());
        validator.setView(tv);

        tv.setText("");
        Assert.assertFalse(validator.isValid());

        tv.setText("test");
        Assert.assertTrue(validator.isValid());
    }

    @Test
    public void numberValidators() {
        NumberValidators.NumberValidator validator =
                new NumberValidators.GE_NumberValidator();

        TextView tv = new TextView(getContext());
        validator.setView(tv);

        tv.setText("0");
        Assert.assertTrue(validator.isValid());
        tv.setText("0.12");
        Assert.assertTrue(validator.isValid());
        tv.setText("10000000");
        Assert.assertTrue(validator.isValid());
         tv.setText("-10");
        Assert.assertFalse(validator.isValid());
        tv.setText("-0.0000001");
        Assert.assertFalse(validator.isValid());
    }
}
