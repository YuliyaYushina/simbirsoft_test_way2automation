package org.simbirsoft.tests;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.helper.DriverFactory;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.helper.WaitHelper;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class BaseTest {
    protected static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    protected WebDriverWait webDriverWait;
    protected WaitHelper waitHelper;

    public WebDriver getDriver() {
        return driverThread.get();
    }

    @BeforeMethod
    protected void initializeDriver() {
        String browser = ParameterProvider.get("browser");
        if (browser == null) {
            browser = "chrome";
        }

        boolean runOnGrid = Boolean.parseBoolean(ParameterProvider.get("run.grid"));
        String gridUrl = "http://localhost:4444"; // можно вынести в ParameterProvider

        WebDriver driver = DriverFactory.createInstance(browser, runOnGrid, gridUrl);

        driverThread.set(driver);

        webDriverWait = new WebDriverWait(getDriver(),
                Duration.ofSeconds(Long.parseLong(ParameterProvider.get("explicit.wait.time"))));
        waitHelper = new WaitHelper(webDriverWait);
    }

    @AfterMethod
    protected void quitDriver(ITestResult result) {
        WebDriver webDriver = getDriver();
        if (webDriver != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                saveScreenshot();
            }
            webDriver.quit();
        }
    }

    @Attachment(value = "Скриншот при падении теста", type = "image/png")
    public byte[] saveScreenshot() {
        Screenshot screenshot = new AShot().takeScreenshot(getDriver());
        BufferedImage image = screenshot.getImage();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0]; // возвращаем пустой массив в случае ошибки
        }
    }

    /**
     * Проверка всплывающего окна
     * @param expectedText текст, который должен содержаться в алерте
     */
    @Step("Проверка алерта")
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
