package org.qateams.constansts;

public enum ApplicationStatus {
    IN_REVIEW("На рассмотрении"),
    APPROVED("Одобрена"),
    REJECTED("Отклонена");

    private final String russianName;

    ApplicationStatus(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }


}
