package org.simbirsoft.pages.authorization;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

@Getter
public class AuthorizationPage extends BasePage {

    @FindBy(id = "username")
    private WebElement username;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(css = "input[id*='input_username']")
    private WebElement username2;

    @FindBy(css = "button.btn-danger")
    private WebElement loginButton;

    @FindBy(xpath = "//div[@ng-if='Auth.error']")
    private WebElement errorMessage;

    public AuthorizationPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Переход на страницу Logged in
     * @return страница Logged in
     */
    @Step("Переход на страницу Logged in")
    public LoggedInPage goToLoggedIn() {
        webDriverWait.until(ExpectedConditions.visibilityOf(loginButton)).click();
        return new LoggedInPage(webDriver, webDriverWait);
    }

    /**
     * Заполнение полей для авторизации на странице Authorization
     * @param login значение userName
     * @param password значение password
     */
    @Step("Заполнение полей для авторизации на странице")
    public void auth(String login, String password) {
        sendKeys(username, login);
        sendKeys(this.password, password);
        sendKeys(username2, login);
    }
}
