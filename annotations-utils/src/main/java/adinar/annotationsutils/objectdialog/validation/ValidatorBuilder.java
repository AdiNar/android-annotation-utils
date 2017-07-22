package adinar.annotationsutils.objectdialog.validation;


/**  */
public class ValidatorBuilder {
    private Validator first, current;

    public ValidatorBuilder() {
        first = current = new Validator() {
            @Override
            protected boolean isValidSingle() {
                return true;
            }

            @Override
            protected void setErrorMessageInView() {}

            @Override
            protected void hideErrorMessageInView() {}
        };
    }

    public ValidatorBuilder add(Validator val) {
        current.setNext(val);
        current = val;

        return this;
    }

    public Validator build() {
        // We want to return sth non-null, first is a dummy validator that is always true,
        // if there's a real validator next then cut this first one out.
        return first.getNext() != null ? first.getNext() : first;
    }
}