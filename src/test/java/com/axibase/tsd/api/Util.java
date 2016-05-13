package com.axibase.tsd.api;

/**
 * @author Dmitry Korchagin.
 */
public class Util {
    public static String buildVariablePrefix() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < methodName.length(); i++) {
            Character ch = methodName.charAt(i);
            if (Character.isUpperCase(ch)) {
                prefix.append("-");
            }
            prefix.append(Character.toLowerCase(ch));
        }
        prefix.append("-");
        return prefix.toString();
    }
}
