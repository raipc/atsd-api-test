package com.axibase.tsd.api.method.sql.trade;

import com.axibase.tsd.api.Checker;
import com.axibase.tsd.api.method.checks.EntityCheck;
import com.axibase.tsd.api.method.property.PropertyMethod;
import com.axibase.tsd.api.method.trade.session_summary.TradeSessionSummaryMethod;
import com.axibase.tsd.api.model.entity.Entity;
import com.axibase.tsd.api.model.financial.*;
import com.axibase.tsd.api.model.property.Property;
import com.axibase.tsd.api.util.InstrumentStatisticsSender;
import com.axibase.tsd.api.util.Util;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TradePropertyTest extends SqlTradeTest {
    public static final String SECURITY_DEFINITIONS = "security_definitions";

    @BeforeClass
    public void prepareData() throws Exception {
        Entity entity = new Entity();
        entity.addTag("class_code", clazz());
        entity.setName(entity());
        Trade trade = fromISOString("2020-06-15T10:21:49.123456Z");
        insert(trade);
        Checker.check(new EntityCheck(entity));
        trade = fromISOString("2020-06-15T10:21:49.123456Z").setExchange("MOEX");
        insert(trade);
        TradeSessionSummary sessionSummary = new TradeSessionSummary(clazz(), symbol(), TradeSessionType.MORNING, TradeSessionStage.N, "2020-09-10T10:15:20Z");
        sessionSummary.addTag("rptseq", "2");
        sessionSummary.addTag("offer", "10.5");
        sessionSummary.addTag("assured", "true");
        sessionSummary.addTag("action", "test");
        sessionSummary.addTag("starttime", "10:15:20");
        sessionSummary.addTag("snapshot_datetime", "2020-09-10T10:15:20Z");
        TradeSessionSummaryMethod.importStatistics(sessionSummary);

        Property property = new Property();
        property.setType(SECURITY_DEFINITIONS);
        property.setEntity(entity.getName());
        property.setDate("2020-06-14T10:21:49.123Z");
        Map<String, String> tags = new HashMap<>();
        tags.put("roundlot", "10");
        tags.put("product", "5");
        tags.put("marketcode", "FOND");
        property.setTags(tags);
        PropertyMethod.insertPropertyCheck(property);
        property = new Property();
        property.setType(SECURITY_DEFINITIONS);
        property.setEntity(entity.getName());
        property.setDate("2020-06-15T10:21:49.123Z");
        property.setKey(Collections.singletonMap("a", "b"));
        tags = new HashMap<>();
        tags.put("roundlot", "20");
        tags.put("product", "17");
        tags.put("test", "8");
        property.setTags(tags);
        PropertyMethod.insertPropertyCheck(property);

        InstrumentStatistics instrumentStatistics =
                new InstrumentStatistics()
                        .setClazz(clazz())
                        .setSymbol(symbol())
                        .setTimestamp(Util.getUnixTime("2020-06-15T20:21:49.123Z"))
                        .setMicros(456)
                        .addValue("2", "1234") // numoffers
                        .addValue("5", "5060") // numbids
                        .addValue("10", "196.04") // bid
                ;
        InstrumentStatisticsSender
                .send(instrumentStatistics)
                .waitUntilTradesInsertedAtMost(1, TimeUnit.MINUTES);

    }

    @Test
    public void test() throws Exception {
        String sql = "select sec_def.roundlot, sec_def('product'), stat.numbids, stat('numoffers'), stat.bid " +
                "from atsd_trade where " + instrumentCondition();
        String[][] expected = new String[][]{
                {
                        "10", "5", "5060", "1234", "196.04"
                }
        };
        assertSqlQueryRows(expected, sql);
    }

    @Test
    public void testWhereClause() throws Exception {
        String sql = "SELECT symbol, exchange\n" +
                " FROM atsd_trade\n" +
                "WHERE symbol = '" + symbol() + "' AND exchange='MOEX' AND SEC_DEF.marketcode IN ('FOND', 'FNDT')";
        String[][] expected = new String[][]{
                {symbol(), "MOEX"}
        };
        assertSqlQueryRows(expected, sql);
    }

    @Test
    public void testEntityQueryWhereClause() {
        String sql = "select entity from atsd_entity where tags.class_code = '" + clazz() + "' and SEC_DEF.marketcode = 'FOND'";
        String[][] expected = new String[][]{
                {entity()}
        };
        assertSqlQueryRows(expected, sql);
    }

    @Test
    public void testEntityQuery() throws Exception {
        String sql = "select sec_def.roundlot, sec_def('product'), stat.numbids, stat('numoffers'), stat.bid " +
                "from atsd_entity where name = '" + entity() + "'";
        String[][] expected = new String[][]{
                {
                        "10", "5", "5060", "1234", "196.04"
                }
        };
        assertSqlQueryRows(expected, sql);
    }

    @Test
    public void testSessionSummaryQuery() {
        String sql = "select sec_def.roundlot, sec_def('product'), stat.numbids, stat('numoffers'), stat.bid " +
                "from atsd_session_summary where class = '" + clazz() + "' and symbol = '" + symbol() + "'";
        String[][] expected = new String[][]{
                {
                        "10", "5", "5060", "1234", "196.04"
                }
        };
        assertSqlQueryRows(expected, sql);
    }

    @Test
    public void testDateTimeField() {
        String sql = "select sec_def.datetime, stat.datetime from atsd_trade where " + instrumentCondition();
        String[][] expected = new String[][]{
                {
                        "2020-06-14T10:21:49.123000Z", "2020-06-15T20:21:49.123456Z"
                }
        };
        assertSqlQueryRows(expected, sql);
    }
}