package com.axibase.tsd.api.method.message;

import com.axibase.tsd.api.model.message.Message;
import com.axibase.tsd.api.model.message.MessageQuery;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageInsertTest extends MessageMethod {

    @BeforeClass
    public static void setUpBeforeClass() {
        prepare();
    }

    /* #2903 */
    @Test
    public void testTrimmedMessages() throws Exception {
        String entityName = "          nurswgvml022    \n    ";
        String messageText = "          NURSWGVML007 ssh: error: connect_to localhost port 8881: failed.     \n     ";
        String type = "      application    \n      ";
        String startDate = "2016-05-21T00:00:00Z";
        String endDate = "2016-05-21T00:00:01Z";

        Message message = new Message(entityName, messageText);
        message.setDate(startDate);
        message.setType(type);

        Assert.assertTrue("Fail to insert message", insertMessages(message));

        MessageQuery messageQuery = new MessageQuery("nurswgvml022", startDate, endDate);
        executeQuery(messageQuery);

        Assert.assertEquals("nurswgvml022", getDataField(0, "entity"));
        Assert.assertEquals("NURSWGVML007 ssh: error: connect_to localhost port 8881: failed.", getDataField(0, "message"));
        Assert.assertEquals("application", getDataField(0, "type"));
    }
}
