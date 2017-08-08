package adinar.annotationsexample.viewinserter;


import adinar.annotationsexample.R;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

public class InheritedData extends Data {
    @InsertTo(id = R.id.content)
    private String content = "This class contains only this field but it's mapping to view " +
            "has also all fields from superclass.";
}
