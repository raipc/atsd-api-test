package com.axibase.tsd.api.model.series;

import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.model.Model;

public class Sample extends Model {
    private String d;
    private String v;

    public Sample(long t, String v) {
        this.d = Util.ISOFormat(t);
        this.v = v;
    }

    public Sample(String d, String v) {
        this.d = d;
        this.v = v;
    }

    public String getD() {
        return d;
    }

    public String getV() {
        return v;
    }

}
