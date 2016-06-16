package com.axibase.tsd.api.method.series;

import com.axibase.tsd.api.method.metrics.MetricMethod;
import com.axibase.tsd.api.model.metric.Metric;
import com.axibase.tsd.api.model.series.DataType;
import com.axibase.tsd.api.model.series.Sample;
import com.axibase.tsd.api.model.series.Series;
import com.axibase.tsd.api.model.series.SeriesQuery;
import junit.framework.Assert;
import org.junit.Test;


public class SeriesInsertTest extends SeriesMethod {
    /*
    * #2871
    * */
    @Test
    public void testBigFloatOverflow() throws Exception {

        String entityName = "e-float-1";
        String metricName = "m-float-1";
        String largeNumber = "10.121212121212121212212121212121212121212121";
        final long t = 1465485524888l;
        MetricMethod metricMethod = new MetricMethod();

        Series series = new Series(entityName, metricName);
        series.addData(new Sample(t, largeNumber));

        Metric metric = new Metric(metricName);
        metric.setDataType(DataType.FLOAT);

        Assert.assertTrue("Failed to create metric", metricMethod.createOrReplaceMetric(metric));
        Assert.assertTrue("Failed to insert float series", insertSeries(series));

        SeriesQuery seriesQuery = new SeriesQuery(series.getEntity(), series.getMetricName(), t, t + 1);
        executeQuery(seriesQuery);
        Assert.assertEquals("Stored big float value rounded incorrect", "10.121212121212121", getDataField(0, "v"));
    }

    /*
    * #2871
    * */
    @Test
    public void testBigDecimalOverflow() throws Exception {

        String entityName = "e-decimal-1";
        String metricName = "m-decimal-1";
        String largeNumber = "10.121212121212121212212121212121212121212121";
        final long t = 1465485524888l;
        MetricMethod metricMethod = new MetricMethod();


        Series series = new Series(entityName, metricName);
        series.addData(new Sample(t, largeNumber));

        Metric metric = new Metric(metricName);
        metric.setDataType(DataType.DECIMAL);

        Assert.assertTrue("Failed to insert create or replace metric", metricMethod.createOrReplaceMetric(metric));
        Assert.assertFalse("Managed to insert large decimal series", insertSeries(series));
    }

    /*
    * #2871
    * */
    @Test
    public void testBigDecimalPrecision() throws Exception {

        String entityName = "e-decimal-2";
        String metricName = "m-decimal-2";
        String number = "1.3477777777777777";
        final long t = 1465485524888l;
        MetricMethod metricMethod = new MetricMethod();

        Metric metric = new Metric(metricName);
        metric.setDataType(DataType.DECIMAL);

        Assert.assertTrue("Failed to insert create or replace metric", metricMethod.createOrReplaceMetric(metric));

        Series series = new Series(entityName, metricName);
        series.addData(new Sample(t, number));
        Assert.assertTrue("Failed to insert small decimal series", insertSeries(series));

        SeriesQuery seriesQuery = new SeriesQuery(series.getEntity(), series.getMetricName(), t, t + 1);
        executeQuery(seriesQuery);
        Assert.assertEquals("Stored small decimal value incorrect", number, getDataField(0, "v"));
    }

    /*
    * #2009
    * */
    @Test
    public void testISOFormatsZmsAbsent() throws Exception {

        String entityName = "e-iso-1";
        String metricName = "m-iso-1";
        String value = "0";

        String storedDate = "2016-06-09T17:08:09.000Z";
        Series series = new Series(entityName, metricName);
        String d = "2016-06-09T17:08:09Z";
        series.addData(new Sample(d, value));

        Assert.assertTrue("Failed to insert series", insertSeries(series));

        SeriesQuery seriesQuery = new SeriesQuery(series.getEntity(), series.getMetricName(), d, "2016-06-09T17:08:09.001Z");
        executeQuery(seriesQuery);
        Assert.assertEquals("Stored date incorrect", storedDate, getDataField(0, "d"));
        Assert.assertEquals("Stored value incorrect", value, getDataField(0, "v"));
    }

    /*
    * #2009
    * */
    @Test
    public void testISOFormatsZms() throws Exception {

        String entityName = "e-iso-2";
        String metricName = "m-iso-2";
        String value = "0";

        String storedDate = "2016-06-09T17:08:09.100Z";
        Series series = new Series(entityName, metricName);
        String d = "2016-06-09T17:08:09.100Z";
        series.addData(new Sample(d, value));

        Assert.assertTrue("Failed to insert series", insertSeries(series));

        SeriesQuery seriesQuery = new SeriesQuery(series.getEntity(), series.getMetricName(), d, "2016-06-09T17:08:09.101Z");
        executeQuery(seriesQuery);
        Assert.assertEquals("Stored date incorrect", storedDate, getDataField(0, "d"));
        Assert.assertEquals("Stored value incorrect", value, getDataField(0, "v"));
    }

    /*
    * #2009
    * */
    @Test
    public void testISOFormatsPlusHoursNoMS() throws Exception {

        String entityName = "e-iso-3";
        String metricName = "m-iso-3";
        String value = "0";

        Series series = new Series(entityName, metricName);
        String d = "2016-06-09T10:08:09.000Z";
        series.addData(new Sample("2016-06-09T17:08:09+07:00", value));

        Assert.assertTrue("Failed to insert series", insertSeries(series));

        SeriesQuery seriesQuery = new SeriesQuery(series.getEntity(), series.getMetricName(), d, "2016-06-09T10:08:09.100Z");
        executeQuery(seriesQuery);
        Assert.assertEquals("Stored date incorrect", d, getDataField(0, "d"));
        Assert.assertEquals("Stored value incorrect", value, getDataField(0, "v"));
    }

    /*
    * #2009
    * */
    @Test
    public void testISOFormatsPlusHoursMS() throws Exception {

        String entityName = "e-iso-4";
        String metricName = "m-iso-4";
        String value = "0";

        Series series = new Series(entityName, metricName);
        String d = "2016-06-09T10:08:09.999Z";
        series.addData(new Sample("2016-06-09T17:08:09.999+07:00", value));

        Assert.assertTrue("Failed to insert series", insertSeries(series));

        SeriesQuery seriesQuery = new SeriesQuery(series.getEntity(), series.getMetricName(), d, "2016-06-09T10:08:10Z");
        executeQuery(seriesQuery);
        Assert.assertEquals("Stored date incorrect", d, getDataField(0, "d"));
        Assert.assertEquals("Stored value incorrect", value, getDataField(0, "v"));
    }
}
