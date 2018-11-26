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
import java.util.function.BiFunction;

public class SqlIntegerPrecisionLossTest extends SqlTest {

    private static final String TEST_PREFIX = "integer-precision-";
    private static final String TEST_ENTITY_NAME = TEST_PREFIX + "entity";
    private static final String TEST_METRIC_NAME_POS = TEST_PREFIX + "posmetric";
    private static final String TEST_METRIC_NAME_NEG = TEST_PREFIX + "negmetric";

    private enum OperationAccessor {
        PLUS("+", (x, y) -> x + y),
        MINUS("-", (x, y) -> x - y),
        MULTIPLY("*", (x, y) -> x * y),
        DIVIDE("/", (x, y) -> x / y),
        MOD("%", (x, y) -> x % y);

        String operator;
        BiFunction<Long, Long, Long> operatorFunction;

        OperationAccessor(String operator, BiFunction<Long, Long, Long> operatorFunction) {
            this.operator = operator;
            this.operatorFunction = operatorFunction;
        }

        long apply(long parameterOne, long parameterTwo) {
            return operatorFunction.apply(parameterOne, parameterTwo);
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
            series.addSamples(Sample.ofTimeDecimal(time++, new BigDecimal(Long.MAX_VALUE)));
            value = 1111111111111111111L;
        } else {
            series = new Series(TEST_ENTITY_NAME, TEST_METRIC_NAME_NEG);
            series.addSamples(Sample.ofTimeDecimal(time++, new BigDecimal(Long.MIN_VALUE)));
            value = -1111111111111111111L;
        }

        while (value != 0) {
            series.addSamples(Sample.ofTimeDecimal(time++, new BigDecimal(value)));
            value /= 10;
        }

        return series;
    }

    private static Object[][] getParameters() {
        return new Object[][]{{10L, 2L}, {-10L, -2L}, {900L, 3L}, {1111111111111111111L, 1111111111111111111L}};
    }

    @DataProvider
    public static Object[][] provideArithmeticOperatorsWithOperator() {
        OperationAccessor[] accessors = OperationAccessor.values();
        Object[][] parameters = getParameters();
        Object[][] result = new Object[parameters.length * accessors.length][];

        for (int i = 0; i < accessors.length; i++)
            for (int j = 0; j < parameters.length; j++) {
                result[i * parameters.length + j] = new Object[]{parameters[j][0], parameters[j][1], accessors[i]};
            }
        return result;
    }

    @Issue("5736")
    @Test
    public void testDisplayValues() {
        String query = String.format(
                "select t1.value, t2.value \n" +
                        "from \"%s\" as t1\n" +
                        "join \"%s\" as t2",
                TEST_METRIC_NAME_POS,
                TEST_METRIC_NAME_NEG
        );

        String[][] expectedRows = new String[20][2];
        expectedRows[0][0] = Long.toString(Long.MAX_VALUE);
        expectedRows[0][1] = Long.toString(Long.MIN_VALUE);
        long value = 1111111111111111111L;
        for (int i = 1; i < 20; i++) {
            expectedRows[i][0] = Long.toString(value);
            expectedRows[i][1] = Long.toString(-value);
            value /= 10;
        }
        StringTable resultTable = queryResponse(query).readEntity(StringTable.class);

        assertRowsMatch("Wrong table content", expectedRows, resultTable, query);
    }

    @Issue("5736")
    @Test(dataProvider = "provideArithmeticOperatorsWithOperator")
    public void testArithmeticOperation(Long numberOne, Long numberTwo, OperationAccessor accessor) {
        String query = String.format("SELECT %s %s %s", numberOne, accessor.operator, numberTwo);

        Long expectedResult = accessor.apply(numberOne, numberTwo);
        StringTable resultTable = queryResponse(query).readEntity(StringTable.class);

        String message = String.format("Operator %s wrong ", accessor.operator);
        Assert.assertEquals(resultTable.getTableMetaData().getColumnMeta(0).getDataType(), "bigint", message + "datatype");
        Assert.assertEquals(Long.valueOf(resultTable.getRows().get(0).get(0)), expectedResult, message + "result");
    }
}
