package org.qateams.pages.components.user.valueobject;

import java.util.Objects;

public class MarriageServiceData {

    private final String registrationDate; // Дата регистрации (ДД.ММ.ГГГГ)
    private final String spouseLastName;
    private final String spouseFirstName;
    private final String spouseMiddleName;
    private final String spouseDateOfBirth; // Дата рождения супруга (ДД.ММ.ГГГГ)
    private final String spousePassportNumber;

    private MarriageServiceData(Builder builder) {
        // Все поля обязательны
        if (builder.registrationDate == null || builder.registrationDate.trim().isEmpty() ||
                builder.spouseLastName == null || builder.spouseLastName.trim().isEmpty() ||
                builder.spouseFirstName == null || builder.spouseFirstName.trim().isEmpty() ||
                builder.spouseMiddleName == null || builder.spouseMiddleName.trim().isEmpty() ||
                builder.spouseDateOfBirth == null || builder.spouseDateOfBirth.trim().isEmpty() ||
                builder.spousePassportNumber == null || builder.spousePassportNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Все обязательные поля для MarriageServiceData должны быть заполнены.");
        }
        this.registrationDate = builder.registrationDate;
        this.spouseLastName = builder.spouseLastName;
        this.spouseFirstName = builder.spouseFirstName;
        this.spouseMiddleName = builder.spouseMiddleName;
        this.spouseDateOfBirth = builder.spouseDateOfBirth;
        this.spousePassportNumber = builder.spousePassportNumber;
    }

    // Геттеры
    public String getRegistrationDate() { return registrationDate; }
    public String getSpouseLastName() { return spouseLastName; }
    public String getSpouseFirstName() { return spouseFirstName; }
    public String getSpouseMiddleName() { return spouseMiddleName; }
    public String getSpouseDateOfBirth() { return spouseDateOfBirth; }
    public String getSpousePassportNumber() { return spousePassportNumber; }

    public static class Builder {
        private String registrationDate;
        private String spouseLastName;
        private String spouseFirstName;
        private String spouseMiddleName;
        private String spouseDateOfBirth;
        private String spousePassportNumber;

        public Builder registrationDate(String registrationDate) { this.registrationDate = registrationDate; return this; }
        public Builder spouseLastName(String spouseLastName) { this.spouseLastName = spouseLastName; return this; }
        public Builder spouseFirstName(String spouseFirstName) { this.spouseFirstName = spouseFirstName; return this; }
        public Builder spouseMiddleName(String spouseMiddleName) { this.spouseMiddleName = spouseMiddleName; return this; }
        public Builder spouseDateOfBirth(String spouseDateOfBirth) { this.spouseDateOfBirth = spouseDateOfBirth; return this; }
        public Builder spousePassportNumber(String spousePassportNumber) { this.spousePassportNumber = spousePassportNumber; return this; }

        public MarriageServiceData build() {
            return new MarriageServiceData(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarriageServiceData that = (MarriageServiceData) o;
        return Objects.equals(registrationDate, that.registrationDate) &&
                Objects.equals(spouseLastName, that.spouseLastName) &&
                Objects.equals(spouseFirstName, that.spouseFirstName) &&
                Objects.equals(spouseMiddleName, that.spouseMiddleName) &&
                Objects.equals(spouseDateOfBirth, that.spouseDateOfBirth) &&
                Objects.equals(spousePassportNumber, that.spousePassportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationDate, spouseLastName, spouseFirstName, spouseMiddleName, spouseDateOfBirth, spousePassportNumber);
    }
}
