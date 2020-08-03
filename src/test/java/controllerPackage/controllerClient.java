package controllerPackage;

import hooksPackage.defineUI;
import interfacePackage.interfaceClient;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.datatable.DataTable;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
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
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        element = (MobileElement) driver.findElementById(resourceId);
        element.click();
    }

    public void touchToElementByXpath(String resourceId, Integer item) {
        resourceId = String.format(resourceId, item);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        element = (MobileElement) driver.findElementByXPath(resourceId);
        element.click();
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

    public void touchPickUp(DataTable table) {
        for (Map<Object, Object> data : table.asMaps(String.class, String.class)) {
            try {
                if (data.get("mode").equals("google")) {
                    touchToElementById(defineUI.HOME_PICKUP_ADDRESS);
                    touchToElementById(defineUI.SWITCH_MAP);
                    sendDataToElementById(defineUI.HOME_SEARCH_ADDRESS, (String) data.get("address"));
                    selectAddressList(1);
                } else {
                    touchToElementById(defineUI.HOME_PICKUP_ADDRESS);
                    sendDataToElementById(defineUI.HOME_SEARCH_ADDRESS, (String) data.get("address"));
                    selectAddressList(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void touchDestination(DataTable table) {
        for (Map<Object, Object> data : table.asMaps(String.class, String.class)) {
            try {
                if (data.get("mode").equals("google")) {
                    touchToElementById(defineUI.REQ_SEARCH_DESTINATION);
                    touchToElementById(defineUI.SWITCH_MAP);
                    sendDataToElementById(defineUI.HOME_SEARCH_ADDRESS, (String) data.get("address"));
                    selectAddressList(1);
                } else {
                    touchToElementById(defineUI.REQ_SEARCH_DESTINATION);
                    sendDataToElementById(defineUI.HOME_SEARCH_ADDRESS, (String) data.get("address"));
                    selectAddressList(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean waitingHomeScreen() {
        return checkToElementById(defineUI.HOME_SERVICE);
    }

    public void touchBookType(String string) {
        switch (string) {
            case "Now": {
                touchToElementById(defineUI.HOME_BOOK_NOW);
                break;
            }
            default: {
                touchToElementById(defineUI.HOME_BOOK_LATE);
            }
        }
    }

    public void selectAddressList(Integer item) {
        try {
            Thread.sleep(10000);
            touchToElementByXpath(defineUI.LIST_ADDRESS, item);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Boolean requestBook() {
        try {
            // Stop and waiting response eta from payment server
            Thread.sleep(10000);
            touchToElementById(defineUI.REQ_CREATE_BOOK);
            if (checkToElementById(defineUI.REQ_SUCCESS)) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
