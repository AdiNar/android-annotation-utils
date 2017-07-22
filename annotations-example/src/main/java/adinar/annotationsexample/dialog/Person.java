package adinar.annotationsexample.dialog;

import adinar.annotationsexample.R;
import adinar.annotationsutils.objectdialog.annotations.DialogButton;
import adinar.annotationsutils.objectdialog.annotations.DialogClass;
import adinar.annotationsutils.objectdialog.annotations.DialogEditText;
import adinar.annotationsutils.objectdialog.validation.TextViewValidator;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

@DialogClass(title = "Person",
        buttons = {@DialogButton()}
)
public class Person {
    @InsertTo(id = R.id.field1, asString = true)
    @DialogEditText(
            order = 1,
            labelId = R.string.name,
            validators = @DialogEditText.ETValidator(
                    clazz = TextViewValidator.NonEmptyValidator.class,
                    errorMsgId = R.string.error
            )
    )
    private String name;

    @InsertTo(id = R.id.field2, asString = true)
    @DialogEditText(order=2, hintId = R.string.contact_phone)
    private String contactPhone;

    public String getName() {
        return name;
    }
}
