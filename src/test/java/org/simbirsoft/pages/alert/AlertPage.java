package org.simbirsoft.pages.alert;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

public class AlertPage extends BasePage {

    @FindBy(xpath = "//a[@href='#example-1-tab-2']")
    private WebElement inputAlertButton;

    @FindBy(css = "#example-1-tab-2 iframe.demo-frame")
    private WebElement demoFrame;

    @FindBy(css = "button[onclick='myFunction()']")
    private WebElement inputBoxButton;

    @FindBy(id = "demo")
    private WebElement textDemo;

    public AlertPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Переход в раздел 'Input Alert'
     */
    @Step("Переход в раздел 'Input Alert'")
    public void clickInputAlert() {
        waitHelper.waitForClickable(inputAlertButton).click();
    }

    /**
     * Переключение фокуса в iframe вкладки 'Input Alert'
     */
    @Step("Переключение фокуса в iframe вкладки 'Input Alert'")
    public void switchToDemoFrame() {
        waitHelper.waitForVisibility(demoFrame);
        webDriver.switchTo().frame(demoFrame);
    }

    /**
     * Вызов алерта
     */
    @Step("Вызов алерта")
    public void clickInputBox() {
        waitHelper.waitForClickable(inputBoxButton).click();
    }

    /**
     * Ввод текста в алерте
     * @param text строка для ввода
     */
    @Step("Ввод текста в алерте")
    public void inputTextInAlert(String text) {
        Alert alert = webDriverWait.until(ExpectedConditions.alertIsPresent());
        alert.sendKeys(text);
        alert.accept();
    }

    /**
     * Получение отображаемого текста на странице
     * @return текст на странице
     */
    @Step("Получение отображаемого текста на странице")
    public String getTextDemo() {
        return waitHelper.waitForVisibility(textDemo).getText();
    }
}
