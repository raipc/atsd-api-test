package com.axibase.tsd.api.method.sql.function.string;


import com.axibase.tsd.api.method.sql.SqlTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

import static com.axibase.tsd.api.Util.TestNames.generateMetricName;
import static com.axibase.tsd.api.method.sql.function.string.CommonData.POSSIBLE_FUNCTION_ARGS;
import static com.axibase.tsd.api.method.sql.function.string.CommonData.prepareApplyTestData;

public class LengthTest extends SqlTest {
    private static String TEST_METRIC = generateMetricName();


    @BeforeClass
    public void prepareData() throws FileNotFoundException, InterruptedException {
        prepareApplyTestData(TEST_METRIC);
    }

    @DataProvider(name = "applyTestProvider", parallel = true)
    public Object[][] provideApplyTestsData() {
        Integer size = POSSIBLE_FUNCTION_ARGS.size();
        Object[][] result = new Object[size][1];
        for (int i = 0; i < size; i++) {
            result[i][0] = POSSIBLE_FUNCTION_ARGS.get(i);
        }
        return result;
    }

    /**
     * #2920
     */
    @Test(dataProvider = "applyTestProvider")
    public void testApply(String param) throws Exception {
        String sqlQuery = String.format("SELECT LENGTH(%s) FROM '%s'",
                param, TEST_METRIC
        );
        assertOkRequest(String.format("Can't apply LOWER function to %s", param), queryResponse(sqlQuery));
    }
}