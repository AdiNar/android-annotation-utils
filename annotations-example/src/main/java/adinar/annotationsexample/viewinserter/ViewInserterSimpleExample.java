package adinar.annotationsexample.viewinserter;


import android.os.Bundle;
import android.view.View;

import adinar.annotationsexample.R;
import adinar.annotationsexample.common.CustomTitleActivity;
import adinar.annotationsutils.viewinserter.ViewInserterProcessor;

public class ViewInserterSimpleExample extends CustomTitleActivity {

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.view_inserter_layout);

        init();
    }

    private void init() {
        Person person;
        View viewToInsert;

        person = new Person("John", "Johnatansky", 20);
        viewToInsert = findViewById(R.id.person_layout);

        /* Here the view is filled, it's just a one liner. */
        ViewInserterProcessor.insertInto(viewToInsert, person);
        
        Data data = new Data();
        viewToInsert = findViewById(R.id.data_layout);
        ViewInserterProcessor.insertInto(viewToInsert, data);
    }
}
