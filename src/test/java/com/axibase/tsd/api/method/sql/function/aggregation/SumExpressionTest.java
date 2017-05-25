package com.axibase.tsd.api.method.sql.function.aggregation;

import com.axibase.tsd.api.method.series.SeriesMethod;
import com.axibase.tsd.api.method.sql.SqlTest;
import com.axibase.tsd.api.model.series.Sample;
import com.axibase.tsd.api.model.series.Series;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.axibase.tsd.api.util.TestUtil.TestNames.entity;
import static com.axibase.tsd.api.util.TestUtil.TestNames.metric;

public class SumExpressionTest extends SqlTest {

    private static final String METRIC_NAME = metric();

    @BeforeClass
    public static void prepareData() throws Exception {
        Series series = new Series(entity(), METRIC_NAME);

        series.addSamples(new Sample("2017-01-01T12:00:00.000Z", "1"));
        series.addSamples(new Sample("2017-01-02T12:00:00.000Z", "2"));
        series.addSamples(new Sample("2017-01-03T12:00:00.000Z", "3"));
        series.addSamples(new Sample("2017-01-04T12:00:00.000Z", "4"));
        series.addSamples(new Sample("2017-01-05T12:00:00.000Z", "5"));

        SeriesMethod.insertSeriesCheck(series);
    }

    /**
     * #3950
     */
    @Test
    public void testSumInExpression() {
        String sqlQuery = String.format(
                "SELECT sum(value * value) * sum(value * 2) FROM '%s'",
                METRIC_NAME);

        String[][] expectedRows = {{"1650"}};

        assertSqlQueryRows("Wrong result for SUM aggregation function inside expressions", expectedRows, sqlQuery);
    }
}
