package vn.ktpm.lab11.config;

public final class TestConfig {

    private static final String DEFAULT_BASE_URL = "https://www.saucedemo.com/";
    private static final String DEFAULT_BROWSER = "chrome";

    private TestConfig() {
    }

    public static String getBrowser() {
        return System.getProperty("browser", DEFAULT_BROWSER);
    }

    public static String getBaseUrl() {
        return System.getProperty("baseUrl", DEFAULT_BASE_URL);
    }

    public static String getGridUrl() {
        return System.getProperty("grid.url", "");
    }

    public static boolean isCi() {
        return System.getenv("CI") != null;
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", String.valueOf(isCi())));
    }
}

