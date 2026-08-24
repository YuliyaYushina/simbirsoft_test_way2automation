package org.simbirsoft.pages.main;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

@Getter
public class LifetimeMembershipPage extends BasePage {

    @FindBy(xpath = "//h1[normalize-space()='The Lifetime Membership Club']")
    private WebElement headerPage;

    public LifetimeMembershipPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }


}
