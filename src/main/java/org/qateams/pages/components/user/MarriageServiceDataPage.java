package org.qateams.pages.components.user;

import org.qateams.core.driver.DriverManager;
import org.qateams.pages.components.user.valueobject.MarriageServiceData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Данные услуги для брака
public class MarriageServiceDataPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "marriageRegistrationDate")
    private WebElement registrationDateField;
    @FindBy(id = "spouseLastName")
    private WebElement spouseLastNameField;
    @FindBy(id = "spouseFirstName")
    private WebElement spouseFirstNameField;
    @FindBy(id = "spouseMiddleName")
    private WebElement spouseMiddleNameField;
    @FindBy(id = "spouseDateOfBirth")
    private WebElement spouseDateOfBirthField;
    @FindBy(id = "spousePassportNumber")
    private WebElement spousePassportNumberField;

    @FindBy(id = "completeButton")
    private WebElement completeButton;

    // Конструктор теперь не принимает WebDriver
    public MarriageServiceDataPage() {
        this.driver = DriverManager.getDriver(); // Получаем драйвер
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Заполняет форму данных услуги "Регистрация брака", используя Value Object.
     * @param data Объект MarriageServiceData.
     */
    public void fillMarriageServiceData(MarriageServiceData data) {
        wait.until(ExpectedConditions.visibilityOf(registrationDateField));

        registrationDateField.sendKeys(data.getRegistrationDate());
        spouseLastNameField.sendKeys(data.getSpouseLastName());
        spouseFirstNameField.sendKeys(data.getSpouseFirstName());
        spouseMiddleNameField.sendKeys(data.getSpouseMiddleName());
        spouseDateOfBirthField.sendKeys(data.getSpouseDateOfBirth());
        spousePassportNumberField.sendKeys(data.getSpousePassportNumber());
    }

    public void clickCompleteButton() {
        wait.until(ExpectedConditions.elementToBeClickable(completeButton)).click();
    }

    public boolean isCompleteButtonEnabled() {
        return completeButton.isEnabled();
    }

    /**
     * Проверяет, подсвечено ли поле красным.
     * @param fieldName Название поля.
     * @return true, если поле подсвечено красным, иначе false.
     */
    public boolean isFieldHighlightedRed(String fieldName) {
        WebElement field;
        switch (fieldName.toLowerCase()) {
            case "дата регистрации": field = registrationDateField; break;
            case "фамилия супруга": field = spouseLastNameField; break;
            case "имя супруга": field = spouseFirstNameField; break;
            case "отчество супруги": field = spouseMiddleNameField; break;
            case "дата рождения супруга": field = spouseDateOfBirthField; break;
            case "номер паспорта супруги": field = spousePassportNumberField; break;
            default: throw new IllegalArgumentException("Неизвестное поле для проверки подсветки: " + fieldName);
        }
        String borderColor = field.getCssValue("border-color");
        return borderColor.contains("rgb(255, 0, 0)") || borderColor.contains("#FF0000");
    }

}
