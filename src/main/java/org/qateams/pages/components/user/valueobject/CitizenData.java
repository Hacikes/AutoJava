package org.qateams.pages.components.user.valueobject;

import java.util.Objects;

public final class CitizenData {

    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final String address;
    private final String dateOfBirth; // Формат ДД.ММ.ГГГГ
    private final String passportNumber;
    private final String gender; // "Мужской" или "Женский"

    // Приватный конструктор для использования с Builder Pattern
    private CitizenData(Builder builder) {
        // Валидация обязательных полей (для VO мы пока не делаем строгую валидацию,
        // позволяя создавать объекты с пустыми строками для тестирования UI-валидации)
        this.lastName = builder.lastName;
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.address = builder.address;
        this.dateOfBirth = builder.dateOfBirth;
        this.passportNumber = builder.passportNumber;
        this.gender = builder.gender;
    }

    // Геттеры
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getAddress() { return address; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getPassportNumber() { return passportNumber; }
    public String getGender() { return gender; }

    // Builder Class
    public static class Builder {
        private String lastName;
        private String firstName;
        private String middleName;
        private String address;
        private String dateOfBirth;
        private String passportNumber;
        private String gender;

        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder middleName(String middleName) { this.middleName = middleName; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder dateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
        public Builder passportNumber(String passportNumber) { this.passportNumber = passportNumber; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }

        public CitizenData build() {
            return new CitizenData(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CitizenData that = (CitizenData) o;
        return Objects.equals(lastName, that.lastName) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(address, that.address) &&
                Objects.equals(dateOfBirth, that.dateOfBirth) &&
                Objects.equals(passportNumber, that.passportNumber) &&
                Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, middleName, address, dateOfBirth, passportNumber, gender);
    }

    @Override
    public String toString() {
        return "CitizenData{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}