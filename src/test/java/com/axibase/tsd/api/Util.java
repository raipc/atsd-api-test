package com.axibase.tsd.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public static class ABNF {

        private static List<Character> generateVisibleCharPool() {
            List pool = new ArrayList<>();
            for (int i = 0x21; i < 0x7e; i++) { //visible character
                pool.add((char) i);
            }

            for (int i = 0x80; i < 0xFF; i++) { //Latin-1 Supplement
                pool.add((char) i);
            }

            for (int i = 0x100; i < 0x17F; i++) { //Latin Extended-A
                pool.add((char) i);
            }

            for (int i = 0x370; i < 0x52F; i++) { //Greek and Coptic, Cyrillic, Cyrillic Supplement
                pool.add((char) i);
            }

            for (int i = 0x4E00; i < 0x4E5F; i++) {//9FFF; i++) { //CJK Unified Ideographs
                pool.add((char) i);
            }
            return pool;

        }
        public static String generateNAME(int length) {
            StringBuilder str = new StringBuilder();
            List<Character> characterPool = generateVisibleCharPool();
            Random randomGenerator = new Random();
            int poolSize = characterPool.size();
            for (int i = 0; i < length; i++) {
                char c = characterPool.get(randomGenerator.nextInt(poolSize));
                if (c == '"') {
                    str.append('\\');
                }
                str.append(c);
            }
            return str.toString();
        }

        public static String generateTEXTVALUE(int length) {
            StringBuilder str = new StringBuilder();
            List<Character> characterPool = generateVisibleCharPool();
            characterPool.add(' ');

            boolean needQuoted = false;
            Random randomGenerator = new Random();
            int poolSize = characterPool.size();
            for (int i = 0; i < length; i++) {
                char c = characterPool.get(randomGenerator.nextInt(poolSize));
                if(c == ' ') {
                    needQuoted = true;
                }
                if (c == '"') {
                    str.append('\\');
                }
                str.append(c);
            }

            if(needQuoted) {
                str.insert(0, '"');
                str.append('"');
            }
            return str.toString();
        }
    }


}
