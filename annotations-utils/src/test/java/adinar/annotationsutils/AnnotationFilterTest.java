package adinar.annotationsutils;


import junit.framework.Assert;

import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import adinar.annotationsutils.common.AnnotationFilter;


public class AnnotationFilterTest {

    @Test
    public void testFiltering() {
        AnnotationFilter filter = new AnnotationFilter(TestClass.class)
                .addAnnotation(AnAnnotation.class)
                .filter();

        Assert.assertEquals(2, filter.getFields().size());
        Assert.assertEquals(1, filter.getMethods().size());
        Assert.assertEquals(3, filter.getAllAnnotated().size());
    }

    @Test
    public void testEntries() {
        AnnotationFilter filter = new AnnotationFilter(TestClass.class)
                .addAnnotation(AnAnnotation.class)
                .filter();

        AnnotationFilter.Entry<Field> field = filter.getFields().get(0);
        Assert.assertNotNull(field.getAnn(AnAnnotation.class));
    }

    @Test
    public void testEmptyFilter() {
        AnnotationFilter filter = new AnnotationFilter(TestClass.class).filter();

        Assert.assertEquals(0, filter.getFields().size());
        Assert.assertEquals(0, filter.getMethods().size());
        Assert.assertEquals(0, filter.getAllAnnotated().size());
    }

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface AnAnnotation {}

    public static class TestClass {
        @AnAnnotation
        private String aYes;

        @AnAnnotation
        private int bYes;

        private String cNo;
        private double dNo;

        @AnAnnotation
        int eYes() {
            return 0;
        }

        int fNo() {
            return 0;
        }
    }
}
