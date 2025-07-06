package org.qateams.pages.components.user.valueobject;

import java.util.Objects;

public class DeathServiceData {

    private final String dateOfDeath; // Дата смерти (датапикер)
    private final String placeOfDeath;

    private DeathServiceData(Builder builder) {
        // Все поля обязательны
        if (builder.dateOfDeath == null || builder.dateOfDeath.trim().isEmpty() ||
                builder.placeOfDeath == null || builder.placeOfDeath.trim().isEmpty()) {
            throw new IllegalArgumentException("Все обязательные поля для DeathServiceData должны быть заполнены.");
        }
        this.dateOfDeath = builder.dateOfDeath;
        this.placeOfDeath = builder.placeOfDeath;
    }

    // Геттеры
    public String getDateOfDeath() { return dateOfDeath; }
    public String getPlaceOfDeath() { return placeOfDeath; }

    public static class Builder {
        private String dateOfDeath;
        private String placeOfDeath;

        public Builder dateOfDeath(String dateOfDeath) { this.dateOfDeath = dateOfDeath; return this; }
        public Builder placeOfDeath(String placeOfDeath) { this.placeOfDeath = placeOfDeath; return this; }

        public DeathServiceData build() {
            return new DeathServiceData(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeathServiceData that = (DeathServiceData) o;
        return Objects.equals(dateOfDeath, that.dateOfDeath) &&
                Objects.equals(placeOfDeath, that.placeOfDeath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateOfDeath, placeOfDeath);
    }
}
