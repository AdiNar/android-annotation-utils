package adinar.annotationsexample.common;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import adinar.annotationsexample.R;

public abstract class ExampleListActivity extends CustomTitleActivity {
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.main_layout);
        setTitle(getCustomTitle());

        initExamples();
    }

    private void initExamples() {
        final Entry[] entries = getEntries();
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
                Intent intent = new Intent(ExampleListActivity.this, entries[pos].activity);
                intent.putExtra("TITLE", entries[pos].name);
                startActivity(intent);
            }
        });
    }

    protected class Entry {
        String name;
        Class<? extends Activity> activity;

        public Entry(String name, Class<? extends Activity> activity) {
            this.name = name;
            this.activity = activity;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    protected abstract Entry[] getEntries();
}
