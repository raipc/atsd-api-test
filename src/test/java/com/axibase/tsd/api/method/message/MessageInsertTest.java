package com.axibase.tsd.api.method.message;

import com.axibase.tsd.api.model.message.Message;
import com.axibase.tsd.api.model.message.MessageQuery;
import org.junit.Assert;
import org.junit.Test;

public class MessageInsertTest extends MessageMethod {

    /* #2903 */
    @Test
    public void testTrimmedMessages() throws Exception {
        String entityName = "          nurswgvml022    \n    ";
        String messageText = "          NURSWGVML007 ssh: error: connect_to localhost port 8881: failed.     \n     ";
        String type = "      application    \n      ";
        String date = "2016-05-21T00:00:00Z";
        String endDate = "2016-05-21T00:00:01Z";

        Message message = new Message(entityName, messageText);
        message.setDate(date);
        message.setType(type);

        Assert.assertTrue("Fail to insert message", insertMessages(message));

        MessageQuery messageQuery = new MessageQuery("nurswgvml022", date, endDate);
        executeQuery(messageQuery);

        Assert.assertEquals("nurswgvml022", getField(0, "entity"));
        Assert.assertEquals("NURSWGVML007 ssh: error: connect_to localhost port 8881: failed.", getField(0, "message"));
        Assert.assertEquals("application", getField(0, "type"));
    }
}