package com.axibase.tsd.api.method.property;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * @author Dmitry Korchagin.
 */
public class JsonAssertExample {

    @Test
    public void test_JSONAssert() throws JSONException {
        String given = "{friends:[{id:456,name:\"Carter Page\"}, {id:123,name:\"Corby Page\"}]}";

        String expected = "{friends:[{id:123,name:\"Corby Page\"},{id:456}]}";
        JSONAssert.assertEquals(expected, given, JSONCompareMode.LENIENT);
    }
}
