package adinar.annotationsexample.viewinserter;


import android.os.Bundle;
import android.view.View;

import adinar.annotationsexample.R;
import adinar.annotationsexample.common.CustomTitleActivity;
import adinar.annotationsutils.viewinserter.ViewInserterProcessor;

public class ViewInserterInheritanceExample extends CustomTitleActivity {
    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.view_inserter_inheritance_layout);

        init();
    }

    private void init() {
        InheritedData inheritedData = new InheritedData();
        View viewToInsert = findViewById(R.id.data_layout_inheritance);

        ViewInserterProcessor.insertInto(viewToInsert, inheritedData);

        NotInheritedData notInheritedData = new NotInheritedData();
        viewToInsert = findViewById(R.id.data_layout_no_inheritance);

        ViewInserterProcessor.insertInto(viewToInsert, notInheritedData);
    }
}
