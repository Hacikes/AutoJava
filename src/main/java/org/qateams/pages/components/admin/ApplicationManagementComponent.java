package org.qateams.pages.components.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.core.driver.DriverManager;

import java.time.Duration;
import java.util.List;

public class ApplicationManagementComponent {
    private final ApplicationTableComponent tableComponent = new ApplicationTableComponent();

    // Базовый метод построения XPath
    private By buildXpath(By baseElement, String relativeXpath) {
        return By.xpath("(" + baseElement.toString().replace("By.xpath: ", "") + ")" + relativeXpath);
    }

    // Базовый локатор (общий предок)
    private final By tableContainer = By.xpath("//div[contains(@class, 'MuiPaper-root')]");
    private final By windowTitle = buildXpath(tableContainer, "/h2/span[contains(text(), 'Вы вошли как') and ./b[text()='Aдминистратор']]");

    // Локаторы таблицы и кнопок
    private final By tableRow = buildXpath(By.xpath("//div/table"), "/tr");
    private final By buttonClose = buildXpath(tableContainer, "/div[2]/button[1]");
    private final By buttonRefresh = buildXpath(tableContainer, "/div[2]/button[2]");
    private final By tablePagination = buildXpath(By.xpath("//div/table"), "/ancestor::div[2]/div[2]");
    private final By buttonPaginationBack = buildXpath(tablePagination, "//li[1]");
    private final By buttonPaginationNext = buildXpath(tablePagination, "//li[last()]");

    // Кнопки действий
    public By getButtonApprove(int rowIndex) {
        return buildXpath(tableRow, "[" + rowIndex + "]/td[6]//button[1]");
    }

    public By getButtonReject(int rowIndex) {
        return buildXpath(tableRow, "[" + rowIndex + "]/td[6]//button[2]");
    }

    public void performActionOnApplication(int rowIndex, boolean isApprove) {
        By actionButton = isApprove
                ? getButtonApprove(rowIndex)
                : getButtonReject(rowIndex);

        DriverManager.getDriver().findElement(actionButton).click();
    }

    public void clickApproveButton(int rowIndex) {
        By buttonLocator = getButtonApprove(rowIndex);
        DriverManager.getDriver().findElement(buttonLocator).click();
    }

    public WebElement getCloseButton() {
        return DriverManager.getDriver().findElement(buttonClose);
    }

    public void clickCloseButton() {
        DriverManager.getDriver().findElement(buttonClose).click();
    }

    public WebElement getRefreshButton() {
        return DriverManager.getDriver().findElement(buttonRefresh);
    }

    public void clickRefreshButton() {
        DriverManager.getDriver().findElement(buttonRefresh).click();
    }

    // Всё модальное окно
    public WebElement applicantManagementWindow() {
        return DriverManager.getDriver().findElement(tableContainer);
    }

    // Получение заголовка модального окна
    public WebElement getWindowTitle() {
        return DriverManager.getDriver().findElement(windowTitle);
    }

    // Пагинация
    public WebElement getPaginationButton(boolean isNextButton) {
        By actionDirection = isNextButton
                ? buttonPaginationNext
                : buttonPaginationBack;
        return DriverManager.getDriver().findElement(actionDirection);
    }

    public void clickPaginationButton(boolean isNextButton) {
        getPaginationButton(isNextButton).click();
    }

    // Проверка доступности кнопки для нажатия
    public WebElement checkedPaginationButtonFocus(boolean isNextButton) {
        WebElement paginationButton = getPaginationButton(isNextButton);
        return paginationButton.findElement(By.xpath(".//button"));
    }

    // Делегирование методов из TableComponent
    public List<List<String>> getTableData() {
        return tableComponent.getTableData();
    }

    public String getStatusByApplicationNumber(int applicationNumber) {
        return tableComponent.getStatusByApplicationNumber(applicationNumber);
    }
}
