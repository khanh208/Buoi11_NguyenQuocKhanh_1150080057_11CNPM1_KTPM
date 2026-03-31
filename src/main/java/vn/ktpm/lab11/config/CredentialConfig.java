package vn.ktpm.lab11.config;

public final class CredentialConfig {

    private CredentialConfig() {
    }

    public static String getUsername() {
        return readValue(new String[]{"APP_USERNAME", "SAUCEDEMO_USERNAME"}, "app.username");
    }

    public static String getPassword() {
        return readValue(new String[]{"APP_PASSWORD", "SAUCEDEMO_PASSWORD"}, "app.password");
    }

    public static boolean hasCredentials() {
        return !getUsername().isBlank() && !getPassword().isBlank();
    }

    private static String readValue(String[] envNames, String propertyName) {
        for (String envName : envNames) {
            String envValue = System.getenv(envName);
            if (envValue != null && !envValue.isBlank()) {
                return envValue;
            }
        }
        return ConfigReader.getProperty(propertyName, "");
    }
}
