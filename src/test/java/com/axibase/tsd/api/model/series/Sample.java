package com.axibase.tsd.api.model.series;

import com.axibase.tsd.api.Util;
import java.math.BigDecimal;

public class Sample {
    private String d;
    private BigDecimal v;

    public Sample(long t, BigDecimal v) {
        this.d = Util.ISOFormat(t);
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
