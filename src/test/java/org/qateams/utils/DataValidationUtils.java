package org.qateams.utils;

import org.qateams.constansts.ApplicationStatus;
import org.qateams.constansts.ApplicationType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


public class DataValidationUtils {
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidCustomDateFormat(String value) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            LocalDateTime.parse(value, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidApplicationType(String value) {
        return ApplicationType.getAllType().contains(value);
    }

    public static boolean isValidStatus(String value) {
        return ApplicationStatus.getAllStatus().contains(value);
    }

    public static boolean isDescendingSorted(List<Long> timestamps) {
        if (timestamps == null || timestamps.size() <= 1) {
            return true;
        }

        for (int i = 0; i < timestamps.size() - 1; i++) {
            if (timestamps.get(i) < timestamps.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}