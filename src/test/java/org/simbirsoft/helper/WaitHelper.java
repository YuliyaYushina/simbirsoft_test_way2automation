package org.simbirsoft.helper;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class WaitHelper {

    private final WebDriverWait wait;

    public WaitHelper(WebDriverWait wait) {
        this.wait = wait;
    }


    /**
     * Ожидание возможности клика по элементу
     *
     * @param element элемент страницы
     * @return элемент страницы
     */
    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Ожидание видимости эелемента на странице
     *
     * @param element элемент страницы
     * @return элемент страницы
     */
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Ожидание появления всех элементов списка
     *
     * @param webElements список элементов страницы
     * @return список элементов страницы
     */
    public List<WebElement> waitForVisibilityOfAllElements(List<WebElement> webElements) {
        return wait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    /**
     * Ожидание появления {substring} в URL
     *
     * @param substring подстрока для поиска
     * @return true - URL содержит подстроку, false - URL не содержит подстроку
     */
    public boolean waitForUrlContains(String substring) {
        return wait.until(ExpectedConditions.urlContains(substring));
    }

    /**
     * Ожидание исчезновения элемента
     *
     * @param webElement элемент страницы
     * @return true - элемент исчез со страницы, false - элемент все еще отображается на странице
     */
    public boolean waitForInvisibility(WebElement webElement) {
        return wait.until(ExpectedConditions.invisibilityOf(webElement));
    }

    /**
     * Ожидание, пока список элементов станет пустым
     * @param rows список элементов страницы
     */
    public void waitForTableToBeEmpty(List<WebElement> rows) {
        wait.until(driver -> rows.isEmpty());
    }

    /**
     * Ожидание, пока список элементов не будет пустым
     * @param rowCells список элементов строки таблицы
     */
    public void waitForRowNotToBeEmpty(List<WebElement> rowCells) {
        wait.until(driver -> !rowCells.isEmpty());
    }

    /**
     * Ожидание обновления элемента
     * @param webElement элемент страницы
     */
    public void waitForStalenessOf(WebElement webElement) {
        wait.until(ExpectedConditions.stalenessOf(webElement));
    }

    /**
     * Ожидание, пока список элементов не заполнится (будет содержать более одного элемента)
     * @param elements список элементов страницы
     */
    public void waitForListToPopulate(List<WebElement> elements) {
        wait.until(driver -> elements.size() > 1);
    }

}
