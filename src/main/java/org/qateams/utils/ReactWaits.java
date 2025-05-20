package org.qateams.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class ReactWaits {
    private static final Duration REACT_TIMEOUT = Duration.ofSeconds(15);

    // Ожидание готовности React-приложения
    public static void waitForReactReady(WebDriver driver) {
        new WebDriverWait(driver, REACT_TIMEOUT).until(d -> {
            return Objects.requireNonNull(((JavascriptExecutor) driver).executeScript(
                    "return window.reactProps !== undefined && " +
                            "document.querySelector('[data-reactroot]') !== null && " +
                            "!document.querySelector('.ReactLoading')"
            ));
        });
    }

    // Ожидание появления React-компонента по data-атрибуту
    public static void waitForReactComponent(WebDriver driver, String componentSelector) {
        new WebDriverWait(driver, REACT_TIMEOUT).until(d -> {
            return (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return document.querySelector('" + componentSelector + "') !== null && " +
                            "document.querySelector('" + componentSelector + "')._reactRootContainer !== undefined"
            );
        });
    }
}
