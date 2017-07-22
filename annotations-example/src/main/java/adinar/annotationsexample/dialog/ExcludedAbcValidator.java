package adinar.annotationsexample.dialog;


import adinar.annotationsutils.objectdialog.validation.TextViewValidator;

public class ExcludedAbcValidator extends TextViewValidator.ExcludedCharsValidator {
    public ExcludedAbcValidator() {
        super("abc");
    }
}
