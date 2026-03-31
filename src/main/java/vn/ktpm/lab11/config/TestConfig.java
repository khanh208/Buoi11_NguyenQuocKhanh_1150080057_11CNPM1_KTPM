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
        return readValue("BASE_URL", "base.url", DEFAULT_BASE_URL);
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

    private static String readValue(String envName, String propertyName, String defaultValue) {
        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return ConfigReader.getProperty(propertyName, defaultValue);
    }
}
