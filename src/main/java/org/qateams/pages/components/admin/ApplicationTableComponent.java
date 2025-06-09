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

public class ApplicationTableComponent {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Базовый локатор (общий предок)
    private final By tableContainer = By.xpath("//div[contains(@class, 'MuiPaper-root')]");

    // Локаторы таблицы и строк
    private final By applicantTable = By.xpath(".//div/table");
    private final By tableRow = By.xpath(".//tr");
    private final By emptyTableText = By.xpath(".//div/text");

    // Локаторы кнопок
    private final By buttonClose = By.xpath(".//div[2]/button[1]");
    private final By buttonRefresh = By.xpath(".//div[2]/button[2]");
    private final By buttonPaginationBack = By.xpath(".//div[2]//li[1]");
    private final By buttonPaginationNext = By.xpath(".//div[2]//li[last()]");

    public ApplicationTableComponent(WebDriver driver) {
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
                    WebElement numberCell = rows.get(i).findElement(By.xpath("./td[1]"));
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

    // Выполнение действия над заявкой
    public void performActionOnApplication(int rowIndex, boolean isApprove) {
        By actionButton = By.xpath(".//tr[" + rowIndex + "]/td[6]//button[" + (isApprove ? 1 : 2) + "]");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(actionButton));
        button.click();
    }

    // Закрытие модального окна
    public void clickCloseButton() {
        WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(buttonClose));
        closeButton.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(tableContainer));
    }

    // Обновление таблицы
    public void clickRefreshButton() {
        WebElement refreshButton = wait.until(ExpectedConditions.elementToBeClickable(buttonRefresh));
        refreshButton.click();
        waitForTableLoaded();
    }

    // Навигация по пагинации
    public void clickPaginationButton(boolean isNextButton) {
        By paginationButton = isNextButton ? buttonPaginationNext : buttonPaginationBack;
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(paginationButton));
        button.click();
        waitForTableLoaded();
    }
}
