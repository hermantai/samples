package com.johannesbrodwall;

import com.johannesbrodwall.infrastructure.AppConfiguration;

public class MyAppConfig extends AppConfiguration {

    private MyAppConfig() {
        this("myapp.properties");
    }

    public MyAppConfig(String propertyFile) {
        super(propertyFile, "myapp");
    }

    private static MyAppConfig instance = new MyAppConfig();

    public String getServiceHost() {
        return getRequiredProperty("service.host");
    }

    public boolean getShouldStartSlow() {
        return getFlag("start-slow", false);
    }

    public static MyAppConfig instance() {
        return instance;
    }
}
