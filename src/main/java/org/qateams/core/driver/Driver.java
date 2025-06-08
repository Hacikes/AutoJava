package org.qateams.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.qateams.config.ConfigLoader;
import org.qateams.constansts.FrameworkConstants;

import java.util.Objects;

public final class Driver {

    private Driver() {
    }

    // Статическая переменная для хранения WebDriver (НЕПОТОКОБЕЗОПАСНАЯ)
    private static WebDriver driver;

    /**
     * Инициализирует WebDriver, если он ещё не был создан.
     * Добавлена потокобезопасность через ThreadLocal.
     */
    public static void initDriver() {

        if (Objects.isNull(DriverManager.getDriver())) { // Проверяем, не создан ли WebDriver в текущем потоке
            System.setProperty("webdriver.chrome.driver", FrameworkConstants.getChromeDriverPath());

            DriverManager.setDriver(new ChromeDriver()); // Создаём новый экземпляр ChromeDriver и сохраняем в ThreadLocal в DriverManager
            try {
                DriverManager.getDriver().get(ConfigLoader.getStartPageUrl()); // Открываем стартовую страницу
            } catch (Exception e) {
                System.out.println("Start page not found!");
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Завершает работу WebDriver и очищает ThreadLocal.
     */
    public static void quitDriver() {
        if (Objects.nonNull(DriverManager.getDriver())) { // Проверяем, есть ли WebDriver в текущем потоке
            DriverManager.getDriver().quit(); // Закрываем браузер
            DriverManager.unload(); // Удаляем WebDriver из ThreadLocal
        }
    }
}
