package adinar.annotationsexample.viewinserter;


import android.os.Bundle;
import android.view.View;

import adinar.annotationsexample.R;
import adinar.annotationsexample.custom.CustomTitleActivity;
import adinar.annotationsutils.viewinserter.ViewInserterProcessor;

public class ViewInserterSimpleExample extends CustomTitleActivity {

    private Person data;
    private View viewToInsert;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.view_inserter_layout);

        init();
        insertData();
    }

    private void insertData() {
        ViewInserterProcessor.insertInto(viewToInsert, data);
    }

    private void init() {
        data = new Person("John", "Johnatansky", 20);
        viewToInsert = findViewById(R.id.data_layout);
    }
}
