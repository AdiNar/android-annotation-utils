package adinar.annotationsexample.viewinserter;

import adinar.annotationsexample.R;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;
import adinar.annotationsutils.viewinserter.annotations.InsertToClass;

@InsertToClass(withSuper = false)
public class NotInheritedData extends Data {
    @InsertTo(id = R.id.content)
    private String content = "This class contains only this field and it's view mapping " +
            "also, no fields from superclass were used in view.";
}
