package vn.ktpm.lab11.tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import vn.ktpm.lab11.base.BaseTest;
import vn.ktpm.lab11.config.CredentialConfig;
import vn.ktpm.lab11.pages.LoginPage;

public class LoginWithSecretTest extends BaseTest {

    @Test(description = "Lab 3: dang nhap bang credential tu GitHub Secrets hoac local config")
    public void shouldLoginWithCredentialFromEnvironmentOrConfig() {
        if (!CredentialConfig.hasCredentials()) {
            throw new SkipException("Chua co credential. Hay dat APP_USERNAME, APP_PASSWORD hoac tao local.properties.");
        }

        LoginPage loginPage = new LoginPage(driver());
        loginPage.open(baseUrl());
        loginPage.loginAs(CredentialConfig.getUsername(), CredentialConfig.getPassword());

        Assert.assertTrue(driver().getCurrentUrl().contains("inventory"),
                "Dang nhap thanh cong phai chuyen sang trang inventory");
    }
}

