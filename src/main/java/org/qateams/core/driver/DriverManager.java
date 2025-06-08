package org.qateams.core.driver;

import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private DriverManager() {
    }

    // Используем ThreadLocal для обеспечения потокобезопасности (каждый поток получит свой экземпляр WebDriver)
    private static ThreadLocal<WebDriver> dr = new ThreadLocal<>();

    /**
     * Возвращает WebDriver, связанный с текущим потоком.
     * @return WebDriver текущего потока.
     */
    public static WebDriver getDriver() {
        return dr.get();
    }

    /**
     * Устанавливает WebDriver для текущего потока.
     * @param driverref - экземпляр WebDriver.
     */
    public static void setDriver(WebDriver driverref) {
        dr.set(driverref);
    }

    /**
     * Удаляет WebDriver из текущего потока (очищает переменную ThreadLocal).
     */
    public static void unload() {
        dr.remove();
    }
}
