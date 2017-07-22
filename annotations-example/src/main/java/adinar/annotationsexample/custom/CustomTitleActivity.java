package adinar.annotationsexample.custom;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import adinar.annotationsexample.R;

public class CustomTitleActivity extends AppCompatActivity {
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.main_layout);
        setTitle(getCustomTitle());
    }

    protected String getCustomTitle() {
        return getIntent().getStringExtra("TITLE");
    }
}
