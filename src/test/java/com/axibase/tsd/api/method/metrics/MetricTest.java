package com.axibase.tsd.api.method.metrics;

import com.axibase.tsd.api.model.series.DataType;
import com.axibase.tsd.api.model.series.Metric;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MetricTest extends MetricMethod {

    @BeforeClass
    public static void setUpBeforeClass() {
        prepare();
    }

    @Test
    public void testCreateOrReplaceMetric() throws Exception {

        Metric metric = new Metric("m-create-or-replace");
        metric.setDataType(DataType.DECIMAL);

        Assert.assertTrue("Failed to insert create or replace metric", createOrReplaceMetric(metric));
        Assert.assertTrue(getMetric(metric));
        Assert.assertEquals(metric.getDataType(), getMetricField("dataType"));
    }
}
