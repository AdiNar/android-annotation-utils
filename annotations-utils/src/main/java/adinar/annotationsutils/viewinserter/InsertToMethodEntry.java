package adinar.annotationsutils.viewinserter;


import java.lang.reflect.Method;

import adinar.annotationsutils.common.MethodEntry;

class InsertToMethodEntry extends MethodEntry {
    public static final String TAG = "InsertToMethodEntry";

    public InsertToMethodEntry(Method m) {
        super(m);
    }
}
