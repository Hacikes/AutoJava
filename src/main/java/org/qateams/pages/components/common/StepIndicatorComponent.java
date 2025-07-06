package org.qateams.pages.components.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.core.driver.DriverManager;

import java.time.Duration;

public class StepIndicatorComponent {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = ".step-indicator[data-step='1']")
    private WebElement step1Indicator;

    @FindBy(css = ".step-indicator[data-step='2']")
    private WebElement step2Indicator;

    @FindBy(css = ".step-indicator[data-step='3']")
    private WebElement step3Indicator;

    @FindBy(css = ".step-indicator[data-step='4']")
    private WebElement step4Indicator;

    @FindBy(css = ".step-indicator[data-step='5']")
    private WebElement step5Indicator;

    // Конструктор теперь не принимает WebDriver, а получает его из DriverManager
    public StepIndicatorComponent() {
        this.driver = DriverManager.getDriver(); // Получаем драйвер из ThreadLocal
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Проверяет, что индикатор указанного шага стал зеленым.
     * @param stepNumber Номер шага (1-5).
     * @return true, если индикатор зеленый, иначе false.
     */
    public boolean isStepIndicatorGreen(int stepNumber) {
        WebElement indicator;
        switch (stepNumber) {
            case 1: indicator = step1Indicator; break;
            case 2: indicator = step2Indicator; break;
            case 3: indicator = step3Indicator; break;
            case 4: indicator = step4Indicator; break;
            case 5: indicator = step5Indicator; break;
            default: throw new IllegalArgumentException("Некорректный номер шага: " + stepNumber);
        }
        wait.until(ExpectedConditions.visibilityOf(indicator));
        String backgroundColor = indicator.getCssValue("background-color");
        // Пример: проверям, что фон зеленый (RGB для зеленого может быть, например, rgb(0, 128, 0) или rgb(34, 197, 94) из Tailwind green-500)
        // Также можно проверить наличие класса, например, 'step-active' или 'step-completed'
        return backgroundColor.contains("rgb(34, 197, 94)") || indicator.getAttribute("class").contains("step-active");
    }

    /**
     * Проверяет, что индикатор указанного шага стал серым (неактивным/незаполненным).
     * @param stepNumber Номер шага (1-5).
     * @return true, если индикатор серый, иначе false.
     */
    public boolean isStepIndicatorGray(int stepNumber) {
        WebElement indicator;
        switch (stepNumber) {
            case 1: indicator = step1Indicator; break;
            case 2: indicator = step2Indicator; break;
            case 3: indicator = step3Indicator; break;
            case 4: indicator = step4Indicator; break;
            case 5: indicator = step5Indicator; break;
            default: throw new IllegalArgumentException("Некорректный номер шага: " + stepNumber);
        }
        wait.until(ExpectedConditions.visibilityOf(indicator));
        String backgroundColor = indicator.getCssValue("background-color");
        // Пример: проверям, что фон серый (rgb(209, 213, 219) для Tailwind gray-300)
        return backgroundColor.contains("rgb(209, 213, 219)") || indicator.getAttribute("class").contains("step-inactive");
    }
}
