package adinar.annotationsexample.dialog;

import adinar.annotationsexample.R;
import adinar.annotationsutils.objectdialog.annotations.DialogButton;
import adinar.annotationsutils.objectdialog.annotations.DialogClass;
import adinar.annotationsutils.objectdialog.annotations.DialogEditText;
import adinar.annotationsutils.objectdialog.annotations.DialogTitle;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

@DialogClass(buttons = {@DialogButton(
        textId = R.string.add,
        type = DialogButton.ButtonType.POSITIVE)}
)
class Titled {

    @InsertTo(id = R.id.field1)
    @DialogEditText(order = 1)
    private String titleA = "A title";

    @InsertTo(id = R.id.field2)
    @DialogEditText(order = 2)
    private String titleB = " made of two strings.";

    @DialogTitle
    String getTitle() {
        return titleA + titleB;
    }
}
