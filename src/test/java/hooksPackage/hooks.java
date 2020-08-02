package hooksPackage;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class hooks {
    private static AndroidDriver driver;
    public static Logger LOGGER = LogManager.getLogger(hooks.class);

    @Before
    public static AndroidDriver setUp() {
        System.setProperty("log4j.configurationFile","./log4j2.properties");

        InputStream inputStream;
        try {
            Properties config = new Properties();
            String configFile = "config.properties";
            inputStream = hooks.class.getClassLoader().getResourceAsStream(configFile);
            if (inputStream != null) {
                config.load(inputStream);
            } else {
                throw new FileNotFoundException("config.properties '" + configFile + "' not found in the classpath");
            }
            String platformVersion = config.getProperty("platformVersion");
            String appPackage = config.getProperty("appPackage");
            String appActivity = config.getProperty("appActivity");
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("deviceName", "Android Emulator");
            cap.setCapability("platformName", "Android");
            cap.setCapability("platformVersion", platformVersion);
            cap.setCapability("appPackage", appPackage);
            cap.setCapability("appActivity", appActivity);
            cap.setCapability("autoGrantPermissions", "true");
            cap.setCapability("skipDeviceInitialization", "false");
            cap.setCapability("skipServerInstallation", "false");
            cap.setCapability("noReset", "true");
            try {
                LOGGER.info("Android Driver: {} ", cap);
                driver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), cap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }

    @After
    public void clear() {

    }
}
