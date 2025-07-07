package org.qateams.pages.components.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Выбор услуги
public class ServiceSelectionPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы кнопок услуг - ОБНОВЛЕНО
    // Теперь находим кнопки по их тексту, так как ID отсутствуют
    private final By marriageRegistrationButtonLocator = By.xpath("//button[contains(@class, 'ub-pst_relative') and contains(., 'Регистрация брака')]");
    private final By birthRegistrationButtonLocator = By.xpath("//button[contains(@class, 'ub-pst_relative') and contains(., 'Регистрация рождения')]");
    private final By deathRegistrationButtonLocator = By.xpath("//button[contains(@class, 'ub-pst_relative') and contains(., 'Регистрация смерти')]");

    public ServiceSelectionPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this); // Инициализация элементов, если есть @FindBy
    }

    // Методы для взаимодействия с кнопками
    public void selectMarriageRegistration() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(marriageRegistrationButtonLocator));
        button.click();
    }

    public void selectBirthRegistration() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(birthRegistrationButtonLocator));
        button.click();
    }

    public void selectDeathRegistration() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(deathRegistrationButtonLocator));
        button.click();
    }

    /**
     * Проверяет, отображается ли кнопка "Регистрация брака".
     * @return true, если кнопка отображается, иначе false.
     */
    public boolean isMarriageRegistrationButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(marriageRegistrationButtonLocator)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    /**
     * Проверяет, отображается ли кнопка "Регистрация рождения".
     * @return true, если кнопка отображается, иначе false.
     */
    public boolean isBirthRegistrationButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(birthRegistrationButtonLocator)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    /**
     * Проверяет, отображается ли кнопка "Регистрация смерти".
     * @return true, если кнопка отображается, иначе false.
     */
    public boolean isDeathRegistrationButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(deathRegistrationButtonLocator)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}