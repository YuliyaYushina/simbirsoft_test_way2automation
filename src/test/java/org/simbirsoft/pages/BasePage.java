package org.simbirsoft.pages;

import lombok.Getter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.helper.WaitHelper;

@Getter
public class BasePage {
    protected WebDriver webDriver;
    protected WebDriverWait webDriverWait;
    protected WaitHelper waitHelper;

    public BasePage(WebDriver webDriver, WebDriverWait webDriverWait) {
        this.webDriver = webDriver;
        this.webDriverWait = webDriverWait;
        this.waitHelper = new WaitHelper(webDriverWait);
    }

    /**
     * Метод прокрутки страницы до конкретного элемента
     * @param element элемент страницы
     */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(false);", element);
    }

    /**
     * Метод прокрутки в самый низ страницы
     */
    public void scrollToBottom() {
        ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    /**
     * Метод проверки отображения элементов на странице
     * @param element элемент страницы
     * @return true - если элемент отображается; false - если элемент не найден
     */
    public boolean checkDisplayed(WebElement element) {
        return waitHelper.waitForVisibility(element).isDisplayed();
    }

    /**
     * Заполнение поля
     * @param string значение для заполнения
     * @param element поле
     */
    public void sendKeys(String string, WebElement element) {
        element.clear();
        element.sendKeys(string);
    }

}
