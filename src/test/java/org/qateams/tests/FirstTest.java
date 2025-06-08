package org.qateams.tests;

import org.openqa.selenium.By;
import org.qateams.core.driver.DriverManager;
import org.testng.annotations.Test;


public final class FirstTest extends BaseTest {

    private FirstTest() {
        super();
    }

    @Test
    public void test2() {
        DriverManager.getDriver().findElement(By.cssSelector("body > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(3) > button:nth-child(3)")).click();
    }

    @Test
    public void test3() {
        DriverManager.getDriver().findElement(By.cssSelector("body > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(3) > button:nth-child(3)")).click();
    }

}
