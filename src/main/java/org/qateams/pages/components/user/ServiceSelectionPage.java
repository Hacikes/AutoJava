package org.qateams.pages.components.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Выбор услуги
public class ServiceSelectionPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы кнопок услуг
    @FindBy(id = "marriageRegistrationButton") // Пример ID
    private WebElement marriageRegistrationButton;
    @FindBy(id = "birthRegistrationButton")
    private WebElement birthRegistrationButton;
    @FindBy(id = "deathRegistrationButton")
    private WebElement deathRegistrationButton;

    public ServiceSelectionPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void selectMarriageRegistration() {
        wait.until(ExpectedConditions.elementToBeClickable(marriageRegistrationButton)).click();
    }

    public void selectBirthRegistration() {
        wait.until(ExpectedConditions.elementToBeClickable(birthRegistrationButton)).click();
    }

    public void selectDeathRegistration() {
        wait.until(ExpectedConditions.elementToBeClickable(deathRegistrationButton)).click();
    }
}
