package adinar.annotationsexample.dialog;

import adinar.annotationsexample.R;
import adinar.annotationsutils.objectdialog.annotations.DialogClass;
import adinar.annotationsutils.objectdialog.annotations.DialogField;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

@DialogClass(title = "Person")
public class Person {
    @InsertTo(id = R.id.name, asString = true)
    @DialogField(order = 1)
    String name = "test";

    @InsertTo(id = R.id.contact_phone, asString = true)
    @DialogField(name = "Phone", order=2)
    String contactPhone = "aaa";
}
