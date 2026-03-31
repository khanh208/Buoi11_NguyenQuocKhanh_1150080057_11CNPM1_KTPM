package vn.ktpm.lab11.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InventoryPage {

    private final WebDriverWait wait;

    private final By pageTitle = By.className("title");
    private final By shoppingCartLink = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isLoaded() {
        return "Products".equalsIgnoreCase(wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText());
    }

    public boolean isCartIconVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(shoppingCartLink)).isDisplayed();
    }
}
