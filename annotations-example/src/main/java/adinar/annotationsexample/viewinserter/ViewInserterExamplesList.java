package adinar.annotationsexample.viewinserter;


import adinar.annotationsexample.custom.ExampleListActivity;

public class ViewInserterExamplesList  extends ExampleListActivity {

    @Override
    protected Entry[] getEntries() {
        return new Entry[] {
            new Entry("Simple", ViewInserterSimpleExample.class),
            new Entry("Save", ViewInserterSaveExample.class),
        };
    }
}
