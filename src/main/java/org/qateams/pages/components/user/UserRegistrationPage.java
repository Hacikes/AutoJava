package org.qateams.pages.components.user;

import org.openqa.selenium.Keys;
import org.qateams.core.driver.DriverManager;
import org.qateams.pages.components.common.StepIndicatorComponent;
import org.qateams.pages.components.user.valueobject.ApplicantData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.utils.InputValidationComponent;

import java.time.Duration;
import java.util.Optional;

// Данные заявителя
public class UserRegistrationPage {

    private final WebDriver driver;
    public final WebDriverWait wait;
    private final InputValidationComponent inputValidationComponent;
    private final StepIndicatorComponent stepIndicatorComponent; // <-- Добавляем экземпляр компонента

    // Заголовок модального окна (текст "Вы вошли как Пользоватиль")
    @FindBy(xpath = "//h2[contains(@class, 'MuiDialogTitle-root')]/span")
    private WebElement pageTitleHint;

    // Текст выбранной услуги
    @FindBy(xpath = "//span[contains(text(), 'Вы выбрали услугу:')]")
    private WebElement selectedServiceText;

    // Поля ввода данных заявителя
    @FindBy(id = "TextInputField-1") // Фамилия
    public WebElement lastNameField;

    @FindBy(id = "TextInputField-2") // Имя
    public WebElement firstNameField;

    @FindBy(id = "TextInputField-3") // Отчество
    public WebElement middleNameField;

    @FindBy(id = "TextInputField-4") // Телефон
    public WebElement phoneField;

    @FindBy(id = "TextInputField-5") // Номер паспорта
    public WebElement passportNumberField;

    @FindBy(id = "TextInputField-6") // Адрес прописки
    public WebElement addressField;

    // Кнопки
    @FindBy(xpath = "//button[contains(., 'Далее') and not(contains(@class, 'Mui-disabled'))]")
    private WebElement nextButton;

    @FindBy(xpath = "//button[contains(., 'Закрыть')]")
    private WebElement closeButton;

    public UserRegistrationPage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        this.inputValidationComponent = new InputValidationComponent();
        this.stepIndicatorComponent = new StepIndicatorComponent(); // <-- Инициализируем компонент
    }

    public String getPageTitleHint() {
        wait.until(ExpectedConditions.visibilityOf(pageTitleHint));
        return pageTitleHint.getText();
    }

    public String getSelectedServiceText() {
        wait.until(ExpectedConditions.visibilityOf(selectedServiceText));
        return selectedServiceText.getText();
    }

    /**
     * Заполняет форму данных заявителя, используя Value Object.
     * Поля очищаются перед вводом.
     * @param data Объект ApplicantData, содержащий данные заявителя.
     */
    public void fillApplicantData(ApplicantData data) {
        wait.until(ExpectedConditions.visibilityOf(passportNumberField));

        Optional.ofNullable(data.getLastName()).ifPresent(val -> { lastNameField.clear(); lastNameField.sendKeys(val); });
        Optional.ofNullable(data.getFirstName()).ifPresent(val -> { firstNameField.clear(); firstNameField.sendKeys(val); });
        Optional.ofNullable(data.getMiddleName()).ifPresent(val -> { middleNameField.clear(); middleNameField.sendKeys(val); });
        Optional.ofNullable(data.getPhone()).ifPresent(val -> { phoneField.clear(); phoneField.sendKeys(val); });
        Optional.ofNullable(data.getPassportNumber()).ifPresent(val -> { passportNumberField.clear(); passportNumberField.sendKeys(val); });
        Optional.ofNullable(data.getAddress()).ifPresent(val -> { addressField.clear(); addressField.sendKeys(val); });
    }

    // Методы для очистки полей
    public void clearLastNameField() { lastNameField.clear(); }
    public void clearFirstNameField() { firstNameField.clear(); }
    public void clearMiddleNameField() { middleNameField.clear(); }
    public void clearPhoneField() { phoneField.clear(); }
    public void clearPassportNumberField() {
        passportNumberField.sendKeys(Keys.CONTROL + "a");
        passportNumberField.sendKeys(Keys.DELETE);
    }
    public void clearAddressField() { addressField.clear(); }

    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    public void clickCloseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
    }

    public boolean isNextButtonEnabled() {
        try {
            return nextButton.isEnabled();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isFieldHighlightedRed(String fieldName) {
        By fieldLocator;
        switch (fieldName.toLowerCase()) {
            case "фамилия": fieldLocator = By.id("TextInputField-1"); break;
            case "имя": fieldLocator = By.id("TextInputField-2"); break;
            case "отчество": fieldLocator = By.id("TextInputField-3"); break;
            case "телефон": fieldLocator = By.id("TextInputField-4"); break;
            case "номер паспорта": fieldLocator = By.id("TextInputField-5"); break;
            case "адрес прописки": fieldLocator = By.id("TextInputField-6"); break;
            default: throw new IllegalArgumentException("Неизвестное поле для проверки подсветки: " + fieldName);
        }
        return inputValidationComponent.isFieldHighlightedInRed(fieldLocator);
    }

    public void clickOnFreeArea() {
        WebElement freeArea = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains(@class, 'MuiDialogTitle-root')]/span")));
        freeArea.click();
    }

    /**
     * Возвращает экземпляр StepIndicatorComponent для работы с индикаторами шагов.
     * @return StepIndicatorComponent.
     */
    public StepIndicatorComponent getStepIndicatorComponent() {
        return stepIndicatorComponent;
    }
}
