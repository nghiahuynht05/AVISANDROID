package controllerPackage;

import hooksPackage.defineUI;
import interfacePackage.interfaceClient;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class controllerClient implements interfaceClient {
    AndroidDriver driver;
    MobileElement element;
    InputStream inputStream;
    defineUI defineUI;

    public static String appPackageId;
    String configFile = "config.properties";

    public controllerClient(AndroidDriver driver) {
        this.driver = driver;
        try {
            Properties config = new Properties();
            inputStream = controllerClient.class.getClassLoader().getResourceAsStream(configFile);
            if (inputStream != null) {
                config.load(inputStream);
            } else {
                throw new FileNotFoundException("config.properties '" + configFile + "' not found in the classpath");
            }
            String appPackage = config.getProperty("appPackageId");
            appPackageId = appPackage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void touchToElementById(String resourceId) {
        resourceId = String.format(resourceId, appPackageId);
        element = (MobileElement) driver.findElementById(resourceId);
        element.click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public void touchToElementByXpath(String resourceId, String item) {
        resourceId = resourceId + item;
        element = (MobileElement) driver.findElementByXPath(resourceId);
        element.click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public void sendDataToElementById(String resourceId, String string) {
        resourceId = String.format(resourceId, appPackageId);
        element = (MobileElement) driver.findElementById(resourceId);
        element.sendKeys(string);
    }


    public boolean checkToElementById(String resourceId) {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();

        try {
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
            resourceId = String.format(resourceId, appPackageId);
            element = (MobileElement) driver.findElementById(resourceId);
            values.offer(element);
            values.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (element.isDisplayed()) {
            return true;
        } else {
            return false;
        }
    }

    public void touchPickUp(String string) {
        touchToElementById(defineUI.HOME_PICKUP_ADDRESS);
        sendDataToElementById(defineUI.HOME_SEARCH_ADDRESS, string);
        driver.hideKeyboard();
    }

    public void touchDestination(String string) {
        touchToElementById(defineUI.REQ_SEARCH_DESTINATION);
        sendDataToElementById(defineUI.HOME_SEARCH_ADDRESS, string);
        driver.hideKeyboard();
    }

    public Boolean waitingHomeScreen() {
        return checkToElementById(defineUI.HOME_SERVICE);
    }

    public void touchBookType(String string) {
        switch (string) {
            case "Now": {
                touchToElementById(defineUI.HOME_BOOK_NOW);
            }
            default: {
                touchToElementById(defineUI.HOME_BOOK_LATE);
            }
        }
    }
}
