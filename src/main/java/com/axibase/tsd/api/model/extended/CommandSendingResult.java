package com.axibase.tsd.api.model.extended;


import com.axibase.tsd.api.util.Util;
import lombok.Data;

@Data
public class CommandSendingResult {
    private int fail;
    private int success;
    private int total;

    public CommandSendingResult(int fail, int success) {
        this.fail = fail;
        this.success = success;
        this.total = fail + success;
    }

    @Override
    public String toString() {
        return Util.prettyPrint(this);
    }
}
