package org.qateams.tests.admintests;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.qateams.components.admin.ApplicationManagement;
import org.qateams.constansts.ApplicationStatus;
import org.qateams.driver.DriverManager;
import org.qateams.tests.BaseTest;
import org.qateams.utils.TestUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;


public class ApplicationManagementTest extends BaseTest {

    private ApplicationManagement am;
//    SoftAssert softAssert = new SoftAssert();


    @BeforeMethod
    public void setup() {
        // Беру данные для формы из TestUtils.prepareAdminData()
        TestUtils.prepareAdminData().clickNextButton();
        am = new ApplicationManagement();
    }


    private ApplicationManagementTest() {
    }

    // Проверка на заполненность таблицы
    @Test
    public void testAllCellsAreFilled() {

        // Получаю таблицу с заявками
        List<List<String>> actualData = am.getTableData();

        actualData.forEach(row ->
                IntStream.range(0, row.size() - 1)
                        .forEach(colIndex ->
                                softAssert.assertFalse(row.get(colIndex).trim().isEmpty(),
                                        "Ячейка [" + row.indexOf(row) + "," + colIndex + "] пуста!")
                        )
        );
//        softAssert.assertAll("Ошибка получения таблицы заявок");
    }


    // Одобрение и отклонение заявки
    @Test
    public void testApplicantApprove() {
        performApplicationAction(true);
    }


    // Отклонение заявки
    @Test
    public void testApplicantReject() {
        performApplicationAction(false);
    }


    // Метод нажатия на кнопки одобрения или отклонения и проверки актуального статуса после нажатия
    private void performApplicationAction(boolean isApprove) {
        List<List<String>> table = am.getTableData();
        int randomRowIndex = new Random().nextInt(table.size());

        int changedApplicationNumber = am.getApplicationNumber(randomRowIndex);
        am.performActionOnApplication(randomRowIndex, isApprove);

        String expectedStatus = isApprove
                ? ApplicationStatus.APPROVED.getRussianName()
                : ApplicationStatus.REJECTED.getRussianName();

        String actualStatus = am.getStatusByApplicationNumber(changedApplicationNumber);
        softAssert.assertEquals(actualStatus, expectedStatus);
//        softAssert.assertAll("Ошибка при одобрении или отклонении заявки");
    }


    // Проверка обновление таблицы
    @Test
    public void testRefreshButton() {

        // Сохраняем initial состояние таблицы
        List<List<String>> initialTableData = am.getTableData();

        // Выполняем обновление
        am.clickRefreshButton();

        // Получаем данные после обновления
        List<List<String>> refreshedTableData = am.getTableData();
//

        softAssert.assertNotNull(refreshedTableData, "Таблица после обновления не должна быть null");
        softAssert.assertFalse(refreshedTableData.isEmpty(), "Таблица после обновления не должна быть пустой");
        softAssert.assertAll("Ошибка при обновлении таблицы заявок");


    }


    // Проверка закрытия модалки на кнопку "Закрыть"
    @Test
    public void clickCloseButton() {
        am.clickCloseButton();

        try {
            WebElement amWindow = am.getWindowTitle();
            softAssert.assertFalse(amWindow.isDisplayed(), "Окно должно быть закрыто");
        } catch (NoSuchElementException e) {
            // Если элемент не найден, это означает, что окно закрыто
            softAssert.assertTrue(true, "Окно успешно закрыто");
        }

//        softAssert.assertAll();
    }


    // Проверка отображения и активности кнопки
    @Test
    public void testButtonsVisibilityAndEnabled() {
//        SoftAssert softAssert = new SoftAssert();

        // Проверка кнопки обновления
        WebElement refreshButton = am.getRefreshButton();
        softAssert.assertTrue(refreshButton.isDisplayed(), "Кнопка обновления должна быть видима");
        softAssert.assertTrue(refreshButton.isEnabled(), "Кнопка обновления должна быть активна");

        // Проверка кнопки закрытия
        WebElement closeButton = am.getRefreshButton();
        softAssert.assertTrue(closeButton.isDisplayed(), "Кнопка закрытия должна быть видима");
        softAssert.assertTrue(closeButton.isEnabled(), "Кнопка закрытия должна быть активна");

//        softAssert.assertAll();
    }

    // Проверка доступности нажатия на кнопки пагинации
//    @Test
//    public void testPaginationDisplayed(){
//        int applicantCount = am.getTableData().size();
//        System.out.println(applicantCount);
//        if(applicantCount < 200) {
//            softAssert.assertEquals();
//        }
//    }

}


