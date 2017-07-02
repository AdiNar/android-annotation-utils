package adinar.annotationsexample;


import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class ActivityWithFab extends AppCompatActivity {

    @Override
    public void setContentView(int id) {
        super.setContentView(id);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setVisibility(View.VISIBLE);

        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onFabClick();
            }
        });
    }

    protected abstract void onFabClick();
}
