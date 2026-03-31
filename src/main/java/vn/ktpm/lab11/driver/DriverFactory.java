package vn.ktpm.lab11.driver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import vn.ktpm.lab11.config.TestConfig;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        String requestedBrowser = browser == null || browser.isBlank() ? TestConfig.getBrowser() : browser;
        String gridUrl = TestConfig.getGridUrl();

        if (!gridUrl.isBlank()) {
            return createRemoteDriver(requestedBrowser, gridUrl, TestConfig.isHeadless());
        }

        return createLocalDriver(requestedBrowser, TestConfig.isHeadless());
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        return switch (browser.toLowerCase()) {
            case "firefox" -> new FirefoxDriver(buildFirefoxOptions(headless));
            case "chrome" -> new ChromeDriver(buildChromeOptions(headless));
            default -> throw new IllegalArgumentException("Browser khong duoc ho tro: " + browser);
        };
    }

    private static WebDriver createRemoteDriver(String browser, String gridUrl, boolean headless) {
        MutableCapabilities capabilities = switch (browser.toLowerCase()) {
            case "firefox" -> buildFirefoxOptions(headless);
            case "chrome" -> buildChromeOptions(headless);
            default -> throw new IllegalArgumentException("Browser khong duoc ho tro tren Grid: " + browser);
        };

        try {
            RemoteWebDriver driver = new RemoteWebDriver(URI.create(gridUrl).toURL(), capabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Grid URL khong hop le: " + gridUrl, e);
        }
    }

    private static ChromeOptions buildChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-search-engine-choice-screen");

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        } else {
            options.addArguments("--start-maximized");
        }

        return options;
    }

    private static FirefoxOptions buildFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");

        if (headless) {
            options.addArguments("-headless");
        }

        return options;
    }
}

