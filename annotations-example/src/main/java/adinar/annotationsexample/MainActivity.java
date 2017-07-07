package adinar.annotationsexample;

import adinar.annotationsexample.custom.ExampleListActivity;
import adinar.annotationsexample.dialog.DialogExamplesList;

public class MainActivity extends ExampleListActivity {

    @Override
    protected String getCustomTitle() {
        return "Annotation examples";
    }

    @Override
    protected Entry[] getEntries() {
        return new Entry[]{new Entry("Dialog examples", DialogExamplesList.class)};
    }
}
