package org.qateams.pages.components.user;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.core.driver.DriverManager;
import org.qateams.pages.components.user.valueobject.CitizenData;
import org.qateams.utils.InputValidationComponent;

import java.time.Duration;
import java.util.Optional;

// Данные гражданина - общая
public class CitizenDataPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final InputValidationComponent inputValidationComponent;

    // --- УЛУЧШЕННЫЕ ЛОКАТОРЫ ПОЛЕЙ ВВОДА ---
    private WebElement getInputFieldByLabel(String labelText) {
        WebElement label = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//label[contains(text(), '" + labelText + "')]")));
        String inputId = label.getAttribute("for");
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(inputId)));
    }

    public WebElement lastNameField() { return getInputFieldByLabel("Фамилия"); }
    public WebElement firstNameField() { return getInputFieldByLabel("Имя"); }
    public WebElement middleNameField() { return getInputFieldByLabel("Отчество"); }
    public WebElement dateOfBirthField() { return getInputFieldByLabel("Дата рождения"); }
    public WebElement passportNumberField() { return getInputFieldByLabel("Номер паспорта"); }
    public WebElement genderField() { return getInputFieldByLabel("Пол"); }
    public WebElement addressField() { return getInputFieldByLabel("Адрес прописки"); }

    // --- УЛУЧШЕННЫЕ ЛОКАТОРЫ КНОПОК ---
    @FindBy(xpath = "//button[.//svg[@data-icon='arrow-right']]")
    private WebElement nextButton;
    @FindBy(xpath = "//button[.//svg[@data-icon='arrow-left']]")
    private WebElement backButton;
    @FindBy(xpath = "//div[contains(@class, 'MuiDialogActions-root')]/button[1]")
    private WebElement closeButton;


    public CitizenDataPage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        this.inputValidationComponent = new InputValidationComponent();
    }

    public void fillCitizenData(CitizenData data) {
        wait.until(ExpectedConditions.visibilityOf(lastNameField()));

        Optional.ofNullable(data.getLastName()).ifPresent(val -> { lastNameField().clear(); lastNameField().sendKeys(val); });
        Optional.ofNullable(data.getFirstName()).ifPresent(val -> { firstNameField().clear(); firstNameField().sendKeys(val); });
        Optional.ofNullable(data.getMiddleName()).ifPresent(val -> { middleNameField().clear(); middleNameField().sendKeys(val); });
        Optional.ofNullable(data.getAddress()).ifPresent(val -> { addressField().clear(); addressField().sendKeys(val); });
        Optional.ofNullable(data.getDateOfBirth()).ifPresent(val -> { dateOfBirthField().clear(); dateOfBirthField().sendKeys(val); });
        Optional.ofNullable(data.getPassportNumber()).ifPresent(val -> { passportNumberField().clear(); passportNumberField().sendKeys(val); });
        Optional.ofNullable(data.getGender()).ifPresent(val -> { genderField().clear(); genderField().sendKeys(val); });
    }

    public void clearLastNameField() { lastNameField().clear(); }
    public void clearFirstNameField() { firstNameField().clear(); }
    public void clearMiddleNameField() { middleNameField().clear(); }
    public void clearAddressField() { addressField().clear(); }
    public void clearDateOfBirthField() { dateOfBirthField().sendKeys(Keys.CONTROL + "a", Keys.DELETE); }
    public void clearPassportNumberField() { passportNumberField().clear(); }
    public void clearGenderField() { genderField().clear(); }


    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    public void clickBackButton() {
        wait.until(ExpectedConditions.elementToBeClickable(backButton)).click();
    }

    public void clickCloseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
    }

    public boolean isNextButtonEnabled() {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(nextButton)).isEnabled();
        } catch (org.openqa.selenium.TimeoutException | org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isFieldHighlightedRed(String fieldName) {
        String inputId;
        switch (fieldName.toLowerCase()) {
            case "фамилия": inputId = lastNameField().getAttribute("id"); break;
            case "имя": inputId = firstNameField().getAttribute("id"); break;
            case "отчество": inputId = middleNameField().getAttribute("id"); break;
            case "дата рождения": inputId = dateOfBirthField().getAttribute("id"); break;
            case "номер паспорта": inputId = passportNumberField().getAttribute("id"); break;
            case "пол": inputId = genderField().getAttribute("id"); break;
            case "адрес прописки": inputId = addressField().getAttribute("id"); break;
            default: throw new IllegalArgumentException("Неизвестное поле для проверки подсветки: " + fieldName);
        }
        return inputValidationComponent.isFieldHighlightedInRed(By.id(inputId));
    }

    /**
     * Кликает на свободную область модального окна для активации валидации полей.
     * ОБНОВЛЕНО: Теперь кликает на основную "бумажную" область модального окна.
     */
    public void clickOnFreeArea() {
        WebElement modalPaper = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("MuiDialog-paper")));
        modalPaper.click();
    }
}