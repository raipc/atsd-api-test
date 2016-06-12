package com.axibase.tsd.api.model.series;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Sample {
    private String d;
    private BigDecimal v;

    public Sample(long t, BigDecimal v) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(t);

        this.d = dateFormat.format(calendar.getTime());
        this.v = v;
    }

    public Sample(String d, BigDecimal v) {
        this.d = d;
        this.v = v;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public BigDecimal getV() {
        return v;
    }

    public void setV(BigDecimal v) {
        this.v = v;
    }
}
