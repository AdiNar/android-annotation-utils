package adinar.annotationsutils.objectdialog;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adinar.annotationsutils.objectdialog.annotations.DialogButton;
import adinar.annotationsutils.objectdialog.annotations.DialogClass;
import adinar.annotationsutils.objectdialog.annotations.DialogEditText;
import adinar.annotationsutils.objectdialog.annotations.DialogTitle;

/** Responsible for parsing all annotations and supplying data to dialog. */
class DialogClassData<T> {
    private static final String TAG = "DialogClassData";
    private List<DialogFieldEntry<T>> fields = new ArrayList<>();
    private List<DialogButton> neutralButtons;
    private DialogButton positive, negative;
    private DialogTitleEntry<T> title;
    private Context ctx;

    public DialogClassData(Class<T> clazz, Context ctx) {
        this.ctx = ctx.getApplicationContext();

        // Class must have DialogClass annotation.
        if (clazz.isAnnotationPresent(DialogClass.class)) {
            applyClassSettings(clazz);
        } else {
            throw new InvalidDialogClassException(clazz);
        }

        extractFields(clazz);
        if (title == null) {
            extractTitleFromMethods(clazz);
            extractTitleFromFields(clazz);
        }
    }

    private void applyClassSettings(Class<T> clazz) {
        DialogClass ann = clazz.getAnnotation(DialogClass.class);

        extractTitleFromClass(ann);
        extractButtons(ann);
    }

    private void extractButtons(DialogClass ann) {
        neutralButtons = new ArrayList<>();
        positive = negative = null;

        /** Assert only at most one Positive and Negative button is present. */
        for (DialogButton button : ann.buttons()) {
            if (button.type() == DialogButton.ButtonType.POSITIVE) {
                if (positive != null) throw new DuplicatedButtonException(
                        DialogButton.ButtonType.POSITIVE);
                positive = button;
            } else
            if (button.type() == DialogButton.ButtonType.NEGATIVE) {
                if (negative != null) throw new DuplicatedButtonException(
                        DialogButton.ButtonType.NEGATIVE);
                negative = button;
            } else {
                neutralButtons.add(button);
            }
        }
    }

    private void extractTitleFromClass(DialogClass ann) {
        String titleString = ann.title();

        if (titleString.length() == 0 && ann.titleId() != 0) {
            titleString = ctx.getString(ann.titleId());
        }

        if (titleString.length() != 0) {
            title = DialogTitleEntry.titleFromString(titleString);
        }
    }

    /** Create {@link DialogFieldEntry} for every field that has proper annotation. */
    private void extractFields(Class<T> clazz) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(DialogEditText.class)) {
                fields.add(new DialogEditTextFieldEntry(f, ctx));
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

    /** Extract title from methods if it's not specified in {@link DialogClass}. */
    private void extractTitleFromMethods(Class<T> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            DialogTitle ann = m.getAnnotation(DialogTitle.class);
            if (ann != null) {
                title = DialogTitleEntry.titleFromMethod(m);
                return;
            }
        }
    }

    /** Extract title from fields if it's not specified in {@link DialogClass}. */
    private void extractTitleFromFields(Class<T> clazz) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(DialogTitle.class)) {
                if (title == null) title = DialogTitleEntry.titleFromField(f);
            }
        }
    }

    private static class InvalidDialogClassException extends RuntimeException {
        InvalidDialogClassException(Class clazz) {
            super(String.format("Class %s should be annotated with %s in " +
                    "order to create dialog.",
                    clazz.getSimpleName(),
                    DialogClass.class.getSimpleName()));
        }
    }

    private static class DuplicatedButtonException extends RuntimeException {
        public DuplicatedButtonException(DialogButton.ButtonType positive) {
            super(String.format("Only one button with type %s can be specified for dialog.",
                    positive));
        }
    }

    public DialogTitleEntry<T> getTitleEntry() {
        return title;
    }

    public List<DialogFieldEntry<T>> getFields() {
        return fields;
    }

    public List<DialogButton> getNeutralButtons() {
        return neutralButtons;
    }

    public DialogButton getPositiveButton() {
        return positive;
    }

    public DialogButton getNegativeButton() {
        return negative;
    }
}
