package adinar.annotationsexample.viewinserter;


import android.os.Bundle;
import android.view.View;

import adinar.annotationsexample.R;
import adinar.annotationsexample.custom.CustomTitleActivity;
import adinar.annotationsutils.viewinserter.ViewInserterProcessor;

/** This example shows how to easily, with annotations make views able to save data to objects. */
public class ViewInserterSaveExample extends CustomTitleActivity {
    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.view_inserter_save_example_layout);

        init();
    }

    private void init() {
        Juice juice = new Juice();
        View viewToInsert;
        viewToInsert = findViewById(R.id.src);
        ViewInserterProcessor.insertInto(viewToInsert, juice);
    }

    public void onButtonClick(View view) {
        View viewToInsert;
        Juice newJuice = new Juice();
        viewToInsert = findViewById(R.id.src);

        // Here is the main part, load data from view to object.
        // If there was already a tag holder created for view it will be reused,
        /// otherwise it will be set as view tag.
        ViewInserterProcessor.saveFrom(viewToInsert, newJuice);

        viewToInsert = findViewById(R.id.dst);
        ViewInserterProcessor.insertInto(viewToInsert, newJuice);
    }
}
