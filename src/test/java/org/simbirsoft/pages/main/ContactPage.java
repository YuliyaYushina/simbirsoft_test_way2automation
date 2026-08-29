package org.simbirsoft.pages.main;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

@Getter
public class ContactPage extends BasePage {

    @FindBy(xpath = "//div[@class='contact-info-card']//span[contains(text(), '@')]")
    private WebElement email;

    @FindBy(xpath = "//div[@class='contact-info-card']//span[contains(text(), '+')]")
    private WebElement phone;

    @FindBy(xpath = "//div[@class='contact-info-card']//span[contains(text(), 'www.')]")
    private WebElement website;

    public ContactPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Получение email на странице Contact
     * @return строка email
     */
    public String getActualEmail() {
        return email.getText();
    }

    /**
     * Получение телефона на странице Contact
     * @return строка телефона
     */
    public String getActualPhone() {
        return phone.getText();
    }

    /**
     * Получение вебсайта на странице Contact
     * @return строка вебсайта
     */
    public String getActualWebsite() {
        return website.getText();
    }
}
