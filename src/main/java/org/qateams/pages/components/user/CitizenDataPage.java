package org.qateams.pages.components.user;

import org.qateams.core.driver.DriverManager;
import org.qateams.pages.components.user.valueobject.CitizenData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select; // Для работы с выпадающими списками
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Данные гражданина - общая
public class CitizenDataPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "citizenLastName")
    private WebElement lastNameField;
    @FindBy(id = "citizenFirstName")
    private WebElement firstNameField;
    @FindBy(id = "citizenMiddleName")
    private WebElement middleNameField;
    @FindBy(id = "citizenAddress")
    private WebElement addressField;
    @FindBy(id = "citizenDateOfBirth")
    private WebElement dateOfBirthField;
    @FindBy(id = "citizenPassportNumber")
    private WebElement passportNumberField;
    @FindBy(id = "citizenGender")
    private WebElement genderDropdownElement;
    private Select genderDropdown;

    @FindBy(id = "nextButton")
    private WebElement nextButton;
    @FindBy(id = "backButton")
    private WebElement backButton;

    // Конструктор теперь не принимает WebDriver
    public CitizenDataPage() {
        this.driver = DriverManager.getDriver(); // Получаем драйвер
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        genderDropdown = new Select(genderDropdownElement);
    }

    /**
     * Заполняет форму данных гражданина, используя Value Object.
     * @param data Объект CitizenData, содержащий данные гражданина.
     */
    public void fillCitizenData(CitizenData data) {
        wait.until(ExpectedConditions.visibilityOf(lastNameField));

        lastNameField.sendKeys(data.getLastName());
        firstNameField.sendKeys(data.getFirstName());
        middleNameField.sendKeys(data.getMiddleName());
        addressField.sendKeys(data.getAddress());
        dateOfBirthField.sendKeys(data.getDateOfBirth());
        passportNumberField.sendKeys(data.getPassportNumber());
        genderDropdown.selectByVisibleText(data.getGender());
    }

    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    public void clickBackButton() {
        wait.until(ExpectedConditions.elementToBeClickable(backButton)).click();
    }

    public boolean isNextButtonEnabled() {
        return nextButton.isEnabled();
    }

    /**
     * Проверяет, подсвечено ли поле красным.
     * @param fieldName Название поля (Фамилия, Имя, Отчество, Адрес прописки, Дата рождения, Номер паспорта, Пол).
     * @return true, если поле подсвечено красным, иначе false.
     */
    public boolean isFieldHighlightedRed(String fieldName) {
        WebElement field;
        switch (fieldName.toLowerCase()) {
            case "фамилия": field = lastNameField; break;
            case "имя": field = firstNameField; break;
            case "отчество": field = middleNameField; break;
            case "адрес прописки": field = addressField; break;
            case "дата рождения": field = dateOfBirthField; break;
            case "номер паспорта": field = passportNumberField; break;
            case "пол": field = genderDropdownElement; break;
            default: throw new IllegalArgumentException("Неизвестное поле для проверки подсветки: " + fieldName);
        }
        String borderColor = field.getCssValue("border-color");
        return borderColor.contains("rgb(255, 0, 0)") || borderColor.contains("#FF0000");
    }
}
