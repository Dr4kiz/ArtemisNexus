package me.dkz.plugin.artemis.artemisnexus.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class StringUtils {

    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.forLanguageTag("pt")));
    private static final String[] SUFFIXES = {"K", "M", "B", "T", "Q", "Qq", "Sx", "Sp", "Oc", "N", "D", "Un", "DD"};

    public static String format5(long number) {
        StringBuilder builder = new StringBuilder();
        int suffix = -1;
        if (number >= 1000) {
            double n = number - number % 1;
            long length = length(n);

            long p = length%3;
            if (p == 0) p = 3;

            number = (long) (n / Math.pow(10, length-p));
            suffix = (int)((length - 1) / 3 - 1);
        }

        int base = (int) number;
        int decimals = (int)((number - base)*100);

        builder.append(base).append(".");
        if (decimals < 10) builder.append("0");
        builder.append(decimals);
        if (suffix != -1 && suffix < SUFFIXES.length) builder.append(SUFFIXES[suffix]);
        return builder.toString();
    }

    private static long length(double number) {
        for (long i = 0;;i++) {
            if (number / Math.pow(10, i) < 10) return i+1;
        }
    }
}
