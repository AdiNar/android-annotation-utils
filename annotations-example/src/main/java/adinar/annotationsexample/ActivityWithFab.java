package adinar.annotationsexample;


import android.support.design.widget.FloatingActionButton;
import android.view.View;
import adinar.annotationsexample.common.CustomTitleActivity;

public abstract class ActivityWithFab extends CustomTitleActivity {

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
