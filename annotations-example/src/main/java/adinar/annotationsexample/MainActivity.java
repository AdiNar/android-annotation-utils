package adinar.annotationsexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import adinar.annotationsexample.dialog.DialogExampleActivity;

public class MainActivity extends AppCompatActivity {

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.main_layout);

        initExamples();
    }

    private void initExamples() {
        final Entry[] entries = {new Entry("Dialog example", DialogExampleActivity.class)};
        ListView lv = (ListView) findViewById(R.id.example_list);

        ArrayAdapter<Entry> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                entries
        );

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                startActivity(new Intent(MainActivity.this, entries[pos].activity));
            }
        });
    }

    private class Entry {
        String name;
        Class<? extends Activity> activity;

        Entry(String name, Class<? extends Activity> activity) {
            this.name = name;
            this.activity = activity;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
