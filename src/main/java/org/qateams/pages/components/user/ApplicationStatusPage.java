package org.qateams.pages.components.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.core.driver.DriverManager;

import java.time.Duration;

// Статус заявки
public class ApplicationStatusPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = "h2.success-message")
    private WebElement successMessageHeader;
    @FindBy(id = "applicationDate")
    private WebElement applicationDateElement;
    @FindBy(id = "applicationStatus")
    private WebElement applicationStatusElement;

    @FindBy(id = "refreshStatusButton")
    private WebElement refreshStatusButton;
    @FindBy(id = "createNewApplicationButton")
    private WebElement createNewApplicationButton;

    // Конструктор теперь не принимает WebDriver
    public ApplicationStatusPage() {
        this.driver = DriverManager.getDriver(); // Получаем драйвер
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public String getSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOf(successMessageHeader));
        return successMessageHeader.getText();
    }

    public String getApplicationDate() {
        wait.until(ExpectedConditions.visibilityOf(applicationDateElement));
        return applicationDateElement.getText();
    }

    public String getApplicationStatus() {
        wait.until(ExpectedConditions.visibilityOf(applicationStatusElement));
        return applicationStatusElement.getText();
    }

    public void clickRefreshStatusButton() {
        wait.until(ExpectedConditions.elementToBeClickable(refreshStatusButton)).click();
    }

    public void clickCreateNewApplicationButton() {
        wait.until(ExpectedConditions.elementToBeClickable(createNewApplicationButton)).click();
    }
}
