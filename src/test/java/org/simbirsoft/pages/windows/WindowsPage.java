package org.simbirsoft.pages.windows;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

public class WindowsPage extends BasePage {

    @FindBy(css = "iframe.demo-frame")
    private WebElement demoFrame;

    @FindBy(linkText = "New Browser Tab")
    private WebElement newBrowserTabLink;

    @FindBy(linkText = "New Browser Tab")
    private WebElement clickLink;


    public WindowsPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Переключение фокуса в iframe
     */
    @Step("Переключение фокуса в iframe")
    public void switchToDemoFrame() {
        waitHelper.waitForVisibility(demoFrame);
        webDriver.switchTo().frame(demoFrame);
    }

    /**
     * Клик по ссылке 'New Browser Tab' на первой вкладке
     */
    @Step("Клик по ссылке 'New Browser Tab' на первой вкладке")
    public void clickNewBrowserTab() {
        waitHelper.waitForClickable(newBrowserTabLink).click();
    }

    /**
     * Клик по ссылке 'Click' на второй вкладке
     */
    @Step("Клик по ссылке 'Click' на следующей вкладке")
    public void clickClickLinkOnTab() {
        waitHelper.waitForClickable(clickLink).click();
    }
}
