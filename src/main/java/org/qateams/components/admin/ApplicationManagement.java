package org.qateams.components.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.driver.DriverManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ApplicationManagement {

    // Метод для построения относительных XPath по базовому локатору
    private By buildXpath(By baseElement, String relativeXpath) {
        return By.xpath("(" + baseElement.toString().replace("By.xpath: ", "") + ")" + relativeXpath);
    }


    // Базовый локатор (общий предок)
    private final By tableContainer = By.xpath("//div[contains(@class, 'MuiPaper-root')]");
    //    private final By formTitle = buildXpath(formContainer, "//span/descendant-or-self::b");


    // Локаторы таблицы, хедера и строк
    private final By applicantTable = buildXpath(tableContainer, "//div/table");
    private final By windowTitle = buildXpath(tableContainer, "/h2/span[contains(text(), 'Вы вошли как') and ./b[text()='Aдминистратор']]");
    private final By applicantEmptyTable = buildXpath(tableContainer, "//div/text");
    //    private final By tableHeader = buildXpath(applicantTable, "/thead/tr");
    private final By tableRow = buildXpath(applicantTable, "/tr");

    // Локаторы столбцов
    private final By rowApplicationNumber = buildXpath(tableRow, "/td[1]");
    private final By rowApplicant = buildXpath(tableRow, "/td[2]");
    private final By rowType = buildXpath(tableRow, "/td[3]");
    private final By rowDate = buildXpath(tableRow, "/td[4]");
    private final By rowPersonal = buildXpath(tableRow, "/td[5]");
    private final By rowAction = buildXpath(tableRow, "/td[6]");

    // Локатор кнопки действия над заявкой
    private final By buttonApprove = buildXpath(rowAction, "//button[1]");
    private final By buttonReject = buildXpath(rowAction, "//button[2]");

    // Локаторы пагинации
    private final By tablePagination = buildXpath(applicantTable, "/ancestor::div[2]/div[2]");
    private final By buttonPaginationBack = buildXpath(tablePagination, "//li[1]");
    private final By buttonPaginationNext = buildXpath(tablePagination, "//li[last()]");


    // Локаторы кнопок Закрыть и Далее
    private final By buttonClose = buildXpath(tableContainer, "/div[2]/button[1]");
    private final By buttonRefresh = buildXpath(tableContainer, "/div[2]/button[2]");


    // Метод получения всей таблицы
    public List<List<String>> getTableData() {

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));

        // Ожидание присутствия таблицы
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tableRow));

        // Ожидание видимости всех строк
        wait.until(driver -> {
            List<WebElement> rows = driver.findElements(tableRow);
            return !rows.isEmpty() && rows.stream().allMatch(WebElement::isDisplayed);
        });

        // Дополнительная проверка на полную загрузку данных
        wait.until(driver -> {
            List<WebElement> rows = driver.findElements(tableRow);
            return rows.stream()
                    .noneMatch(row -> row.findElements(By.tagName("td")).isEmpty());
        });

        return DriverManager.getDriver()
                .findElements(tableRow)
                .stream()
                .map(row -> row.findElements(By.tagName("td"))
                        .stream()
                        .map(cell -> cell.getText().trim())
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }

    public WebElement getEmptyTableText() {
        return DriverManager.getDriver().findElement(applicantEmptyTable);
    }


    // Методы для получения локаторов ячеек строки (по номеру строки)
    public By getRowNumber(int rowIndex) {

        return buildXpath(tableRow,"[" + rowIndex + "]/td[1]");
    }

    // Метод получения номера заявки в (по номеру строки)
    public int getApplicationNumber(int rowIndex) {
        By applicationNumber = getRowNumber(rowIndex);
        System.out.println(applicationNumber);
        WebElement numberElement = DriverManager.getDriver().findElement(applicationNumber);
        String tempString = numberElement.getText().trim();
        return Integer.parseInt(tempString);
    }
    
//
//    public By getRowApplicant(int rowIndex) {
//        return buildXpath(tableRow, "[" + rowIndex + "]/td[2]");
//    }
//
//    public By getRowType(int rowIndex) {
//        return buildXpath(tableRow, "[" + rowIndex + "]/td[3]");
//    }
//
//    public By getRowDate(int rowIndex) {
//        return buildXpath(tableRow, "[" + rowIndex + "]/td[4]");
//    }
//
    public By getRowStatus(int rowIndex) {
        return buildXpath(tableRow, "[" + rowIndex + "]/td[5]");
    }

    public String getStatusText(int rowIndex) {
        By statusLocator = getRowStatus(rowIndex);
        WebElement statusElement = DriverManager.getDriver().findElement(statusLocator);
        return statusElement.getText().trim(); // trim() убирает лишние пробелы
    }

    //    Поиск строки по номеру заявки
    public int findRowIndexByApplicationNumber(int applicationNumber) {
        List<WebElement> rows = DriverManager.getDriver().findElements(tableRow);
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

//
//    public By getRowAction(int rowIndex) {
//        return buildXpath(tableRow, "[" + rowIndex + "]/td[6]");
//    }


    // Кнопки действий
    public By getButtonApprove(int rowIndex) {
        return buildXpath(tableRow, "[" + rowIndex + "]/td[6]//button[1]");
    }

    public void clickApproveButton(int rowIndex) {
        By buttonLocator = getButtonApprove(rowIndex);
        DriverManager.getDriver().findElement(buttonLocator).click();
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


    public WebElement getCloseButton() {
        return DriverManager.getDriver().findElement(buttonClose);
    }

    public WebElement getRefreshButton() {
         return DriverManager.getDriver().findElement(buttonClose);
    }

    // Кнопки закрыть и обновить
//    public void clickCloseButton() {
//        DriverManager.getDriver().findElement(buttonClose).click();
//    }
    public void clickCloseButton() {
        WebElement closeButton = DriverManager.getDriver().findElement(buttonClose);
        closeButton.click();

        // Явное ожидание с условием
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(1))
                .until(ExpectedConditions.invisibilityOf(closeButton));
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


    public void paginationButton(boolean direction) {
        By actionDirection = direction
                ? buttonPaginationBack
                : buttonPaginationNext;
        DriverManager.getDriver().findElement(actionDirection).click();
    }



}
