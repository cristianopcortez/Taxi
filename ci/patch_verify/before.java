// ... lines above ...
public class AndroidDriverManager {

    public void createAndroidDriver() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAutomationName("espresso");
        options.setPlatformName("Android");

        options.setApp(DriverTestConfig.appiumAppPath());
        options.setCapability("appium:forceAppiumRebuild", true);
        options.setCapability("appium:espressoBuildConfig", ESPRESSO_BUILD_CONFIG_JSON);

        options.setNewCommandTimeout(Duration.ofSeconds(300));

        URL appiumServerUrl = new URL(APPIUM_URL);
        driver = new AndroidDriver(appiumServerUrl, options);

        driver.setSetting("driver", "compose");
    }
}
