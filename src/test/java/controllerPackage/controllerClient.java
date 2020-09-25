package controllerPackage;

import hooksPackage.defineUI;
import interfacePackage.interfaceClient;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.datatable.DataTable;
import io.cucumber.messages.internal.com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.openqa.selenium.By;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Time;
import java.util.List;
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
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
            if (checkToElementById(defineUI.HOME_SEARCH_ADDRESS)) {
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

    public void selectPickUpTime(String hours, String mins) {
        int hour = Integer.parseInt(hours);
        int min = Integer.parseInt(mins);
        while (hour > 0) {
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//android.widget.LinearLayout/android.widget.TimePicker/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.NumberPicker[1]/android.widget.Button[2]")).click();
            hour--;
        }
        while (min > 0) {
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//android.widget.LinearLayout/android.widget.TimePicker/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.NumberPicker[2]/android.widget.Button[2]")).click();
            min--;
        }
        driver.findElementById(String.format(defineUI.BUTTON_YES, appPackageId)).click();
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

    public void selectPaymentMethod(String string) {
        touchToElementById(defineUI.REQ_PAYMENT_METHOD);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        List<MobileElement> list = (List<MobileElement>) driver.findElementsById(String.format(defineUI.REQ_CARDHOLDER, appPackageId));
        int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            if (list.get(i).getText().equals(string)) {
                list.get(i).click();
            }
        }
        touchToElementById(defineUI.BUTTON_YES);
    }

    public void selectTypeRide(String string) {
        switch (string) {
            case "Personal":
                touchToElementById(defineUI.HOME_RIDE_PERSONAL);
                break;
            default:
                touchToElementById(defineUI.HOME_RIDE_BUSINESS);
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
        LOGGER.info("Actual response data: {} ", actualData);
        LOGGER.info("Excepted data: {}", string);
        if (string.equals(actualData)) {
            return true;
        } else {
            return false;
        }
    }
}
