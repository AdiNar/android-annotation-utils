package adinar.annotationsexample.viewinserter;


import adinar.annotationsexample.common.ExampleListActivity;

public class ViewInserterExamplesList  extends ExampleListActivity {

    @Override
    protected Entry[] getEntries() {
        return new Entry[] {
            new Entry("Simple", ViewInserterSimpleExample.class),
            new Entry("Save", ViewInserterSaveExample.class),
            new Entry("Inheritance", ViewInserterInheritanceExample.class),
        };
    }
}
