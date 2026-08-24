package org.simbirsoft.pages.authorization;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

@Getter
public class LoggedInPage extends BasePage {

    @FindBy(xpath = "//p[text()=\"You're logged in!!\"]")
    private WebElement loggedIn;

    @FindBy(css = "a[href='#/login']")
    private WebElement logout;

    public LoggedInPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Переход на страницу Authorization (разлогинивание)
     * @return страница Authorization
     */
    public AuthorizationPage selectLogout() {
        webDriverWait.until(ExpectedConditions.visibilityOf(logout)).click();
        return new AuthorizationPage(webDriver, webDriverWait);
    }
}
