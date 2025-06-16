package org.qateams.utils;

public class StringTrimmer {
    public static String trimToMaxLength(String input, int maxLength) {
        if (input == null) {
            return null;
        }

        return input.length() <= maxLength
                ? input
                : input.substring(0, maxLength);
    }
}
