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
import org.qateams.pages.components.user.valueobject.MarriageServiceData;
import org.qateams.utils.InputValidationComponent;

import java.time.Duration;
import java.util.Optional;

// Данные услуги для брака
public class MarriageServiceDataPage {
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

    // Поля ввода (теперь инициализируются через getInputFieldByLabel в методах)
    public WebElement registrationDateField() { return getInputFieldByLabel("Дата регистрации"); }
    public WebElement newLastNameField() { return getInputFieldByLabel("Новая фамилия"); } // Необязательное поле
    public WebElement spouseLastNameField() { return getInputFieldByLabel("Фамилия супруга/и"); }
    public WebElement spouseFirstNameField() { return getInputFieldByLabel("Имя супруга/и"); }
    public WebElement spouseMiddleNameField() { return getInputFieldByLabel("Отчество супруга/и"); }
    public WebElement spouseDateOfBirthField() { return getInputFieldByLabel("Дата рождения супруга/и"); } // Необязательное поле
    public WebElement spousePassportNumberField() { return getInputFieldByLabel("Номер паспорта супруга/и"); }

    // --- УЛУЧШЕННЫЕ ЛОКАТОРЫ КНОПОК ---
    // В HTML-коде кнопки находятся внутри <div class="MuiDialogActions-root">
    // "Завершить" - это последняя кнопка в этом контейнере.
    @FindBy(xpath = "//div[contains(@class, 'MuiDialogActions-root')]/button[last()]")
    private WebElement completeButton;

    // "Назад" - это предпоследняя кнопка в этом контейнере.
    @FindBy(xpath = "//div[contains(@class, 'MuiDialogActions-root')]/button[last()-1]")
    private WebElement backButton;

    // "Закрыть" - это первая кнопка в этом контейнере.
    @FindBy(xpath = "//div[contains(@class, 'MuiDialogActions-root')]/button[1]")
    private WebElement closeButton;


    public MarriageServiceDataPage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        this.inputValidationComponent = new InputValidationComponent();
    }

    /**
     * Заполняет форму данных услуги "Регистрация брака", используя Value Object.
     * Поля очищаются перед вводом.
     * @param data Объект MarriageServiceData.
     */
    public void fillMarriageServiceData(MarriageServiceData data) {
        wait.until(ExpectedConditions.visibilityOf(registrationDateField())); // Ждем видимости первого поля

        Optional.ofNullable(data.getRegistrationDate()).ifPresent(val -> { registrationDateField().sendKeys(Keys.CONTROL + "a", Keys.DELETE); registrationDateField().sendKeys(val); });
        Optional.ofNullable(data.getNewLastName()).ifPresent(val -> { newLastNameField().clear(); newLastNameField().sendKeys(val); });
        Optional.ofNullable(data.getSpouseLastName()).ifPresent(val -> { spouseLastNameField().clear(); spouseLastNameField().sendKeys(val); });
        Optional.ofNullable(data.getSpouseFirstName()).ifPresent(val -> { spouseFirstNameField().clear(); spouseFirstNameField().sendKeys(val); });
        Optional.ofNullable(data.getSpouseMiddleName()).ifPresent(val -> { spouseMiddleNameField().clear(); spouseMiddleNameField().sendKeys(val); });
        Optional.ofNullable(data.getSpouseDateOfBirth()).ifPresent(val -> { spouseDateOfBirthField().sendKeys(Keys.CONTROL + "a", Keys.DELETE); spouseDateOfBirthField().sendKeys(val); });
        Optional.ofNullable(data.getSpousePassportNumber()).ifPresent(val -> { spousePassportNumberField().clear(); spousePassportNumberField().sendKeys(val); });
    }

    // Методы для очистки полей
    public void clearRegistrationDateField() { registrationDateField().sendKeys(Keys.CONTROL + "a", Keys.DELETE); }
    public void clearNewLastNameField() { newLastNameField().clear(); }
    public void clearSpouseLastNameField() { spouseLastNameField().clear(); }
    public void clearSpouseFirstNameField() { spouseFirstNameField().clear(); }
    public void clearSpouseMiddleNameField() { spouseMiddleNameField().clear(); }
    public void clearSpouseDateOfBirthField() { spouseDateOfBirthField().sendKeys(Keys.CONTROL + "a", Keys.DELETE); }
    public void clearSpousePassportNumberField() { spousePassportNumberField().clear(); }


    public void clickCompleteButton() {
        wait.until(ExpectedConditions.elementToBeClickable(completeButton)).click();
    }

    public void clickBackButton() {
        wait.until(ExpectedConditions.elementToBeClickable(backButton)).click();
    }

    public void clickCloseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
    }

    /**
     * Проверяет, доступна ли кнопка "Завершить" для клика.
     * @return true, если кнопка "Завершить" активна, иначе false.
     */
    public boolean isCompleteButtonEnabled() {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(completeButton)).isEnabled();
        } catch (org.openqa.selenium.TimeoutException | org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Проверяет, подсвечено ли поле красным, используя InputValidationComponent.
     *
     * @param fieldName Название поля (Дата регистрации, Фамилия супруга/и, Имя супруга/и, Отчество супруга/и, Дата рождения супруга/и, Номер паспорта супруга/и).
     * @return true, если поле подсвечено красным, иначе false.
     */
    public boolean isFieldHighlightedRed(String fieldName) {
        String inputId;
        switch (fieldName.toLowerCase()) {
            case "дата регистрации": inputId = registrationDateField().getAttribute("id"); break;
            case "новая фамилия": inputId = newLastNameField().getAttribute("id"); break; // Необязательное поле
            case "фамилия супруга": inputId = spouseLastNameField().getAttribute("id"); break;
            case "имя супруга": inputId = spouseFirstNameField().getAttribute("id"); break;
            case "отчество супруга": inputId = spouseMiddleNameField().getAttribute("id"); break;
            case "дата рождения супруга": inputId = spouseDateOfBirthField().getAttribute("id"); break; // Необязательное поле
            case "номер паспорта супруга": inputId = spousePassportNumberField().getAttribute("id"); break;
            default: throw new IllegalArgumentException("Неизвестное поле для проверки подсветки: " + fieldName);
        }
        return inputValidationComponent.isFieldHighlightedInRed(By.id(inputId));
    }

    /**
     * Кликает на свободную область модального окна для активации валидации полей.
     */
    public void clickOnFreeArea() {
        WebElement modalPaper = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("MuiDialog-paper")));
        modalPaper.click();
    }
}