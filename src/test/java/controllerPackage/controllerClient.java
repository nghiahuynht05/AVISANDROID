package controllerPackage;

import hooksPackage.defineUI;
import interfacePackage.interfaceClient;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.datatable.DataTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class controllerClient implements interfaceClient {
    public static Logger LOGGER = LogManager.getLogger(controllerClient.class);

    AndroidDriver driver;
    MobileElement element;
    InputStream inputStream;
    defineUI defineUI;
    String actualData;
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

    public String getTextElementById(String resourceId) {
        resourceId = String.format(resourceId, appPackageId);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        element = (MobileElement) driver.findElementById(resourceId);
        return element.getText();
    }

    public boolean checkToElementById(String resourceId) {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();

        try {
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
            resourceId = String.format(resourceId, appPackageId);
            element = (MobileElement) driver.findElementById(resourceId);
            values.offer(element);
            element = (MobileElement) values.take();
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
            touchToElementById(defineUI.HOME_PICKUP_ADDRESS);
            if(checkToElementById(defineUI.HOME_SEARCH_ADDRESS)) {
                try {
                    if (data.get("mode").equals("google")) {
                        touchToElementById(defineUI.SWITCH_MAP);
                    }
                    sendDataToElementById(defineUI.HOME_SEARCH_ADDRESS, (String) data.get("address"));
                    selectAddressList(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public void selectServiceType(String string) {
        touchToElementById(defineUI.REQ_BOOK_TYPE);
        switch (string) {
            case "Regular":
                touchToElementByXpath(defineUI.REQ_BOOK_TYPE_SERVICE, 1);
                break;
            default:
                touchToElementByXpath(defineUI.REQ_BOOK_TYPE_SERVICE, 2);
        }
    }

    public void selectCarType(String string) {
        switch (string) {
            case "Premium":
                touchToElementByXpath(defineUI.HOME_CAR_TYPE, 2);
                break;
            case "Business":
                touchToElementByXpath(defineUI.HOME_CAR_TYPE, 3);
                break;
            default:
                touchToElementByXpath(defineUI.HOME_CAR_TYPE, 1);
        }
    }

    public Boolean requestBook() {
        try {
            // Stop and waiting response eta from payment server
            Thread.sleep(10000);
            touchToElementById(defineUI.REQ_CREATE_BOOK);
            if (checkToElementById(defineUI.REQ_POPUP)) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getContentMsg() {
        actualData = getTextElementById(defineUI.MESSAGE);
    }

    public Boolean matchResponseMsg(String string) {
        LOGGER.info( "Actual response data: {} ", actualData );
        LOGGER.info( "Excepted data: {}", string );
        if(string.equals(actualData)) {
            return true;
        }
        else {
            return false;
        }
    }
}
