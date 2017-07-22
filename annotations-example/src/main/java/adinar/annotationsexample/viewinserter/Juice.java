package adinar.annotationsexample.viewinserter;


import adinar.annotationsexample.R;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

public class Juice {
    public Juice() {
        taste = "orange";
        size = 500;
        description = "Very good juice!";
    }

    @InsertTo(id = R.id.taste, save = @InsertTo.AllowSave)
    private String taste;

    @InsertTo(id = R.id.size, asString = true, save = @InsertTo.AllowSave)
    private Integer size;

    @InsertTo(id = R.id.description, save = @InsertTo.AllowSave(saveMethodName = "saveDescription"))
    private String description;

    public void saveDescription(String desc) {
        description = String.format(">>>>>> %s <<<<<<", desc);
    }
}
