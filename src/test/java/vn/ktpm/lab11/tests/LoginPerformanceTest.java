package vn.ktpm.lab11.tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import vn.ktpm.lab11.base.BaseTest;
import vn.ktpm.lab11.config.CredentialConfig;
import vn.ktpm.lab11.pages.InventoryPage;
import vn.ktpm.lab11.pages.LoginPage;

public class LoginPerformanceTest extends BaseTest {

    @Test(description = "4C-01: mo trang dang nhap")
    public void shouldOpenLoginPage() {
        LoginPage loginPage = openLoginPage();

        Assert.assertTrue(loginPage.isLoaded(), "Trang dang nhap phai hien thi logo");
    }

    @Test(description = "4C-02: hien thi day du control dang nhap")
    public void shouldDisplayLoginControls() {
        LoginPage loginPage = openLoginPage();

        Assert.assertTrue(loginPage.isUsernameInputVisible(), "O username phai hien thi");
        Assert.assertTrue(loginPage.isPasswordInputVisible(), "O password phai hien thi");
        Assert.assertTrue(loginPage.isLoginButtonVisible(), "Nut Login phai hien thi");
    }

    @Test(description = "4C-03: bao loi khi de trong ca username va password")
    public void shouldRejectEmptyCredentials() {
        LoginPage loginPage = openLoginPage();
        loginPage.clickLogin();

        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
    }

    @Test(description = "4C-04: bao loi khi co username nhung thieu password")
    public void shouldRequirePasswordWhenUsernameProvided() {
        LoginPage loginPage = openLoginPage();
        loginPage.enterUsername("placeholder_user");
        loginPage.clickLogin();

        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Password is required");
    }

    @Test(description = "4C-05: bao loi khi co password nhung thieu username")
    public void shouldRequireUsernameWhenPasswordProvided() {
        LoginPage loginPage = openLoginPage();
        loginPage.enterPassword("placeholder_password");
        loginPage.clickLogin();

        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
    }

    @Test(description = "4C-06: bao loi khi password sai")
    public void shouldRejectInvalidPassword() {
        requireCredentials();

        LoginPage loginPage = openLoginPage();
        loginPage.enterUsername(CredentialConfig.getUsername());
        loginPage.enterPassword("invalid_password");
        loginPage.clickLogin();

        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service");
    }

    @Test(description = "4C-07: dang nhap hop le chuyen sang inventory")
    public void shouldNavigateToInventoryAfterValidLogin() {
        requireCredentials();

        LoginPage loginPage = openLoginPage();
        loginPage.loginAs(CredentialConfig.getUsername(), CredentialConfig.getPassword());

        Assert.assertTrue(driver().getCurrentUrl().contains("inventory"),
                "Dang nhap hop le phai chuyen sang inventory");
    }

    @Test(description = "4C-08: inventory hien thi title va gio hang sau khi dang nhap")
    public void shouldDisplayInventoryHeaderAfterValidLogin() {
        requireCredentials();

        LoginPage loginPage = openLoginPage();
        loginPage.loginAs(CredentialConfig.getUsername(), CredentialConfig.getPassword());

        InventoryPage inventoryPage = new InventoryPage(driver());
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory phai hien thi tieu de Products");
        Assert.assertTrue(inventoryPage.isCartIconVisible(), "Icon gio hang phai hien thi sau khi dang nhap");
    }

    private LoginPage openLoginPage() {
        LoginPage loginPage = new LoginPage(driver());
        loginPage.open(baseUrl());
        return loginPage;
    }

    private void requireCredentials() {
        if (!CredentialConfig.hasCredentials()) {
            throw new SkipException("Chua co credential. Hay dat APP_USERNAME va APP_PASSWORD de chay bai 4C.");
        }
    }
}
