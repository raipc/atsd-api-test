package com.axibase.tsd.api.method.series;

import com.axibase.tsd.api.model.Interval;
import com.axibase.tsd.api.model.TimeUnit;
import com.axibase.tsd.api.model.series.Sample;
import com.axibase.tsd.api.model.series.Series;
import com.axibase.tsd.api.model.series.SeriesQuery;
import com.axibase.tsd.api.util.Util;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.util.Collections;

import static com.axibase.tsd.api.util.ErrorTemplate.DATE_FILTER_COMBINATION_REQUIRED;
import static com.axibase.tsd.api.util.ErrorTemplate.DATE_FILTER_END_GREATER_START_REQUIRED;
import static com.axibase.tsd.api.util.Mocks.*;
import static com.axibase.tsd.api.util.Util.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class SeriesQueryDateFilterTest extends SeriesMethod {
    private final Sample DATE_FILTER_DEFAULT_SAMPLE = new Sample("2014-06-06T00:00:00.000Z", 1);

    /**
     * #3030
     */
    @Test
    public void testIntervalOnly() throws Exception {
        Series series = new Series("datefilter-e-1", "datefilter-m-1");
        series.addSamples(DATE_FILTER_DEFAULT_SAMPLE);
        insertSeriesCheck(Collections.singletonList(series));

        SeriesQuery query = new SeriesQuery(series.getEntity(), series.getMetric());
        query.setInterval(new Interval(40, TimeUnit.YEAR));

        Response response = querySeries(query);
        assertEquals("Response code mismatch", OK.getStatusCode(), response.getStatus());
        final String expected = jacksonMapper.writeValueAsString(Collections.singletonList(series));
        final String given = response.readEntity(String.class);
        assertTrue("Stored series mismatch", compareJsonString(expected, given));
    }

    /**
     * #3030
     */
    @Test
    public void testIntervalAndEnd() throws Exception {
        Series series = new Series("datefilter-e-2", "datefilter-m-2");
        series.addSamples(DATE_FILTER_DEFAULT_SAMPLE);
        insertSeriesCheck(Collections.singletonList(series));

        SeriesQuery query = new SeriesQuery(series.getEntity(), series.getMetric());
        query.setInterval(new Interval(300, TimeUnit.YEAR));
        query.setEndDate(MAX_STORABLE_DATE);

        Response response = querySeries(query);
        assertEquals("Response code mismatch", OK.getStatusCode(), response.getStatus());
        final String expected = jacksonMapper.writeValueAsString(Collections.singletonList(series));
        final String given = response.readEntity(String.class);
        assertTrue("Stored series mismatch", compareJsonString(expected, given));
    }

    /**
     * #3030
     */
    @Test
    public void testIntervalAndStart() throws Exception {
        Series series = new Series("datefilter-e-3", "datefilter-m-3");
        series.addSamples(DATE_FILTER_DEFAULT_SAMPLE);
        insertSeriesCheck(Collections.singletonList(series));

        SeriesQuery query = new SeriesQuery(series.getEntity(), series.getMetric());
        query.setInterval(new Interval(300, TimeUnit.YEAR));
        query.setStartDate(MIN_STORABLE_DATE);

        Response response = querySeries(query);
        assertEquals("Response code mismatch", OK.getStatusCode(), response.getStatus());
        final String expected = jacksonMapper.writeValueAsString(Collections.singletonList(series));
        final String given = response.readEntity(String.class);
        assertTrue("Stored series mismatch", compareJsonString(expected, given));
    }

    /**
     * #3030
     */
    @Test
    public void testStartOnlyRaiseError() throws Exception {
        SeriesQuery query = new SeriesQuery("mockEntity", "mockMetric");
        query.setStartDate(MIN_STORABLE_DATE);

        Response response = querySeries(query);
        assertEquals("Response code mismatch", BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Error message mismatch", DATE_FILTER_COMBINATION_REQUIRED, extractErrorMessage(response));
    }

    /**
     * #3030
     */
    @Test
    public void testEndOnlyRaiseError() throws Exception {
        SeriesQuery query = new SeriesQuery("mockEntity", "mockMetric");
        query.setEndDate(MAX_QUERYABLE_DATE);

        Response response = querySeries(query);
        assertEquals("Response code mismatch", BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Error message mismatch", DATE_FILTER_COMBINATION_REQUIRED, extractErrorMessage(response));
    }

    /**
     * #3030
     */
    @Test
    public void testStartGreaterEndRaiseError() throws Exception {
        SeriesQuery query = new SeriesQuery("mockEntity", "mockMetric");
        query.setEndDate(MIN_QUERYABLE_DATE);
        query.setStartDate(Util.addOneMS(MIN_QUERYABLE_DATE));

        Response response = querySeries(query);
        assertEquals("Response code mismatch", BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Error message mismatch", DATE_FILTER_END_GREATER_START_REQUIRED, extractErrorMessage(response));
    }

    /**
     * #3030
     */
    @Test
    public void testStartEqualEndRaiseError() throws Exception {
        SeriesQuery query = new SeriesQuery("mockEntity", "mockMetric");
        query.setEndDate(MIN_QUERYABLE_DATE);
        query.setStartDate(MIN_QUERYABLE_DATE);

        Response response = querySeries(query);
        assertEquals("Response code mismatch", BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Error message mismatch", DATE_FILTER_END_GREATER_START_REQUIRED, extractErrorMessage(response));
    }

    /**
     * #3030
     */
    @Test
    public void testIntervalZeroAndStartRaiseError() throws Exception {
        SeriesQuery query = new SeriesQuery("mockEntity", "mockMetric");
        query.setInterval(new Interval(0, TimeUnit.HOUR));
        query.setStartDate(MIN_QUERYABLE_DATE);

        Response response = querySeries(query);
        assertEquals("Response code mismatch", BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Error message mismatch", DATE_FILTER_END_GREATER_START_REQUIRED, extractErrorMessage(response));
    }

    /**
     * #3030
     */
    @Test
    public void testIntervalZeroAndEndRaiseError() throws Exception {
        SeriesQuery query = new SeriesQuery("mockEntity", "mockMetric");
        query.setInterval(new Interval(0, TimeUnit.HOUR));
        query.setEndDate(MIN_QUERYABLE_DATE);

        Response response = querySeries(query);
        assertEquals("Response code mismatch", BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Error message mismatch", DATE_FILTER_END_GREATER_START_REQUIRED, extractErrorMessage(response));
    }

}
