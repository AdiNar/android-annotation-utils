package adinar.annotationsutils;


import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import adinar.annotationsutils.viewinserter.MethodResolver;
import adinar.annotationsutils.viewinserter.ViewInserterProcessor;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;

import static android.support.test.InstrumentationRegistry.getContext;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class ViewInserterProcessorTest {

    @Test
    public void testViewInsertTo() {
        TestClass obj = new TestClass();

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.test_view_inserter_processor, null);

        ViewInserterProcessor.insertInto(view, obj);

        TextView tv = (TextView) view.findViewById(R.id.string1);
        Assert.assertEquals(obj.string1, tv.getText());

        tv = (TextView) view.findViewById(R.id.int1);
        Assert.assertEquals(String.valueOf(obj.int1), tv.getText());
    }

    @Test(expected = MethodResolver.NoSuchMethodExceptionRuntime.class)
    public void testViewInsertToError() {
        FailTestClass obj = new FailTestClass();

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.test_view_inserter_processor, null);

        ViewInserterProcessor.insertInto(view, obj);

        TextView tv = (TextView) view.findViewById(R.id.int1);
        Assert.assertEquals(String.valueOf(obj.int1), tv.getText());
    }
}

class TestClass {
    @InsertTo(id = R.id.string1)
    String string1 = "string1";

    @InsertTo(id = R.id.int1, asString = true)
    Integer int1 = 1;
}

class FailTestClass {
    // This is wrong, no matching method.
    @InsertTo(id = R.id.int1)
    Integer int1 = 1;
}
