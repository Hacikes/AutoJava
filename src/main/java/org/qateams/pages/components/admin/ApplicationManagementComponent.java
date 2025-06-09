package org.qateams.pages.components.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ApplicationManagementComponent {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Базовый локатор (общий предок)
    private final By tableContainer = By.xpath("//div[contains(@class, 'MuiPaper-root')]");

    // Локаторы таблицы и строк
    private final By applicantTable = By.xpath(".//div/table");
    private final By tableRow = By.xpath(".//tr");
    private final By windowTitle = By.xpath(".//h2/span[contains(text(), 'Вы вошли как') and ./b[text()='Aдминистратор']]");

    // Локаторы столбцов
    private final By columnApplicationNumber = By.xpath(".//td[1]");
    private final By columnApplicant = By.xpath(".//td[2]");
    private final By columnType = By.xpath(".//td[3]");
    private final By columnDate = By.xpath(".//td[4]");
    private final By columnStatus = By.xpath(".//td[5]");
    private final By columnActions = By.xpath(".//td[6]");

    // Локаторы кнопок действий
    private final By buttonApprove = By.xpath(".//td[6]//button[1]");
    private final By buttonReject = By.xpath(".//td[6]//button[2]");

    // Локаторы кнопок управления
    private final By buttonClose = By.xpath(".//div[2]/button[1]");
    private final By buttonRefresh = By.xpath(".//div[2]/button[2]");

    // Локаторы пагинации
    private final By buttonPaginationBack = By.xpath(".//li[1]");
    private final By buttonPaginationNext = By.xpath(".//li[last()]");

    public ApplicationManagementComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Получение всех данных таблицы
    public List<List<String>> getTableData() {
        waitForTableLoaded();

        return driver.findElements(tableRow).stream()
                .map(row -> row.findElements(By.tagName("td"))
                        .stream()
                        .map(cell -> cell.getText().trim())
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }

    // Ожидание полной загрузки таблицы
    private void waitForTableLoaded() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tableRow));
        wait.until(driver -> {
            List<WebElement> rows = driver.findElements(tableRow);
            return !rows.isEmpty() &&
                    rows.stream().allMatch(WebElement::isDisplayed) &&
                    rows.stream().noneMatch(row -> row.findElements(By.tagName("td")).isEmpty());
        });
    }

    // Получение заголовка окна
    public String getWindowTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(windowTitle)).getText();
    }

    // Получение статуса заявки по индексу строки
    public String getStatusText(int rowIndex) {
        By statusLocator = By.xpath(".//tr[" + rowIndex + "]/td[5]");
        WebElement statusElement = driver.findElement(statusLocator);
        return statusElement.getText().trim();
    }

    // Поиск строки по номеру заявки
    public int findRowIndexByApplicationNumber(int applicationNumber) {
        List<WebElement> rows = driver.findElements(tableRow);
        return IntStream.range(0, rows.size())
                .filter(i -> {
                    WebElement numberCell = rows.get(i).findElement(columnApplicationNumber);
                    try {
                        return Integer.parseInt(numberCell.getText().trim()) == applicationNumber;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Заявка с номером " + applicationNumber + " не найдена")) + 1;
    }

    // Получение статуса по номеру заявки
    public String getStatusByApplicationNumber(int applicationNumber) {
        int rowIndex = findRowIndexByApplicationNumber(applicationNumber);
        return getStatusText(rowIndex);
    }

    // Получение номера заявки по индексу строки
    public int getApplicationNumberByRowIndex(int rowIndex) {
        By applicationNumberLocator = By.xpath(".//tr[" + rowIndex + "]/td[1]");
        WebElement numberElement = driver.findElement(applicationNumberLocator);
        return Integer.parseInt(numberElement.getText().trim());
    }

    // Выполнение действия над заявкой
    public void performActionOnApplication(int rowIndex, boolean isApprove) {
        By actionButton = isApprove ?
                By.xpath(".//tr[" + rowIndex + "]/td[6]//button[1]") :
                By.xpath(".//tr[" + rowIndex + "]/td[6]//button[2]");

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(actionButton));
        button.click();

        // Ожидание обновления статуса
        wait.until(ExpectedConditions.stalenessOf(button));
    }

    // Одобрение заявки по номеру
    public void approveApplication(int applicationNumber) {
        int rowIndex = findRowIndexByApplicationNumber(applicationNumber);
        performActionOnApplication(rowIndex, true);
    }

    // Отклонение заявки по номеру
    public void rejectApplication(int applicationNumber) {
        int rowIndex = findRowIndexByApplicationNumber(applicationNumber);
        performActionOnApplication(rowIndex, false);
    }

    // Закрытие модального окна
    public void closeWindow() {
        WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(buttonClose));
        closeButton.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(tableContainer));
    }

    // Обновление таблицы
    public void refreshTable() {
        WebElement refreshButton = wait.until(ExpectedConditions.elementToBeClickable(buttonRefresh));
        refreshButton.click();
        waitForTableLoaded();
    }

    // Навигация по пагинации
    public void navigatePagination(boolean isNext) {
        By paginationButton = isNext ? buttonPaginationNext : buttonPaginationBack;
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(paginationButton));
        button.click();
        waitForTableLoaded();
    }

    // Проверка наличия данных в таблице
    public boolean isTableNotEmpty() {
        waitForTableLoaded();
        return !driver.findElements(tableRow).isEmpty();
    }

    // Получение количества строк в таблице
    public int getTableRowCount() {
        waitForTableLoaded();
        return driver.findElements(tableRow).size();
    }
}
