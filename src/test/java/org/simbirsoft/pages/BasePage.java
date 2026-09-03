package org.simbirsoft.pages;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.helper.WaitHelper;

import java.util.ArrayList;
import java.util.List;

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
    @Step("Прокрутка страницы")
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(false);", element);
    }

    /**
     * Метод прокрутки в самый низ страницы
     */
    @Step("Прокрутка страницы вниз")
    public void scrollToBottom() {
        ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    /**
     * Метод проверки отображения элементов на странице
     * @param element элемент страницы
     * @return true - если элемент отображается; false - если элемент не найден
     */
    @Step("Отбражение элемента")
    public boolean checkDisplayed(WebElement element) {
        return waitHelper.waitForVisibility(element).isDisplayed();
    }

    /**
     * Заполнение поля
     * @param string значение для заполнения
     * @param element поле
     */
    @Step("Ввод текста: '{string}")
    public void sendKeys(WebElement element, String string) {
        element.clear();
        element.sendKeys(string);
    }

    /**
     * Убирание фокуса (blur) с элемента страницы
     * @param element элемент страницы
     */
    @Step("Убрать фокус с элемента")
    public void removeFocus(WebElement element) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].blur();", element);
    }

    /**
     * Определение наличия вертикального скролла на странице
     * @return true - если скролл присутствует; false - если страница помещается на одном экране
     */
    @Step("Определение наличия вертикального скролла на странице")
    public boolean isVerticalScrollPresent() {
        return (Boolean) ((JavascriptExecutor) webDriver).executeScript(
                "return document.documentElement.scrollHeight > document.documentElement.clientHeight;"
        );
    }

    /**
     * Проверка, находится ли конкретный элемент в фокусе на данный момент
     * @param element элемент для проверки
     * @return true - если фокус на элементе; false - если элемент не активен
     */
    @Step("Проверка, находится ли элемент в фокусе")
    public boolean isElementFocused(WebElement element) {
        return (Boolean) ((JavascriptExecutor) webDriver).executeScript(
                "return document.activeElement === arguments[0];", element
        );
    }

    /**
     * Переключение на вкладку по индексу
     * @param index индекс вкладки (начиная с 0)
     */
    @Step("Переключение на {index} вкладку")
    public void goToWindow(int index) {
        List<String> windowHandles = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(windowHandles.get(index));
    }
}
