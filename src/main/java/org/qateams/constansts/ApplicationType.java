package org.qateams.constansts;

import java.util.Arrays;
import java.util.List;

public enum ApplicationType {

    BIRTH_RU("Получение свидетельства о рождении"),
    WEDDING_RU("Получение свидетельства о браке"),
    DEATH_RU("Получение свидетельства о смерти");

    private final String russianName;

    ApplicationType(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }

    public static List<String> getAllType() {
        return Arrays.asList(
                BIRTH_RU.getRussianName(),
                WEDDING_RU.getRussianName(),
                DEATH_RU.getRussianName()
        );
    }
}
