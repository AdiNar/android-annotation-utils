package adinar.annotationsexample.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adinar.annotationsexample.ActivityWithFab;
import adinar.annotationsexample.R;
import adinar.annotationsutils.objectdialog.DialogProcessor;
import adinar.annotationsutils.objectdialog.SimpleAnnotationDialogListener;
import adinar.annotationsutils.viewinserter.ViewInserterArrayAdapter;

public class DialogSimpleActivity<T> extends ActivityWithFab {
    private static final String TAG = "DialogSimpleActivity";
    private List<T> data;
    private DialogProcessor dialogProcessor;
    private ArrayAdapter<T> adapter;

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.main_layout);
        data = new ArrayList<>();
        dialogProcessor = new DialogProcessor(this);

        initListView();
    }

    protected Class<T> getDataClass() {
        return (Class<T>) Person.class;
    }

    @Override
    protected void onFabClick() {
        T obj = getDataInstance();

        Dialog dialog = getDialog(obj);

        dialog.show();
    }

    private Dialog getDialog(T obj) {
        return dialogProcessor.createDialogFrom(obj,
                    new SimpleAnnotationDialogListener<T>() {
                @Override
                public void onDialogAccepted(T person) {
                    if (adapter.getPosition(person) == -1) {
                        data.add(person);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
    }

    private T getDataInstance() {
        T obj = null;
        try {
            obj = getDataClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private void initListView() {
        ListView lv = (ListView) findViewById(R.id.example_list);
        adapter = new ViewInserterArrayAdapter<>(this, R.layout.person_layout, data);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDialog(adapter.getItem(position));
            }
        });
    }
}
