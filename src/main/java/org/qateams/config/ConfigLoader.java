package org.qateams.config;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigLoader {
    private static final Dotenv dotenv = Dotenv.load();

    // URL, логин и пароль подтягиваются из .env
    public static String getStartPageUrl() {
        String login = dotenv.get("LOGIN");
        String password = dotenv.get("PASSWORD");
        String baseUrl = dotenv.get("BASEURL");
        return String.format("https://%s:%s@%s", login, password, baseUrl);
    }
}
