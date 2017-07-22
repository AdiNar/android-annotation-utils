# Android Annotation Utils
Library contains view related annotations that I found helpful and not present in other libraries.
They're made to speed up the data transfer process between object and corresponding view by doing all repetitive work for you.

To use library just add
```
dependencies {
    compile 'adinar.annotationsutils:annotations-utils:0.9' (not ready yet)
}
```
to your build.gradle file.

# Short insight

## Example usage of ViewInserter.
Assume it's your data class:
```
public class Juice {
    @InsertTo(id = R.id.taste, save = @InsertTo.AllowSave)
    private String taste;

    @InsertTo(id = R.id.size, asString = true, save = @InsertTo.AllowSave)
    private Integer size;

    @InsertTo(id = R.id.mainIngredient, save = @InsertTo.AllowSave(saveMethodName = "saveMainIngredient"))
    private String mainIngredient;

    public void saveMainIngredient(String ingr) {
        mainIngredient = String.format(">>>>>> %s <<<<<<", ingr);
    }
}
```

Then just type in code 
`ViewInserterProcessor.insertInto(view, juice);`
to fill view with data from juice and
`ViewInserterProcessor.saveFrom(view, juice);`
to save view state in juice object.
You can also provide a listener for the view and many more, see examples app.

## Content list
1. View Inserter - provides methods that transfer data between view and object based on rules defined in annotations.
2. Object Dialog - provides methods that made create/edit dialogs for objects, with input validation and data saving (based on View Inserter).

## Examples
If you want to start using the library or see it in work, there's an example app in annotations-example/ with all the
useful stuff that it provides!

Documentation is not ready yet but I hope examples are clear enough to understand all usecases.
