package com.axibase.tsd.api.method.sql.response;

import com.axibase.tsd.api.method.series.SeriesMethod;
import com.axibase.tsd.api.method.sql.SqlTest;
import com.axibase.tsd.api.model.series.Sample;
import com.axibase.tsd.api.model.series.Series;
import com.axibase.tsd.api.model.sql.StringTable;
import io.qameta.allure.Issue;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.function.Function;

public class SqlIntegerPrecisionLossTest extends SqlTest {

    private static final String TEST_PREFIX = "integer-precision-";
    private static final String TEST_ENTITY_NAME = TEST_PREFIX + "entity";
    private static final String TEST_METRIC_NAME_POS = TEST_PREFIX + "posmetric";
    private static final String TEST_METRIC_NAME_NEG = TEST_PREFIX + "negmetric";

    private enum OperationAccessor {
        PLUS("+", x -> 1000L + x),
        MINUS("-", x -> 1000L - x),
        MULTIPLY("*", x -> 1000L * x),
        DEVIDE("/", x -> 1000L / x),
        MOD("%", x -> 1000L % x);

        String operator;
        Function<Long, Long> operatorFunction;

        OperationAccessor(String operator, Function<Long, Long> operatorFunction) {
            this.operator = operator;
            this.operatorFunction = operatorFunction;
        }

        long apply(long parameter) {
            return operatorFunction.apply(parameter);
        }
    }

    @BeforeClass
    public void prepareData() throws Exception {
        Series negSeries = createTestSeries(false);
        Series PosSeries = createTestSeries(true);

        SeriesMethod.insertSeriesCheck(negSeries);
        SeriesMethod.insertSeriesCheck(PosSeries);
    }

    private static Series createTestSeries(boolean isPositive) {
        long time = 1541030400000L, value;
        Series series;
        if (isPositive) {
            series = new Series(TEST_ENTITY_NAME, TEST_METRIC_NAME_POS);
            series.addSamples(Sample.ofTimeDecimal(time++, new BigDecimal(0x7FFFFFFFFFFFFFFFL)));
            value = 1111111111111111111L;
        } else {
            series = new Series(TEST_ENTITY_NAME, TEST_METRIC_NAME_NEG);
            series.addSamples(Sample.ofTimeDecimal(time++, new BigDecimal(0x8000000000000000L)));
            value = -1111111111111111111L;
        }

        while (value != 0) {
            series.addSamples(Sample.ofTimeDecimal(time++, new BigDecimal(value)));
            value /= 10;
        }

        return series;
    }

    @DataProvider
    public static Object[][] provideArithmeticOperators() {
        return new Object[][]{{"+", 1001L}, {"-", 1001L}, {"/", 1001L}, {"*", 1001L}, {"%", 1001L}};
    }

    @Issue("5736")
    @Test
    public void testPositiveValues() {
        String query = String.format("SELECT value from \"%s\" where entity='%s'", TEST_METRIC_NAME_POS, TEST_ENTITY_NAME);

        String[][] expectedRows = new String[20][1];
        expectedRows[0][0] = Long.toString(Long.MAX_VALUE);
        long value = 1111111111111111111L;
        for (int i = 1; i < 20; i++) {
            expectedRows[i][0] = Long.toString(value);
            value /= 10;
        }
        StringTable resultTable = queryResponse(query).readEntity(StringTable.class);

        assertRowsMatch("Wrong table content", expectedRows, resultTable, query);
    }

    @Issue("5736")
    @Test
    public void testNegativeValues() {
        String query = String.format("SELECT value from \"%s\" where entity='%s'", TEST_METRIC_NAME_NEG, TEST_ENTITY_NAME);

        String[][] expectedRows = new String[20][1];
        expectedRows[0][0] = Long.toString(Long.MIN_VALUE);
        long value = -1111111111111111111L;
        for (int i = 1; i < 20; i++) {
            expectedRows[i][0] = Long.toString(value);
            value /= 10;
        }
        StringTable resultTable = queryResponse(query).readEntity(StringTable.class);

        assertRowsMatch("Wrong table content", expectedRows, resultTable, query);
    }

    @Issue("5736")
    @Test(dataProvider = "provideArithmeticOperators")
    public void testArithmeticOperation(String operator, long number) {
        String query = "SELECT 100000001 " + operator + " 100000001";

        Long expectedResult = 100000001L + 100000001L;
        StringTable resultTable = queryResponse(query).readEntity(StringTable.class);

        Assert.assertEquals(resultTable.getTableMetaData().getColumnMeta(0).getDataType(), "long", "wrong datatype");
        Assert.assertEquals(Long.valueOf(resultTable.getRows().get(0).get(0)), expectedResult, "Operation result wrong");
    }
}
