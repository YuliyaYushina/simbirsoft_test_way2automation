package org.simbirsoft.pages.bankingApp;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

import java.util.List;

@Getter
public class SampleFormPage extends BasePage {

    @FindBy(id = "firstName")
    private WebElement firstName;

    @FindBy(id = "lastName")
    private WebElement lastName;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(css = "input[value='Sports']")
    private WebElement hobbiesSports;

    @FindBy(name = "hobbies")
    private List<WebElement> checkboxHobbies;

    @FindBy(id = "gender")
    private WebElement gender;

    @FindBy(id = "about")
    private WebElement aboutYourself;

    @FindBy(css = "button[type='submit']")
    private WebElement registerButton;

    @FindBy(id = "successMessage")
    private WebElement successMessage;

    public SampleFormPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Выбор "Sports" в пункте "Hobbies"
     * @param shouldBeChecked true - если чекбокс должен быть отмечен, false - если отметку нужно снять.
     */
    @Step("Выбор 'Sports' в пункте 'Hobbies'")
    public void selectSportsCheckbox(Boolean shouldBeChecked) {
        if (hobbiesSports.isSelected() != shouldBeChecked) {
            hobbiesSports.click();
        }
    }

    /**
     * Выбора пола "male"
     */
    @Step("Выбора пола 'male'")
    public void selectGenderMale() {
        Select select = new Select(gender);
        select.selectByValue("male");
    }

    /**
     * Написание в разделе о себе "Самое длинное слово из предложенных хобби - "
     * и программное вычисление самого длинного слова в "Hobbies"
     * @param checkboxHobbies список элементов "Hobbies"
     * @return строка заполнения раздела о себе
     */
    @Step("Заполнение раздела 'О себе'")
    public String writeAboutYourself(List<WebElement> checkboxHobbies) {
        String longestWord = "";
        for (WebElement checkboxHobby : checkboxHobbies) {
            if (checkboxHobby.getAttribute("value").length() >= longestWord.length()) {
                longestWord = checkboxHobby.getAttribute("value");
            }
        }
        return "Самое длинное слово из предложенных хобби - " + longestWord;
    }
}
