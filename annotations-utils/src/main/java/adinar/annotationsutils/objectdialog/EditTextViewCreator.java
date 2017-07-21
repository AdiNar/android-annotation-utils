package adinar.annotationsutils.objectdialog;


import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import adinar.annotationsutils.R;

class EditTextViewCreator extends DialogElementViewCreator<EditText> {
    private DialogEditTextFieldEntry entry;
    EditText valueView;

    public EditTextViewCreator(Context ctx) {
        super(ctx);
    }

    @Override
    public EditText getValueView() {
        return valueView;
    }

    public <T> EditTextViewCreator(DialogEditTextFieldEntry<T> dialogFieldEntry, Context ctx) {
        this(ctx);
        this.entry = dialogFieldEntry;
    }

    @Override
    protected void fillView(View view) {
        EditText et = (EditText) view.findViewById(R.id.value);
        et.setText(entry.getFieldValue());
        et.setHint(entry.getHint());
        et.setInputType(entry.getInputType());
        valueView = et;

        TextView tv = (TextView) view.findViewById(R.id.label);
        tv.setText(entry.getLabel());
    }

    @Override
    protected int getViewId() {
        return R.layout.edit_text;
    }
}
