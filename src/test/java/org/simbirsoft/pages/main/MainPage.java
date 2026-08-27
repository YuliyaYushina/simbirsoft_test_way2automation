package org.simbirsoft.pages.main;

import lombok.Getter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

import java.util.List;

@Getter
public class MainPage extends BasePage {

    @FindBy(id = "flyerClose")
    private WebElement closeFlyer;

    @FindBy(css = ".site-header")
    private WebElement header;

    @FindBy(id = "navLinks")
    private WebElement navigation;

    @FindBy(css = ".btn-login")
    private WebElement buttonLogin;

    @FindBy(xpath = "//div[contains(@class, 'footer-col') and h4[text()='Courses']]")
    private WebElement footerCoursesBlock;

    @FindBy(css = ".site-footer")
    private WebElement footer;

    @FindBy(css = "#navLinks a[href='contact.html']")
    private WebElement contactHeader;

    @FindBy(xpath = "//div[contains(@class, 'footer-col') and h4[text()='Company']]//a[@href='contact.html']")
    private WebElement contactFooter;

    @FindBy(xpath = "//div[contains(@class, 'footer-col') and h4[text()='Courses']]//a[@href='courses.html']")
    private List<WebElement> listCourses;

    @FindBy(css = "#navLinks a[href='lifetime-membership-club/']")
    private WebElement lifetimeMembership;

    public MainPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Закрытие всплывающего окна на главной странице
     */
    public void closeFlyer() {
        try {
            waitHelper.waitForClickable(closeFlyer).click();
            waitHelper.waitForInvisibility(closeFlyer);
        } catch (TimeoutException e) {
            System.out.println("Флаер не появился");
        }
    }

    /**
     * Переход на страницу Contact из блока навагиции
     * @return страница Contact
     */
    public ContactPage goToContactHeader() {
        waitHelper.waitForVisibility(contactHeader).click();
        return new ContactPage(webDriver, webDriverWait);
    }

    /**
     * Переход на страницу Contact из блока футера
     * @return страница Contact
     */
    public ContactPage goToContactFooter() {
        waitHelper.waitForVisibility(contactFooter);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", contactFooter);
        return new ContactPage(webDriver, webDriverWait);
    }

    /**
     * Переход на страницу Lifetime Membership из блока навигации
     * @return страница Lifetime Membership
     */
    public LifetimeMembershipPage goToLifetimeMembershipPage() {
        waitHelper.waitForVisibility(lifetimeMembership).click();
        return new LifetimeMembershipPage(webDriver, webDriverWait);
    }
}
