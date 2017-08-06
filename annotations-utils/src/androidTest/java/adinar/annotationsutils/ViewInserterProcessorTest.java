package adinar.annotationsutils;


import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import adinar.annotationsutils.viewinserter.MethodResolver;
import adinar.annotationsutils.viewinserter.ViewInserterProcessor;
import adinar.annotationsutils.viewinserter.annotations.InsertTo;
import adinar.annotationsutils.viewinserter.annotations.InsertToClass;

import static android.support.test.InstrumentationRegistry.getContext;

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

    @Test
    public void testCustomMethod() {
        AdvancedTestClass obj = new AdvancedTestClass();

        View view = LayoutInflater.from(getContext())
            .inflate(R.layout.test_view_inserter_processor_advanced, null);

        ViewInserterProcessor.insertInto(view, obj);

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
        Assert.assertEquals((int) obj.seekBarMax, seekBar.getMax());
        Assert.assertEquals((int) obj.seekBarProgress, seekBar.getProgress());
    }

    @Test
    public void testSaveToObject() {
        SaveTestClass obj = new SaveTestClass();

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.test_view_inserter_processor_save, null);

        ViewInserterProcessor.insertInto(view, obj);

        TextView tv = (TextView) view.findViewById(R.id.save1);

        tv.setText("1234");
        tv.setHint("What a hint!");
        tv.setEnabled(false);

        ProgressBar pb = (ProgressBar) view.findViewById(R.id.save2IntegerValue);
        pb.setMax(1000);
        pb.setProgress(1000);

        tv = (TextView) view.findViewById(R.id.non_save1);
        tv.setText("Should not be saved");
        tv = (TextView) view.findViewById(R.id.non_save2);
        tv.setText("This one also");

        ViewInserterProcessor.saveFrom(view, obj);

        Assert.assertEquals("1234", obj.save1);
        Assert.assertEquals(Integer.valueOf(1234), obj.save1AsString);
        Assert.assertEquals(1234, obj.save1AsStringPrimitive);
        Assert.assertEquals("What a hint!", obj.save1SaveHint);
        Assert.assertTrue(obj.save1WithMethod > 5);
        Assert.assertEquals(1, obj.nonSave1);
        Assert.assertEquals("not_saved", obj.nonSave2);
        Assert.assertEquals(Integer.valueOf(1000), obj.save2IntegerValue);
    }

    @Test
    public void testInheritedAnnotations() {
        InheritanceTestClass obj = new InheritanceTestClass();

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.test_view_inserter_processor, null);

        ViewInserterProcessor.insertInto(view, obj);

        TextView tv = (TextView) view.findViewById(R.id.string1);
        Assert.assertEquals(obj.string1, tv.getText());

        tv = (TextView) view.findViewById(R.id.int1);
        Assert.assertEquals(String.valueOf(obj.overrideInt), tv.getText());

        tv = (TextView) view.findViewById(R.id.int2);
        Assert.assertEquals(String.valueOf(obj.int2), tv.getText());
    }

    @Test
    public void testNoInheritanceWhenDisabled() {
        NoInheritanceTestClass obj = new NoInheritanceTestClass();

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.test_view_inserter_processor, null);

        ViewInserterProcessor.insertInto(view, obj);

        TextView tv = (TextView) view.findViewById(R.id.string1);
        Assert.assertFalse("string1".equals(tv.getText()));

        tv = (TextView) view.findViewById(R.id.int1);
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
    Double int1 = 1.0;
}

class AdvancedTestClass {
    @InsertTo(id = R.id.seekBar1, method = "setProgress")
    Integer seekBarProgress = 34;

    @InsertTo(id = R.id.seekBar1, method = "setMax")
    Integer seekBarMax = 200;
}

class SaveTestClass {
    @InsertTo(id = R.id.save1, save = @InsertTo.AllowSave)
    String save1;

    @InsertTo(id = R.id.save1, save = @InsertTo.AllowSave(viewMethodName = "getHint"))
    String save1SaveHint;

    @InsertTo(id = R.id.save1, asString = true, save = @InsertTo.AllowSave)
    Integer save1AsString;

    @InsertTo(id = R.id.save1, asString = true, save = @InsertTo.AllowSave)
    int save1AsStringPrimitive;

    @InsertTo(id = R.id.save1, asString = true,
            save = @InsertTo.AllowSave(
                    saveMethodName = "save1Method",
                    saveMethodArgument = boolean.class,
                    viewMethodName = "isEnabled"))
    Double save1WithMethod;

    public void save1Method(boolean value) {
        save1WithMethod = value ? -10.0 : 10.0;
    }

    @InsertTo(id = R.id.save2IntegerValue, method = "setProgress",
            save = @InsertTo.AllowSave(viewMethodName = "getProgress"))
    Integer save2IntegerValue = 10;

    @InsertTo(id = R.id.non_save1, asString = true)
    int nonSave1 = 1;

    @InsertTo(id = R.id.non_save2)
    String nonSave2 = "not_saved";
}

class InheritanceTestClass extends TestClass {
    @InsertTo(id = R.id.int2, asString = true)
    int int2 = 2;

    @InsertTo(id = R.id.int1, asString = true)
    int overrideInt = 3;
}

@InsertToClass(withSuper = false)
class NoInheritanceTestClass extends TestClass {
    @InsertTo(id = R.id.int1, asString = true)
    int int1 = 10;
}