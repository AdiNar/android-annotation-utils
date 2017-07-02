package adinar.annotationsutils.objectdialog;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adinar.annotationsutils.objectdialog.annotations.DialogClass;
import adinar.annotationsutils.objectdialog.annotations.DialogField;
import adinar.annotationsutils.objectdialog.annotations.DialogTitle;

/** Responsible for parsing all annotations and supplying data to dialog. */
class DialogClassData<T> {
    private static final String TAG = "DialogClassData";
    private List<DialogFieldEntry<T>> fields = new ArrayList<>();
    private DialogTitleEntry<T> title;
    private Context ctx;

    public DialogClassData(Class<T> clazz, Context ctx) {
        this.ctx = ctx;

        if (clazz.isAnnotationPresent(DialogClass.class)) {
            applyClassSettings(clazz);
        } else {
            throw new InvalidDialogClassException(clazz);
        }

        extractFields(clazz);
        if (title == null) extractTitleFromMethods(clazz);
    }

    private void applyClassSettings(Class<T> clazz) {
        DialogClass ann = clazz.getAnnotation(DialogClass.class);

        String titleString = ann.title();

        if (titleString.length() == 0 && ann.titleId() != 0) {
            titleString = ctx.getString(ann.titleId());
        }

        if (titleString.length() != 0) {
            title = DialogTitleEntry.titleFromString(titleString);
        }
    }

    private void extractFields(Class<T> clazz) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(DialogField.class)) {
                fields.add(new DialogFieldEntry(f));
            } else {
                if (f.isAnnotationPresent(DialogTitle.class)) {
                    if (title == null) title = DialogTitleEntry.titleFromField(f);
                }
            }
        }
        
        sortFieldsByOrder();
    }

    private void sortFieldsByOrder() {
        Collections.sort(fields, new Comparator<DialogFieldEntry>() {
            @Override
            public int compare(DialogFieldEntry lhs, DialogFieldEntry rhs) {
                return Double.compare(lhs.getOrder(), rhs.getOrder());
            }
        });
    }

    private void extractTitleFromMethods(Class<T> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            DialogTitle ann = m.getAnnotation(DialogTitle.class);
            if (ann != null) {
                title = DialogTitleEntry.titleFromMethod(m);
                return;
            }
        }
    }

    public DialogTitleEntry<T> getTitleEntry() {
        return title;
    }

    public List<DialogFieldEntry<T>> getFields() {
        return fields;
    }

    private static class InvalidDialogClassException extends RuntimeException {
        InvalidDialogClassException(Class clazz) {
            super(String.format("Class %s should be annotated with %s in " +
                    "order to create dialog.",
                    clazz.getSimpleName(),
                    DialogClass.class.getSimpleName()));
        }
    }
}
