package org.simbirsoft.tests.base;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseClassTest extends BaseTest {
    @BeforeClass
    public void setUpClass() {
        initializeDriver();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        quitDriver();
    }
}
