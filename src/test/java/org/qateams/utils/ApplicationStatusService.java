package org.qateams.utils;

import org.qateams.constansts.ApplicationStatus;

import java.util.Optional;

public class ApplicationStatusService {
    public static void validateStatusAndColor(String statusText) {
        // Проверка статуса и цвета
        Optional<String> statusColor = ApplicationStatus.getColorByRussianName(statusText);

        if (!ApplicationStatus.getAllStatus().contains(statusText)) {
            throw new IllegalArgumentException("Статус '" + statusText + "' не является допустимым");
        }

        if (statusColor.isEmpty()) {
            throw new IllegalArgumentException("Не найден цвет для статуса: " + statusText);
        }

        // Дополнительные проверки цветов
        switch (statusText) {
            case "На рассмотрении":
                validateColor(statusColor.get(), "#1b7eaf");
                break;
            case "Одобрена":
                validateColor(statusColor.get(), "#088e08");
                break;
            case "Отклонена":
                validateColor(statusColor.get(), "#c33117f2");
                break;
        }
    }

    private static void validateColor(String actualColor, String expectedColor) {
        if (!actualColor.equals(expectedColor)) {
            throw new AssertionError("Неверный цвет для статуса. Ожидался: " + expectedColor + ", получен: " + actualColor);
        }
    }
}