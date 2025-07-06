package org.qateams.pages.components.user.valueobject;

import java.util.Objects;

    public final class ApplicantData {

        private final String lastName;
        private final String firstName;
        private final String middleName;
        private final String address;
        private final String phone;
        private final String passportNumber; // Обязательное поле

        // Используем Builder Pattern для более гибкого создания объектов с необязательными полями
        private ApplicantData(Builder builder) {
            // Валидация обязательных полей
            if (builder.passportNumber == null || builder.passportNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Номер паспорта обязателен.");
            }
            // Можно добавить более строгие проверки формата здесь или на уровне методов set в Page Object,
            // но для VO основная идея - это целостность объекта.

            this.lastName = builder.lastName;
            this.firstName = builder.firstName;
            this.middleName = builder.middleName;
            this.address = builder.address;
            this.phone = builder.phone;
            this.passportNumber = builder.passportNumber;
        }

        // Геттеры
        public String getLastName() { return lastName; }
        public String getFirstName() { return firstName; }
        public String getMiddleName() { return middleName; }
        public String getAddress() { return address; }
        public String getPhone() { return phone; }
        public String getPassportNumber() { return passportNumber; }

        // Builder Class
        public static class Builder {
            private String lastName;
            private String firstName;
            private String middleName;
            private String address;
            private String phone;
            private String passportNumber;

            public Builder lastName(String lastName) { this.lastName = lastName; return this; }
            public Builder firstName(String firstName) { this.firstName = firstName; return this; }
            public Builder middleName(String middleName) { this.middleName = middleName; return this; }
            public Builder address(String address) { this.address = address; return this; }
            public Builder phone(String phone) { this.phone = phone; return this; }
            public Builder passportNumber(String passportNumber) { this.passportNumber = passportNumber; return this; }

            public ApplicantData build() {
                return new ApplicantData(this);
            }
        }

        // Переопределение equals() и hashCode() для сравнения по значению
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ApplicantData that = (ApplicantData) o;
            return Objects.equals(lastName, that.lastName) &&
                    Objects.equals(firstName, that.firstName) &&
                    Objects.equals(middleName, that.middleName) &&
                    Objects.equals(address, that.address) &&
                    Objects.equals(phone, that.phone) &&
                    Objects.equals(passportNumber, that.passportNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lastName, firstName, middleName, address, phone, passportNumber);
        }

        @Override
        public String toString() {
            return "ApplicantData{" +
                    "lastName='" + lastName + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", middleName='" + middleName + '\'' +
                    ", address='" + address + '\'' +
                    ", phone='" + phone + '\'' +
                    ", passportNumber='" + passportNumber + '\'' +
                    '}';
        }
}
