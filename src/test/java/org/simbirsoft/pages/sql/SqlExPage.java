package org.simbirsoft.pages.sql;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.helper.CookieHelper;
import org.simbirsoft.pages.BasePage;

public class SqlExPage extends BasePage {

    @FindBy(name = "login")
    private WebElement inputLogin;

    @FindBy(name = "psw")
    private WebElement inputPassword;

    @FindBy(name = "subm1")
    private WebElement submitButton;

    @FindBy(xpath = "//a[@href='/logout.php']")
    private WebElement logoutLink;

    public SqlExPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Авторизация на странице SqlEx
     * @param login логин
     * @param password пароль
     */
    @Step("Авторизация на странице")
    public void auth(String login, String password) {
        sendKeys(inputLogin, login);
        sendKeys(inputPassword, password);
        submitButton.click();
    }

    /**
     * Проверка, авторизован ли пользователь на данный момент
     */
    @Step("Проверка статуса авторизации")
    public boolean isUserLoggedIn() {
        waitHelper.waitForVisibility(logoutLink);
        return this.checkDisplayed(logoutLink);
    }

    /**
     * Авторизация по логину и паролю только в случае, если сессия по кукам не восстановилась
     * @param login логин
     * @param password пароль
     * @param cookiesPath путь к файлу с сохраненными куками
     */
    @Step("Авторизация на сайте при отсутствии сохраненых куков")
    public void loginIfNeeded(String login, String password, String cookiesPath) {
        if (!isUserLoggedIn()) {
            auth(login, password);
            if (isUserLoggedIn()) {
                CookieHelper.saveCookies(webDriver, cookiesPath);
            }
        }
    }
}
