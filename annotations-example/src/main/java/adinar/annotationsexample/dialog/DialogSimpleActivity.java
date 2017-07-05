package adinar.annotationsexample.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adinar.annotationsexample.ActivityWithFab;
import adinar.annotationsexample.R;
import adinar.annotationsutils.objectdialog.DialogProcessor;
import adinar.annotationsutils.objectdialog.SimpleAnnotationDialogListener;
import adinar.annotationsutils.viewinserter.ViewInserterArrayAdapter;

public class DialogExampleActivity extends ActivityWithFab {
    private static final String TAG = "DialogExampleActivity";
    private List<Person> data;
    private DialogProcessor dialogProcessor;
    private ArrayAdapter<Person> adapter;

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.main_layout);
        data = new ArrayList<>();
        dialogProcessor = new DialogProcessor(this);

        initListView();
    }

    @Override
    protected void onFabClick() {
        final Person person = new Person();
        Dialog dialog = dialogProcessor.createDialogFrom(person,
                new SimpleAnnotationDialogListener<Person>() {
            @Override
            public void onDialogAccepted(Person person) {
                data.add(person);
                adapter.notifyDataSetChanged();
            }
        });

        dialog.show();
    }

    private void initListView() {
        ListView lv = (ListView) findViewById(R.id.example_list);
        adapter = new ViewInserterArrayAdapter<>(this, R.layout.person_layout, data);
        lv.setAdapter(adapter);
    }
}
