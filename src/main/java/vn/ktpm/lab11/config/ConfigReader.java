package vn.ktpm.lab11.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = loadProperties();

    private ConfigReader() {
    }

    public static String getProperty(String key, String defaultValue) {
        String value = PROPERTIES.getProperty(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        loadClasspathConfig(properties);
        loadLocalOverride(properties);
        return properties;
    }

    private static void loadClasspathConfig(Properties properties) {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ignored) {
        }
    }

    private static void loadLocalOverride(Properties properties) {
        Path localConfig = Path.of("local.properties");
        if (!Files.exists(localConfig)) {
            return;
        }

        try (InputStream input = Files.newInputStream(localConfig)) {
            Properties localProperties = new Properties();
            localProperties.load(input);
            properties.putAll(localProperties);
        } catch (IOException ignored) {
        }
    }
}

