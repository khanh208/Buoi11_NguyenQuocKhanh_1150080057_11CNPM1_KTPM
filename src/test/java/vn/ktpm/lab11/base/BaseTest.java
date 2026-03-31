package vn.ktpm.lab11.base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import vn.ktpm.lab11.config.TestConfig;
import vn.ktpm.lab11.driver.DriverFactory;

public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) {
        String resolvedBrowser = System.getProperty("browser", browser);
        WebDriver driver = DriverFactory.createDriver(resolvedBrowser);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        DRIVER.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = DRIVER.get();

        if (driver != null && !result.isSuccess()) {
            saveScreenshot(result.getMethod().getMethodName(), driver);
        }

        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    protected WebDriver driver() {
        return DRIVER.get();
    }

    protected String baseUrl() {
        return TestConfig.getBaseUrl();
    }

    private void saveScreenshot(String testName, WebDriver driver) {
        try {
            Files.createDirectories(Path.of("target", "screenshots"));
            Path destination = Path.of("target", "screenshots", testName + ".png");
            Path source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignored) {
        }
    }
}

