package com.axibase.tsd.api.method.checks;


import com.axibase.tsd.api.method.BaseMethod;
import com.axibase.tsd.api.util.NotCheckedException;


public class NotPassedCheck{

    public static void assertNotPassed(String message, AbstractCheck aCheck) {
        try {
            check(aCheck);
        } catch (NotCheckedException nce) {
            return;
        }
        throw new AssertionError(message);
    }

    private static void check(AbstractCheck check) {
        Long uppderBounds = 10000L;
        Long startTime = System.currentTimeMillis();
        while (!check.isChecked()) {
            if (System.currentTimeMillis() - uppderBounds > startTime) {
                throw new NotCheckedException(check.getErrorMessage());
            } else {
                try {
                    Thread.sleep(BaseMethod.REQUEST_INTERVAL);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e.getMessage());
                }
            }
        }
    }
}
