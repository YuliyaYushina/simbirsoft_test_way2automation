package org.simbirsoft.tests.base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseMethodTest extends BaseTest {
    @BeforeMethod
    public void setUpMethod() {
        initializeDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMethod() {
        quitDriver();
    }
}
