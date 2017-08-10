package adinar.annotationsutils.viewinserter;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Test;

import adinar.annotationsutils.R;
import adinar.annotationsutils.viewinserter.annotations.InsertEmbeddedTo;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

import static android.support.test.InstrumentationRegistry.getContext;

public class ViewInserterEmbeddedTest {

    @Test
    public void classCanBeInsertedFlat() {
        EmbeddedClass obj = new EmbeddedClass();

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.test_view_inserter_processor_embedded, null);

        ViewInserterProcessor.insertInto(view, obj);

        TextView tv = (TextView) view.findViewById(R.id.emb_string);
        Assert.assertEquals(obj.emb, tv.getText());
    }

    @Test
    public void classCanBeInsertedIntoNested() {
        MainTestClass obj = new MainTestClass();
        EmbeddedClassNested[] arr = {obj.nested1, obj.nested2};

        arr[0].embNested = "nested0";
        arr[1].embNested = "nested1";

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.test_view_inserter_processor_embedded, null);

        ViewInserterProcessor.insertInto(view, obj);

        testNested(arr[0], view, R.id.emb_nested1);
        testNested(arr[1], view, R.id.emb_nested2);
    }

    private void testNested(EmbeddedClassNested obj, View view, int layoutId) {
        LinearLayout layout = (LinearLayout) view.findViewById(layoutId);

        TextView tv = (TextView) layout.findViewById(R.id.emb_nested_string);
        Assert.assertEquals(String.valueOf(obj.embNested), tv.getText());
    }
}

class EmbeddedClass {
    @InsertTo(id = R.id.emb_string)
    String emb = "emb";
}

class EmbeddedClassNested {
    @InsertTo(id = R.id.emb_nested_string)
    String embNested = "embNested";
}

class MainTestClass {
    @InsertEmbeddedTo
    EmbeddedClass simple = new EmbeddedClass();

    @InsertEmbeddedTo(id = R.id.emb_nested1)
    EmbeddedClassNested nested1 = new EmbeddedClassNested();

    @InsertEmbeddedTo(id = R.id.emb_nested2)
    EmbeddedClassNested nested2 = new EmbeddedClassNested();
}
