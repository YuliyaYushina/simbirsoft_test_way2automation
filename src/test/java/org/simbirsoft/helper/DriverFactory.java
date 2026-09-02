package org.simbirsoft.helper;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    /**
     * Создание WebDriver на основе переданных параметров
     * @param browser имя браузера (chrome, firefox, edge, ie)
     * @param runOnGrid флаг запуска на Selenium Grid (true/false)
     * @param gridUrl URL хаба Selenium Grid
     * @return WebDriver
     */
    public static WebDriver createInstance(String browser, boolean runOnGrid, String gridUrl) {
        WebDriver driver;
        URL url = null;

        if (runOnGrid) {
            try {
                url = new URL(gridUrl);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Некорректный URL хаба Selenium Grid: " + gridUrl, e);
            }
        }

        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();
                if (runOnGrid) {
                    driver = new RemoteWebDriver(url, ffOptions);
                } else {
                    driver = new FirefoxDriver(ffOptions);
                }
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (runOnGrid) {
                    driver = new RemoteWebDriver(url, edgeOptions);
                } else {
                    driver = new EdgeDriver(edgeOptions);
                }
                break;

            case "ie":
                WebDriverManager.iedriver().setup();
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                // Обход Protected Mode без изменения реестра Windows
                ieOptions.introduceFlakinessByIgnoringSecurityDomains();
                ieOptions.ignoreZoomSettings();
                if (runOnGrid) {
                    driver = new RemoteWebDriver(url, ieOptions);
                } else {
                    driver = new InternetExplorerDriver(ieOptions);
                }
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                prefs.put("safebrowsing.enabled", false);
                chromeOptions.setExperimentalOption("prefs", prefs);

                if (runOnGrid) {
                    driver = new RemoteWebDriver(url, chromeOptions);
                } else {
                    driver = new ChromeDriver(chromeOptions);
                }
                break;
        }
        return driver;
    }
}
