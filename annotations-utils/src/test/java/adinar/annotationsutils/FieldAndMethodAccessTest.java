package adinar.annotationsutils;


import junit.framework.Assert;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import adinar.annotationsutils.common.FieldAndMethodAccess;

import static org.junit.Assert.fail;

public class FieldAndMethodAccessTest {
    @Test
    public void testField() throws NoSuchFieldException {
        Field field;

        TestObject obj = new TestObject();

        field = TestObject.class.getDeclaredField("publicField");
        try {
            Assert.assertEquals("field", FieldAndMethodAccess.getFieldValue(field, obj));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("getFieldValue should read value from private field.");
        }

        field = TestObject.class.getDeclaredField("privateField");
        try {
            Assert.assertEquals(1, FieldAndMethodAccess.getFieldValue(field, obj));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("getFieldValue should read value from private field.");
        }

        try {
            Assert.assertEquals("1", FieldAndMethodAccess.getStringFieldValue(field, obj));
        } catch (Exception e) {
            e.printStackTrace();
            fail("getStringFieldValue should read value from private field.");
        }
    }

    @Test
    public void testMethod() throws NoSuchFieldException, NoSuchMethodException {
        Method method;

        TestObject obj = new TestObject();

        method = TestObject.class.getDeclaredMethod("publicMethod");
        try {
            Assert.assertEquals(2, FieldAndMethodAccess.getMethodValue(method, obj));
        } catch (Exception e) {
            fail("getFieldValue should read value from public field.");
        }

        method = TestObject.class.getDeclaredMethod("privateMethod");
        try {
            Assert.assertEquals(1, FieldAndMethodAccess.getMethodValue(method, obj));
        } catch (Exception e) {
            e.printStackTrace();
            fail("getFieldValue should read value from private field.");
        }

        try {
            Assert.assertEquals("1", FieldAndMethodAccess.getStringMethodValue(method, obj));
        } catch (Exception e) {
            e.printStackTrace();
            fail("getStringFieldValue should read value from private field.");
        }
    }
}

class TestObject {
    private int privateField = 1;
    public String publicField = "field";

    private Integer privateMethod() {
        return 1;
    }

    public int publicMethod() {
        return 2;
    }
}