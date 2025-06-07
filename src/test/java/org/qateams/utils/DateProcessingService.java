package org.qateams.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DateProcessingService {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static List<Long> convertToUnixTimestamps(List<List<String>> data, int dateColumnIndex) {
        List<Long> unixTimestamps = new ArrayList<>();

        for (List<String> row : data) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(row.get(dateColumnIndex), DATE_FORMATTER);
                long unixTimestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
                unixTimestamps.add(unixTimestamp);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Ошибка парсинга даты: " + row.get(dateColumnIndex), e);
            }
        }

        return unixTimestamps;
    }
}
