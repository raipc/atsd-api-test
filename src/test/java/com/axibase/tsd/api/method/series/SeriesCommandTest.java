package com.axibase.tsd.api.method.series;

import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.model.series.Sample;
import com.axibase.tsd.api.model.series.Series;
import com.axibase.tsd.api.model.series.SeriesQuery;
import junit.framework.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.ArrayList;

public class SeriesCommandTest extends SeriesMethod {
    /* #2412 */
    @Test
    public void testMaxLength() throws Exception {
        final int MAX_LENGTH = 128 * 1024;
        final int METRICS_COUNT = 10;
        final int METRIC_PREFIX_LENGTH = 13097 - 2;

        String entityName = "e-series-max-len";
        String metricPrefix = Util.generateStringFromChar('m', METRIC_PREFIX_LENGTH) + "-";
        String date = "2016-05-21T00:00:00.000Z";
        String endDate = "2016-05-21T00:00:00.001Z";

        StringBuilder sb = new StringBuilder("series");
        sb.append(" e:").append(entityName);
        sb.append(" d:").append(date);
        for (int i = 0; i < METRICS_COUNT; i++) {
            sb.append(" m:").append(metricPrefix).append(i).append("=").append(i);
        }
        if (MAX_LENGTH != sb.length()) {
            Assert.fail("Command length is not maximal");
        }
        tcpSender.send(sb.toString());

        ArrayList<SeriesQuery> seriesQueries = new ArrayList<>();
        final ArrayList<Series> seriesList = new ArrayList<>();
        for (int i = 0; i < METRICS_COUNT; i++) {
            Series series = new Series(entityName, metricPrefix + i);
            series.setData(new Sample(date, i));
            seriesList.add(series);
            seriesQueries.add(new SeriesQuery(entityName, metricPrefix + i, date, endDate));
        }
        if (!executeQuery(seriesQueries)) {
            Assert.fail("Failed to insert series");
        }

        String storedSeries = getReturnedSeries().toString();
        String sentSeries = jacksonMapper.writeValueAsString(seriesList);

        JSONAssert.assertEquals(sentSeries, storedSeries, JSONCompareMode.LENIENT);
    }
}
