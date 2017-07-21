package adinar.annotationsexample.dialog;

import android.text.InputType;

import adinar.annotationsexample.R;
import adinar.annotationsutils.objectdialog.annotations.DialogButton;
import adinar.annotationsutils.objectdialog.annotations.DialogClass;
import adinar.annotationsutils.objectdialog.annotations.DialogEditText;
import adinar.annotationsutils.objectdialog.validation.NumberValidators;

@DialogClass(titleId = R.string.city,
        buttons = {@DialogButton(
                textId = R.string.OK,
                type = DialogButton.ButtonType.POSITIVE)}
)
class City {
    @DialogEditText(order = 1, labelId = R.string.zip_code, inputType =
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED)
    private String zipCode;

    @DialogEditText(order = 3, labelId = R.string.phone, inputType = InputType.TYPE_CLASS_PHONE)
    private String phoneToPresident;

    @DialogEditText(order = 2, labelId = R.string.citizens,
            validators = @DialogEditText.ETValidator(
                    clazz = NumberValidators.GE_NumberValidator.class,
                    errorMsgId = R.string.must_be_ge_0))
    private int citizens;
}
