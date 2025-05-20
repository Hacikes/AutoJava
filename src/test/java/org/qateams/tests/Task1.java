//package org.qateams.tests;
//
//import org.junit.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.qateams.utils.ReadProperties;
//
//import java.util.concurrent.TimeUnit;
//
//public class Task1 {
//
//    @Test
//    public void main(){
//        // Инициализация драйвера
//        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + ReadProperties.getProperty("chromedriver"));
//        WebDriver driver = new ChromeDriver();
//        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        driver.get(ReadProperties.getProperty("startpage"));
//
//        // Войти как пользователь
//        driver.findElement(By.cssSelector("button:nth-child(1)")).sendKeys(Keys.ENTER);
//
//        // Форма "Данные заявителя"
//        driver.findElement(By.cssSelector("#TextInputField-1")).sendKeys("Иванов");
//        driver.findElement(By.cssSelector("#TextInputField-2")).sendKeys("Иван", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-3")).sendKeys("Иванович", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-4")).sendKeys("89990009900", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-5")).sendKeys("112233", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-6")).sendKeys("г. Москва, ул. Ленина, д. 28, кв 33", Keys.TAB);
//        driver.findElement(By.cssSelector("div[role='presentation'] button:nth-child(2)")).click();
//
//        // Выбор услуги
//        driver.findElement(By.cssSelector("div[role='presentation'] div:nth-child(2) button:nth-child(1)")).sendKeys(Keys.ENTER);
//
//        // Форма "Данные гражданина"
//        driver.findElement(By.cssSelector("#TextInputField-7")).sendKeys("Иванов");
//        driver.findElement(By.cssSelector("#TextInputField-8")).sendKeys("Иван", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-9")).sendKeys("Иванович", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-10")).sendKeys("01.04.2025", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-11")).sendKeys("112233", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-12")).sendKeys("Муж", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-13")).sendKeys("г. Москва, ул. Ленина, д. 28, кв 33", Keys.TAB);
//        driver.findElement(By.cssSelector("div[role='presentation'] button:nth-child(3)")).click();
//
//        // Данные услуги
//        driver.findElement(By.cssSelector("#TextInputField-14")).sendKeys("01.04.2025");
//        driver.findElement(By.cssSelector("#TextInputField-15")).sendKeys("Иванова", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-16")).sendKeys("Иванова", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-17")).sendKeys("Ивана", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-18")).sendKeys("Ивановна", Keys.TAB);
//        driver.findElement(By.cssSelector("#TextInputField-19")).sendKeys("01.04.2000");
//        driver.findElement(By.cssSelector("#TextInputField-20")).sendKeys("332211", Keys.TAB);
//        driver.findElement(By.cssSelector("div[role='presentation'] button:nth-child(3)")).click();
//
//        // Закрыть последнюю модалку
//        driver.findElement(By.xpath("//button[contains(text(),'Закрыть')]")).click();
//        // Закрыть браузер
//        driver.quit();
//    }
//}
