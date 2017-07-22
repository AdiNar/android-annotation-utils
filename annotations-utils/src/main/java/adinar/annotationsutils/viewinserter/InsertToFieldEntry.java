package adinar.annotationsutils.viewinserter;


import java.lang.reflect.Field;

import adinar.annotationsutils.common.FieldEntry;

public class InsertToFieldEntry extends FieldEntry {
    public static final String TAG = "InsertToFieldEntry";

    public InsertToFieldEntry(Field f) {
        super(f);
    }
}
