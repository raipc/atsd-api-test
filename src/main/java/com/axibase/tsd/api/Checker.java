package com.axibase.tsd.api;


import com.axibase.tsd.api.method.BaseMethod;
import com.axibase.tsd.api.method.checks.AbstractCheck;
import com.axibase.tsd.api.util.NotCheckedException;

public class Checker {
    public static void check(AbstractCheck check) {
        Long startTime = System.currentTimeMillis();
        Boolean result = false;
        do {
            result = check.isChecked();
            if (System.currentTimeMillis() - BaseMethod.UPPER_BOUND_FOR_CHECK > startTime) {
                throw new NotCheckedException(check.getErrorMessage());
            } else {
                try {
                    Thread.sleep(BaseMethod.REQUEST_INTERVAL);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e.getMessage());
                }
            }
        } while (!result);
    }
}