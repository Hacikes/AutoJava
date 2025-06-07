package org.qateams.constansts;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum ApplicationStatus {
    IN_REVIEW("На рассмотрении", "#1b7eaf"),
    APPROVED("Одобрена", "#088e08"),
    REJECTED("Отклонена", "#c33117f2");

    private final String russianName;
    private final String hexColor;

    ApplicationStatus(String russianName, String hexColor) {
        this.russianName = russianName;
        this.hexColor = hexColor;
    }

    public String getRussianName() {
        return russianName;
    }

    public String getHexColor() {
        return hexColor;
    }

    public static List<String> getAllStatus() {
        return Arrays.asList(
                IN_REVIEW.getRussianName(),
                APPROVED.getRussianName(),
                REJECTED.getRussianName()
        );
    }

    public static Optional<ApplicationStatus> getByRussianName(String russianName) {
        return Arrays.stream(values())
                .filter(status -> status.getRussianName().equals(russianName))
                .findFirst();
    }

    public static Optional<String> getColorByRussianName(String russianName) {
        return getByRussianName(russianName)
                .map(ApplicationStatus::getHexColor);
    }




}
