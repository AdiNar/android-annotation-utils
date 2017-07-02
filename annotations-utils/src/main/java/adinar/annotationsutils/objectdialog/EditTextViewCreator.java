package adinar.annotationsutils.objectdialog;


import android.content.Context;
import android.view.View;
import android.widget.EditText;

import adinar.annotationsutils.R;

class EditTextViewCreator extends DialogElementViewCreator {
    private DialogFieldEntry entry;

    public EditTextViewCreator(Context ctx) {
        super(ctx);
    }

    public <T> EditTextViewCreator(DialogFieldEntry<T> dialogFieldEntry, Context ctx) {
        this(ctx);
        this.entry = dialogFieldEntry;
    }

    @Override
    protected void fillView(View view) {
        EditText et = (EditText) view.findViewById(R.id.value);
        et.setText(entry.getFieldValue());
    }

    @Override
    protected int getViewId() {
        return R.layout.edit_text;
    }
}
