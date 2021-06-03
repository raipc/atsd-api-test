package com.axibase.tsd.api.method.trade.export

import com.axibase.tsd.api.model.financial.Trade
import com.axibase.tsd.api.model.trade.ohlcv.ResponseLine
import com.axibase.tsd.api.util.TradeSender
import com.axibase.tsd.api.util.Util
import org.testng.Assert
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import kotlin.streams.toList

/**
 * @param csvFilePath - path to the resource file with trades stored as csv lines
 * @param tradeCreator - function which converts string line values to a Trade
 * @param linesToSkip  - specify to skip several header lines
 */
fun insertTrades(csvFilePath: String,
                 tradeCreator: (List<String>) -> Trade,
                 linesToSkip: Long = 0) {
    val inSteam = Thread.currentThread()
        .contextClassLoader
        .getResourceAsStream(csvFilePath)
        ?: throw IllegalArgumentException("Failed to read file: $csvFilePath")
    val trades = inSteam
        .bufferedReader().lines()
        .skip(linesToSkip)
        .filter(String::isNotBlank)
        .map {
            val values = it
                .split(",")
                .stream()
                .map { v -> v.trim() }
                .collect(Collectors.toList())
            tradeCreator(values)
        }.toList()
    TradeSender.send(trades).waitUntilTradesInsertedAtMost(1, TimeUnit.MINUTES)
}


fun checkLine(lineIndex: Int, actualLine: String, expectedLine: ResponseLine) {
    val actualFields = actualLine.split(",").toTypedArray()
    Assert.assertEquals(actualFields.size, 6, "Unexpected count of fields in line $lineIndex: $actualLine")
    Assert.assertEquals(
        Util.getUnixTime(actualFields[0]), expectedLine.dateMillis,
        "Line $lineIndex. Expected line $expectedLine. Unexpected timestamp.")
    Assert.assertEquals(BigDecimal(actualFields[1]), expectedLine.open, "Line index $lineIndex. Expected line $expectedLine. Unexpected OPEN value.")
    Assert.assertEquals(BigDecimal(actualFields[2]), expectedLine.high, "Line index $lineIndex. Expected line $expectedLine. Unexpected HIGH value.")
    Assert.assertEquals(BigDecimal(actualFields[3]), expectedLine.low, "Line index $lineIndex. Expected line $expectedLine. Unexpected LOW value.")
    Assert.assertEquals(BigDecimal(actualFields[4]), expectedLine.close, "Line index $lineIndex. Expected line $expectedLine. Unexpected CLOSE value.")
    Assert.assertEquals(actualFields[5].toInt(), expectedLine.volume, "Line index $lineIndex. Expected line $expectedLine. Unexpected VOLUME value.")
}
