package org.qateams.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.core.driver.DriverManager;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class InputValidationComponent {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public InputValidationComponent() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Инициализируем WebDriverWait
    }

    /**
     * Проверяет, подсвечено ли поле красным (для ошибок валидации).
     * ОБНОВЛЕНО: Добавлено явное ожидание состояния.
     * @param inputLocator Локатор поля ввода.
     * @return true, если поле подсвечено красным, иначе false.
     */
    public boolean isFieldHighlightedInRed(By inputLocator) {
        WebElement inputElement = wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));

        try {
            // Ждем, пока либо aria-invalid станет true, либо появятся классы красной рамки.
            return wait.until(driver -> {
                WebElement element = driver.findElement(inputLocator); // Перезапрашиваем элемент для актуального состояния
                boolean hasAriaInvalid = element.getAttribute("aria-invalid") != null
                        && element.getAttribute("aria-invalid").equals("true");

                // Классы, отвечающие за красную рамку (из вашего HTML)
                List<String> redBorderClasses = Arrays.asList(
                        "ub-b-btm-clr_D14343_fpx4do",
                        "ub-b-lft-clr_D14343_fpx4do",
                        "ub-b-rgt-clr_D14343_fpx4do",
                        "ub-b-top-clr_D14343_fpx4do"
                );
                // Проверяем, что присутствует ХОТЯ БЫ ОДИН из классов красной рамки
                boolean hasRedBorderClasses = redBorderClasses.stream()
                        .anyMatch(cls -> element.getAttribute("class").contains(cls));

                // Поле считается подсвеченным, если есть aria-invalid="true" ИЛИ есть класс красной рамки.
                return hasAriaInvalid || hasRedBorderClasses;
            });
        } catch (org.openqa.selenium.TimeoutException e) {
            // Если условие не выполнилось в течение таймаута, значит поле не подсвечено красным.
            return false;
        }
    }

    /**
     * Проверяет, находится ли поле в нормальном (неошибочном) состоянии.
     * ОБНОВЛЕНО: Добавлено явное ожидание состояния.
     * @param inputLocator Локатор поля ввода.
     * @return true, если поле в нормальном состоянии, иначе false.
     */
    public boolean isFieldNormalState(By inputLocator) {
        WebElement inputElement = wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));

        try {
            // Ждем, пока aria-invalid не будет true ИЛИ пока не исчезнут классы красной рамки.
            return wait.until(driver -> {
                WebElement element = driver.findElement(inputLocator); // Перезапрашиваем элемент
                boolean noAriaInvalid = element.getAttribute("aria-invalid") == null
                        || element.getAttribute("aria-invalid").equals("false");

                List<String> redBorderClasses = Arrays.asList(
                        "ub-b-btm-clr_D14343_fpx4do",
                        "ub-b-lft-clr_D14343_fpx4do",
                        "ub-b-rgt-clr_D14343_fpx4do",
                        "ub-b-top-clr_D14343_fpx4do"
                );
                // Проверяем, что НИ ОДИН из классов красной рамки не присутствует
                boolean noRedBorderClasses = redBorderClasses.stream()
                        .noneMatch(cls -> element.getAttribute("class").contains(cls));

                return noAriaInvalid && noRedBorderClasses;
            });
        } catch (org.openqa.selenium.TimeoutException e) {
            // Если условие не выполнилось в течение таймаута, значит поле все еще в ненормальном состоянии.
            return false;
        }
    }
}