package org.qateams.utils.Faker;

import com.github.javafaker.Faker;

import java.util.Locale;

public class AdminFakerData {

    protected static Faker faker = new Faker(new Locale("ru"));

    protected String getFirstName() {
        return faker.name().firstName();
    }

    protected String getLastName() {
        return faker.name().lastName();
    }


}
