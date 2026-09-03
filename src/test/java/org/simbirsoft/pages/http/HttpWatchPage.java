package org.simbirsoft.pages.http;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

@Getter
public class HttpWatchPage extends BasePage {

    @FindBy(id = "displayImage")
    private WebElement displayImageButton;

    @FindBy(id = "downloadImg")
    private WebElement downloadImg;


    public HttpWatchPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Клик по кнопке 'Display Image'
     */
    @Step("Клик по кнопке 'Display Image'")
    public void clickDisplayImageButton() {
        scrollToElement(displayImageButton);
        waitHelper.waitForClickable(displayImageButton).click();
    }

    @Step("Регистрация в драйвере учетных данных Basic Auth: логин '{username}'")
    public void registerBasicAuth(String username, String password) {
        if (webDriver instanceof HasAuthentication) {
            ((HasAuthentication) webDriver).register(UsernameAndPassword.of(username, password));
        } else {
            throw new UnsupportedOperationException("Используемый драйвер не поддерживает интерфейс HasAuthentication");
        }
    }
}
