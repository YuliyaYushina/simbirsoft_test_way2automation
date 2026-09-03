package org.simbirsoft.pages.droppable;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

public class DroppablePage extends BasePage {

    @FindBy(css = "iframe.demo-frame")
    private WebElement demoFrame;

    @FindBy(id = "draggable")
    private WebElement draggableBox;

    @FindBy(id = "droppable")
    private WebElement droppableBox;

    public DroppablePage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Переключение фокуса драйвера внутрь iframe
     */
    @Step("Переключение фокуса внутрь iframe с интерактивными элементами")
    public void switchToDemoFrame() {
        waitHelper.waitForVisibility(demoFrame);
        webDriver.switchTo().frame(demoFrame);
    }

    /**
     * Выполнение операции Drag and Drop
     */
    @Step("Перетаскивание элемента в принимающую область (Drag and Drop)")
    public void dragAndDropBox() {
        waitHelper.waitForVisibility(draggableBox);
        waitHelper.waitForVisibility(droppableBox);

        Actions actions = new Actions(webDriver);
        actions.dragAndDrop(draggableBox, droppableBox).perform();
    }

    /**
     * Получение текущего текста из элемента-приемника
     */
    @Step("Получение текста из принимающего элемента")
    public String getDroppableBoxText() {
        return waitHelper.waitForVisibility(droppableBox).getText();
    }
}
