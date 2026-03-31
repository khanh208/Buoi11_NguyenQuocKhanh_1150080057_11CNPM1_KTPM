package vn.ktpm.lab11.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import vn.ktpm.lab11.base.BaseTest;
import vn.ktpm.lab11.pages.LoginPage;

public class SmokeLoginPageTest extends BaseTest {

    @Test(description = "Lab 1 smoke test: mo trang dang nhap SauceDemo")
    public void shouldOpenSauceDemoLoginPage() {
        LoginPage loginPage = new LoginPage(driver());

        loginPage.open(baseUrl());

        Assert.assertTrue(loginPage.isLoaded(), "Logo dang nhap phai hien thi");
        Assert.assertTrue(loginPage.isLoginButtonVisible(), "Nut Login phai hien thi");
    }

    @Test(description = "Lab 1 fail demo: assertion sai co y de chup log do tren GitHub Actions", enabled = false)
    public void shouldFailIntentionallyForLab1Evidence() {
        LoginPage loginPage = new LoginPage(driver());

        loginPage.open(baseUrl());

        Assert.assertTrue(loginPage.isLoaded(), "Logo dang nhap phai hien thi");
        Assert.assertTrue(false, "Fail co y de minh hoa pipeline mau do cho Bai 1");
    }
}
