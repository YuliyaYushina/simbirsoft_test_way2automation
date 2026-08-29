package org.simbirsoft.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.helper.WaitHelper;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertTrue;

public class BaseTest {
    protected WebDriver webDriver;
    protected WebDriverWait webDriverWait;
    protected WaitHelper waitHelper;

    @BeforeMethod
    protected void initializeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();

        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        prefs.put("safebrowsing.enabled", false);
        options.setExperimentalOption("prefs", prefs);

        webDriver = new ChromeDriver(options);
        webDriverWait = new WebDriverWait(webDriver,
                Duration.ofSeconds(Long.parseLong(ParameterProvider.get("explicit.wait.time"))));
        waitHelper = new WaitHelper(webDriverWait);
    }

    @AfterMethod
    protected void quitDriver() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    /**
     * Проверка всплывающего окна
     * @param expectedText текст, который должен содержаться в алерте
     */
    public void checkAlert(String expectedText) {
        try {
            Alert alert = webDriverWait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();

            assertTrue(alertText.contains(expectedText),
                    String.format("Текст алерта '%s' не содержит '%s'!", alertText, expectedText));

            alert.accept();
        } catch (TimeoutException e) {
            Assert.fail("Алерт не появился в течение заданного времени!");
        }
    }
}
