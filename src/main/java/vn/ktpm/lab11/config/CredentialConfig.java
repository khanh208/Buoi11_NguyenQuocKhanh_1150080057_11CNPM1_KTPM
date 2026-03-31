package vn.ktpm.lab11.config;

public final class CredentialConfig {

    private CredentialConfig() {
    }

    public static String getUsername() {
        return readValue("APP_USERNAME", "app.username");
    }

    public static String getPassword() {
        return readValue("APP_PASSWORD", "app.password");
    }

    public static boolean hasCredentials() {
        return !getUsername().isBlank() && !getPassword().isBlank();
    }

    private static String readValue(String envName, String propertyName) {
        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return ConfigReader.getProperty(propertyName, "");
    }
}

