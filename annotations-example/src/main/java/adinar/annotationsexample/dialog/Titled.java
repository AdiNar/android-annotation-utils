package adinar.annotationsexample.dialog;

import adinar.annotationsexample.R;
import adinar.annotationsutils.objectdialog.annotations.DialogButton;
import adinar.annotationsutils.objectdialog.annotations.DialogClass;
import adinar.annotationsutils.objectdialog.annotations.DialogEditText;
import adinar.annotationsutils.objectdialog.annotations.DialogTitle;

@DialogClass(buttons = {@DialogButton(
        textId = R.string.add,
        type = DialogButton.ButtonType.POSITIVE)}
)
class Titled {

    @DialogEditText(order = 1)
    private String titleA = "A title";

    @DialogEditText(order = 2)
    private String titleB = " made of two strings.";

    @DialogTitle
    String getTitle() {
        return titleA + titleB;
    }
}
