package com.axibase.tsd.api.model.series;

import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.model.Model;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sample extends Model {
    private String d;
    private String v;

    public Sample(long t, int v) {
        this.d = Util.ISOFormat(t);
        this.v = String.valueOf(v);
    }

    public Sample(long t, String v) {
        this.d = Util.ISOFormat(t);
        this.v = v;
    }

    public Sample(String d, int v) {
        this.d = d;
        this.v = String.valueOf(v);;
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
