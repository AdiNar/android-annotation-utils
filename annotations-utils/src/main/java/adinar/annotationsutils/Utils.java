package adinar.annotationsutils;


import android.content.Context;

public class Utils {
    public static String getString(Context ctx, int id) {
        if (id == 0) return String.valueOf("");
        return ctx.getString(id);
    }
}
