package adinar.annotationsutils.objectdialog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract class DialogTitleEntry<T> {
    public abstract String getTitleFrom(T object);

    public static DialogTitleEntry titleFromString(final String title) {
        return new DialogTitleEntry() {
            @Override
            public String getTitleFrom(Object object) {
                return title;
            }
        };
    }

    public static<T> DialogTitleEntry<T> titleFromMethod(final Method m) {
        return new DialogTitleEntry<T>() {
            @Override
            public String getTitleFrom(T object) {
                try {
                    return String.valueOf(m.invoke(object));
                } catch (IllegalAccessException e) {
                    throw new InvalidDialogAccessException(m.getName());
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    public static<T> DialogTitleEntry<T> titleFromField(final Field f) {
        return new DialogTitleEntry<T>() {
            @Override
            public String getTitleFrom(T object) {
                try {
                    return String.valueOf(f.get(object));
                } catch (IllegalAccessException e) {
                    throw new InvalidDialogAccessException(f.getName());
                }
            }
        };
    }

    public static class InvalidDialogAccessException extends RuntimeException {
        public InvalidDialogAccessException(String name) {
            super(String.format("Dialog processor has no access to %s.", name));
        }
    }
}
