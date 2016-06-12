package com.axibase.tsd.api.method.series;

import com.axibase.tsd.api.model.series.Metric;
import com.axibase.tsd.api.model.series.Query;
import com.axibase.tsd.api.model.series.Sample;
import com.axibase.tsd.api.model.series.Series;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

public class SeriesInsertTest extends SeriesMethod {

    private String ENTITY = "my_entity";
    private String METRIC = "my_metric";
    private String SMALL_NUMBER = "1.12";
    private String LARGE_NUMBER = "10.121212121212121212212121212121212121212121";
    private final long t = 1465485524888l;

    @BeforeClass
    public static void setUpBeforeClass() {
        prepare();
    }

    @Test
    public void testBigFloatOverflow() throws Exception {

        Series series = new Series("decimal_precision", METRIC);
        series.addData(new Sample(t, new BigDecimal(LARGE_NUMBER)));
        series.addTag("key", "value");

        Metric metric = new Metric(METRIC);
        metric.setDataType("FLOAT");

        Assert.assertTrue("Failed to create metric", createOrReplaceMetric(metric));
        Assert.assertTrue("Failed to insert float series", insertSeries(series));

        Query query = new Query(series.getEntity(), series.getMetricName(), t - 1000, t + 1000);
        executeQuery(query);
        Assert.assertEquals("Stored big float value rounded incorrect", "10.121212121212121", getDataField(0, "v"));

        series.setData(new Sample(t, new BigDecimal(SMALL_NUMBER)));
        Assert.assertTrue("Failed to insert small float value series", insertSeries(series));

        query = new Query(series.getEntity(), series.getMetricName(), t - 1000, t + 1000);
        executeQuery(query);
        Assert.assertEquals("Stored small float value incorrect", SMALL_NUMBER, getDataField(0, "v"));

        Assert.assertTrue("Failed to delete metric", deleteMetric(metric));
    }

    @Test
    public void testBigDecimalOverflow() throws Exception {

        Series series = new Series(ENTITY, METRIC);
        series.addData(new Sample(t, new BigDecimal(LARGE_NUMBER)));
        series.addTag("key", "value");

        Metric metric = new Metric(METRIC);
        metric.setDataType("DECIMAL");

        Assert.assertTrue("Failed to insert create or replace metric", createOrReplaceMetric(metric));
        Assert.assertFalse("Managed to insert large decimal series", insertSeries(series));

        series.setData(new Sample(t, new BigDecimal(SMALL_NUMBER)));
        Assert.assertTrue("Failed to insert small decimal series", insertSeries(series));

        Query query = new Query(series.getEntity(), series.getMetricName(), t - 1000, t + 1000);
        executeQuery(query);
        Assert.assertEquals("Stored small decimal value incorrect", SMALL_NUMBER, getDataField(0, "v"));

        Assert.assertTrue("Failed to delete metric", deleteMetric(metric));
    }

    @Test
    public void testISOFormats() throws Exception {

        String storedDate = "2016-06-09T17:08:09.000Z";
        Series series = new Series(ENTITY, METRIC);
        series.addData(new Sample("2016-06-09T17:08:09Z", new BigDecimal(SMALL_NUMBER)));
        series.addTag("key", "value");

        Assert.assertTrue("Failed to insert iso format", insertSeries(series));

        Query query = new Query(series.getEntity(), series.getMetricName(), "2016-06-09T17:08:00Z", "2016-06-09T17:08:10Z");
        executeQuery(query);
        Assert.assertEquals("Stored date incorrect", storedDate, getDataField(0, "d"));
        Assert.assertEquals("Stored value incorrect", SMALL_NUMBER, getDataField(0, "v"));

        Assert.assertTrue("Failed to delete metric", deleteMetric(series.getMetricName()));


        series.setData(new Sample("2016-06-09T17:08:09+00:00", new BigDecimal(SMALL_NUMBER)));
        Assert.assertTrue("Failed to insert iso format", insertSeries(series));

        query = new Query(series.getEntity(), series.getMetricName(), "2016-06-09T17:08:00Z", "2016-06-09T17:08:10Z");
        executeQuery(query);
        Assert.assertEquals("Stored date incorrect", storedDate, getDataField(0, "d"));
        Assert.assertEquals("Stored value incorrect", SMALL_NUMBER, getDataField(0, "v"));

        Assert.assertTrue("Failed to delete metric", deleteMetric(series.getMetricName()));
    }

    @Test
    public void testCreateOrReplaceMetric() throws Exception {

        Metric metric = new Metric(METRIC);
        metric.setDataType("DECIMAL");

        Assert.assertTrue("Failed to insert create or replace metric", createOrReplaceMetric(metric));
        Assert.assertTrue("Failed to delete metric", deleteMetric(metric));
    }
}
