package adinar.annotationsexample.dialog;


import adinar.annotationsexample.common.ExampleListActivity;

public class DialogExamplesList extends ExampleListActivity {

    @Override
    protected Entry[] getEntries() {
        return new Entry[]{
                new Entry("Simple", DialogSimpleActivity.class),
                new Entry("Validators and primitives", DialogValidatorsExample.class),
                new Entry("Buttons", DialogButtonsExample.class),
                new Entry("Dynamic title", DialogDynamicTitleExample.class)
        };
    }
}
