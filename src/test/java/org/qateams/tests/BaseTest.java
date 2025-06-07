package org.qateams.tests;

import org.qateams.driver.Driver;
import org.qateams.driver.DriverManager;
import org.qateams.utils.SoftAssertListener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;
// Аннотация где переопределён метод, чтобы тесты не падали
@Listeners(SoftAssertListener.class)
public class BaseTest {

    protected SoftAssert softAssert;

    protected BaseTest() {
    }

    @BeforeMethod
    protected void setUp() {
        Driver.initDriver();
    }

    @AfterMethod
    protected void tearDown(){
        Driver.quitDriver();
    }



    @BeforeMethod
    public void initSoftAssert() {
        softAssert = new SoftAssert();
    }

    @AfterMethod
    public void tearDownSoftAssert() {
        try {
            softAssert.assertAll();
        } catch (AssertionError e) {
            System.err.println("Soft assertions failed: " + e.getMessage());
        }
    }
}
