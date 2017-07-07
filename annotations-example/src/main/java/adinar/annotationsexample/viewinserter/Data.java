package adinar.annotationsexample.viewinserter;


import adinar.annotationsexample.R;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

public class Data {

    public Data() {
        boolValue = true;
        checkBoxTitle = "CheckBoxTitle";
        progress = 70;
        hint = "Nice hint!";
    }

    @InsertTo(id = R.id.checkBox, method = "setChecked")
    private boolean boolValue;

    @InsertTo(id = R.id.checkBox)
    private String checkBoxTitle;

    @InsertTo(id = R.id.seekBar, method = "setProgress")
    private int progress;

    @InsertTo(id = R.id.editText, method = "setHint")
    private String hint;
}
