package com.axibase.tsd.api.method.series;

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

        String entityName = "e-series-max-cmd-len";
        String metricPrefix = "m-series-max-cmd-length-";
        String date = "2016-05-21T00:00:00.000Z";
        String endDate = "2016-05-21T00:00:00.001Z";

        StringBuilder sb = new StringBuilder("series");
        sb.append(" e:").append(entityName);
        sb.append(" d:").append(date);

        int i;
        for (i = 0; sb.length() < MAX_LENGTH; i++) {
            sb.append(" m:").append(metricPrefix).append(i).append("=").append(i);
        }

        int metricsCount = i;

        Assert.assertEquals("Command length is not maximal", MAX_LENGTH, sb.length());
        tcpSender.send(sb.toString());

        ArrayList<SeriesQuery> seriesQueries = new ArrayList<>();
        final ArrayList<Series> seriesList = new ArrayList<>();
        for (i = 0; i < metricsCount; i++) {
            Series series = new Series();
            series.setEntity(entityName);
            series.setMetric(metricPrefix + i);
            series.setData(new Sample(date, i));
            seriesList.add(series);
            seriesQueries.add(new SeriesQuery(entityName, metricPrefix + i, date, endDate));
        }
        Assert.assertTrue("Failed to insert series", executeQuery(seriesQueries));

        String storedSeries = getReturnedSeries().toString();
        String sentSeries = jacksonMapper.writeValueAsString(seriesList);

        JSONAssert.assertEquals(sentSeries, storedSeries, JSONCompareMode.LENIENT);
    }
}
