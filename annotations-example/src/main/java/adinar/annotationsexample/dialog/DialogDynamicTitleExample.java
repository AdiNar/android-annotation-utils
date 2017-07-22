package adinar.annotationsexample.dialog;


public class DialogDynamicTitleExample extends DialogSimpleActivity<Titled> {
    protected Class<Titled> getDataClass() {
        return Titled.class;
    }
}
