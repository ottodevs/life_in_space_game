package com.widesteppe.utils;

public class ConsoleWriter {
    public static final float SLOW_TYPE_SPEED = 0.16f;
    public static final float FAST_TYPE_SPEED = 0.08f;
    public static final float VERY_FAST_TYPE_SPEED = 0.04f;
    public static final float SUPER_FAST_TYPE_SPEED = 0.01f;
    public static final float LAST_CHAR_DUR = 0.2f;

    public static String generateString(String string, float timeAfterStart, float oneCharDur) {
        String s = getSubstring(string, timeAfterStart, oneCharDur);
        s = addLastCharToString(s, timeAfterStart);

        return s;
    }

    private static String addLastCharToString(String s, float timeAfterStart) {
        float lastTime = timeAfterStart % LAST_CHAR_DUR;
        if (lastTime > 0.1f) {
            s = s + "_";
        }
        return s;
    }

    private static String getSubstring(String string, float timeAfterStart, float oneCharDur) {
        int charLenth = (int) (timeAfterStart / oneCharDur);
        if (charLenth != 0) {
            if (charLenth < string.length()) {
                string = string.substring(0, charLenth);
            }
        } else {
            string = "";
        }
        return string;
    }


    public static boolean isReady(String string, float timeAfterStart, float oneCharDur) {
        int charLenth = (int) (timeAfterStart / oneCharDur);
        if (charLenth >= string.length()) {
            return true;
        }
        return false;
    }
}
