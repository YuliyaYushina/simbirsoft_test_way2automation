package org.simbirsoft.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ParameterProvider {
    private static final String PARAMETERS_PATH = "configurations/config.properties";

    private static ParameterProvider instance;
    private final Map<String, String> parameters;

    private ParameterProvider() {
        try {
            parameters = new HashMap<String, String>();
            final Properties properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PARAMETERS_PATH);
            properties.load(inputStream);
            properties.stringPropertyNames()
                    .forEach(key -> parameters.put(key, properties.getProperty(key)));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static String get(String key) {
        if (instance == null) {
            instance = new ParameterProvider();
        }
        return instance.parameters.get(key);
    }
}
