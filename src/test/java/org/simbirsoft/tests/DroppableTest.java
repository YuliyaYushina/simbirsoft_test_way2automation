package org.simbirsoft.tests;

import io.qameta.allure.*;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.droppable.DroppablePage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Epic("Интерактивные элементы на странице")
@Feature("Проверка Drag and Drop")
public class DroppableTest extends BaseTest {

    @BeforeMethod
    public void openUrl() {
        getDriver().get(ParameterProvider.get("droppable.url"));
    }

    @Test(description = "Проверка перетаскивания элемента")
    @Story("Перетаскивание элементов(Drag and Drop)")
    @Severity(SeverityLevel.NORMAL)
    void checkDragAndDropTest() {
        DroppablePage droppablePage = new DroppablePage(getDriver(), webDriverWait);

        droppablePage.switchToDemoFrame();

        String expectedTextBeforeDrop = "Drop here";
        String actualTextBeforeDrop = droppablePage.getDroppableBoxText();
        assertEquals(actualTextBeforeDrop, expectedTextBeforeDrop, "Начальный текст приемника не совпадает!");

        droppablePage.dragAndDropBox();

        String textAfterDrop = droppablePage.getDroppableBoxText();
        assertEquals(textAfterDrop, "Dropped!", "Текст приемника не изменился после Drag and Drop!");
    }
}
