package com.axibase.tsd.api.method.series;

import com.axibase.tsd.api.model.series.Query;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVInsertTest extends CSVInsertMethod {

    @BeforeClass
    public static void setUpBeforeClass() {
        prepare();
    }

    @Test
    public void testISOFormat() throws Exception {
        String entity = "e-iso-2";
        String metric = "m-iso-2";
        Map tags = new HashMap<String, String>();
        tags.put("key-1", "value-1");
        tags.put("key-2", "value-2");

        csvInsert(entity, "date,m-iso-2\n2016-05-21T00:00:00Z,12.45\n2016-05-21T00:00:15+00:00,10.8\n", tags);

        Query query = new Query(entity, metric, "2016-05-21T00:00:00Z", "2016-05-21T00:00:10Z");
        executeQuery(query);
        Assert.assertEquals("Stored date incorrect", "2016-05-21T00:00:00.000Z", getDataField(0, "d"));
        Assert.assertEquals("Stored value incorrect", "12.45", getDataField(0, "v"));

        query = new Query(entity, metric, "2016-05-21T00:00:15Z", "2016-05-21T00:00:20Z");
        executeQuery(query);
        Assert.assertEquals("Stored date incorrect", "2016-05-21T00:00:15.000Z", getDataField(0, "d"));
        Assert.assertEquals("Stored value incorrect", "10.8", getDataField(0, "v"));

        Assert.assertTrue("Failed to delete metric", deleteMetric(metric));

    }
}
