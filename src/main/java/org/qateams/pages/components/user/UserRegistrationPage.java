package org.qateams.pages.components.user;

import org.qateams.core.driver.DriverManager;
import org.qateams.pages.components.user.valueobject.ApplicantData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Optional;

// Данные заявителя
public class UserRegistrationPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = "h2.form-title-hint")
    private WebElement pageTitleHint;

    @FindBy(id = "applicantLastName")
    private WebElement lastNameField;
    @FindBy(id = "applicantFirstName")
    private WebElement firstNameField;
    @FindBy(id = "applicantMiddleName")
    private WebElement middleNameField;
    @FindBy(id = "applicantAddress")
    private WebElement addressField;
    @FindBy(id = "applicantPhone")
    private WebElement phoneField;
    @FindBy(id = "applicantPassportNumber")
    private WebElement passportNumberField;

    @FindBy(id = "nextButton")
    private WebElement nextButton;
    @FindBy(id = "closeButton")
    private WebElement closeButton;

    // Конструктор теперь не принимает WebDriver
    public UserRegistrationPage() {
        this.driver = DriverManager.getDriver(); // Получаем драйвер
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public String getPageTitleHint() {
        wait.until(ExpectedConditions.visibilityOf(pageTitleHint));
        return pageTitleHint.getText();
    }

    /**
     * Заполняет форму данных заявителя, используя Value Object.
     * @param data Объект ApplicantData, содержащий данные заявителя.
     */
    public void fillApplicantData(ApplicantData data) {
        wait.until(ExpectedConditions.visibilityOf(passportNumberField));

        passportNumberField.sendKeys(data.getPassportNumber());

        Optional.ofNullable(data.getLastName()).ifPresent(lastNameField::sendKeys);
        Optional.ofNullable(data.getFirstName()).ifPresent(firstNameField::sendKeys);
        Optional.ofNullable(data.getMiddleName()).ifPresent(middleNameField::sendKeys);
        Optional.ofNullable(data.getAddress()).ifPresent(addressField::sendKeys);
        Optional.ofNullable(data.getPhone()).ifPresent(phoneField::sendKeys);
    }

    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    public void clickCloseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
    }

    public boolean isNextButtonEnabled() {
        return nextButton.isEnabled();
    }

    /**
     * Проверяет, подсвечено ли поле красным (для обязательного поля "Номер паспорта").
     * @return true, если поле подсвечено красным, иначе false.
     */
    public boolean isPassportNumberFieldHighlightedRed() {
        String borderColor = passportNumberField.getCssValue("border-color");
        return borderColor.contains("rgb(255, 0, 0)") || borderColor.contains("#FF0000");
    }
}
