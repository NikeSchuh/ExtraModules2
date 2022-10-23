package de.nike.extramodules2.utils;

public class FormatUtils {

    public static String formatE(long value) {
        if (value < 1000) return "" + value;
        int exp = (int) (Math.log(value) / Math.log(1000));
        return String.format("%.1f%c",
                value / Math.pow(1000, exp),
                "KMGTPEZY".charAt(exp-1));
    }

}
