package adinar.annotationsexample.viewinserter;


import adinar.annotationsexample.R;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

public class Person {

    @InsertTo(id = R.id.name)
    private String name;

    @InsertTo(id = R.id.surname)
    private String surname;

    @InsertTo(id = R.id.age, asString = true)
    private int age;

    @InsertTo(id = R.id.full_name)
    public String getFullName() {
        return String.format("%s %s", name, surname);
    }

    public Person(String name, String surname, int age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }
}
