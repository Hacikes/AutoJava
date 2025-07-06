package org.qateams.pages.components.user.valueobject;

import java.util.Objects;

public class BirthServiceData {

    private final String placeOfBirth;
    private final String mother;
    private final String father;
    private final String grandmother;
    private final String grandfather;

    private BirthServiceData(Builder builder) {
        // Все поля обязательны
        if (builder.placeOfBirth == null || builder.placeOfBirth.trim().isEmpty() ||
                builder.mother == null || builder.mother.trim().isEmpty() ||
                builder.father == null || builder.father.trim().isEmpty() ||
                builder.grandmother == null || builder.grandmother.trim().isEmpty() ||
                builder.grandfather == null || builder.grandfather.trim().isEmpty()) {
            throw new IllegalArgumentException("Все обязательные поля для BirthServiceData должны быть заполнены.");
        }
        this.placeOfBirth = builder.placeOfBirth;
        this.mother = builder.mother;
        this.father = builder.father;
        this.grandmother = builder.grandmother;
        this.grandfather = builder.grandfather;
    }

    // Геттеры
    public String getPlaceOfBirth() { return placeOfBirth; }
    public String getMother() { return mother; }
    public String getFather() { return father; }
    public String getGrandmother() { return grandmother; }
    public String getGrandfather() { return grandfather; }

    public static class Builder {
        private String placeOfBirth;
        private String mother;
        private String father;
        private String grandmother;
        private String grandfather;

        public Builder placeOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; return this; }
        public Builder mother(String mother) { this.mother = mother; return this; }
        public Builder father(String father) { this.father = father; return this; }
        public Builder grandmother(String grandmother) { this.grandmother = grandmother; return this; }
        public Builder grandfather(String grandfather) { this.grandfather = grandfather; return this; }

        public BirthServiceData build() {
            return new BirthServiceData(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BirthServiceData that = (BirthServiceData) o;
        return Objects.equals(placeOfBirth, that.placeOfBirth) &&
                Objects.equals(mother, that.mother) &&
                Objects.equals(father, that.father) &&
                Objects.equals(grandmother, that.grandmother) &&
                Objects.equals(grandfather, that.grandfather);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeOfBirth, mother, father, grandmother, grandfather);
    }
}
