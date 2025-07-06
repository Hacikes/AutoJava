package org.qateams.pages.components.user;

import org.qateams.core.driver.DriverManager;
import org.qateams.pages.components.user.valueobject.DeathServiceData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Данные услуги для смерти
public class DeathServiceDataPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "deathDateOfDeath")
    private WebElement dateOfDeathField;
    @FindBy(id = "deathPlaceOfDeath")
    private WebElement placeOfDeathField;

    @FindBy(id = "completeButton")
    private WebElement completeButton;

    // Конструктор теперь не принимает WebDriver
    public DeathServiceDataPage() {
        this.driver = DriverManager.getDriver(); // Получаем драйвер
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Заполняет форму данных услуги "Регистрация смерти", используя Value Object.
     * @param data Объект DeathServiceData.
     */
    public void fillDeathServiceData(DeathServiceData data) {
        wait.until(ExpectedConditions.visibilityOf(dateOfDeathField));

        dateOfDeathField.sendKeys(data.getDateOfDeath());
        placeOfDeathField.sendKeys(data.getPlaceOfDeath());
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
            case "дата смерти": field = dateOfDeathField; break;
            case "место смерти": field = placeOfDeathField; break;
            default: throw new IllegalArgumentException("Неизвестное поле для проверки подсветки: " + fieldName);
        }
        String borderColor = field.getCssValue("border-color");
        return borderColor.contains("rgb(255, 0, 0)") || borderColor.contains("#FF0000");
    }
}
