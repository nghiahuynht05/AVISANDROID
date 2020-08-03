package hooksPackage;

import controllerPackage.controllerAPI;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.Before;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

public class hooks {
    private static AndroidDriver driver;
    public static Logger LOGGER = LogManager.getLogger(hooks.class);
    Socket socket;
    controllerAPI client;

    Socket client() throws URISyntaxException {
        return client(createOptions());
    }

    Socket client(String path) throws URISyntaxException {
        return client(path, createOptions());
    }

    Socket client(IO.Options opts) throws URISyntaxException {
        return client(nsp(), opts);
    }

    Socket client(String path, IO.Options opts) throws URISyntaxException {
        return IO.socket(client.getConfig("socketServer") + path, opts);
    }

    String nsp() {
        return "/";
    }

    IO.Options createOptions() {
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        return opts;
    }

    @Before
    public static AndroidDriver setUp() {
        System.setProperty("log4j.configurationFile", "./log4j2.properties");

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

    public Socket clear() {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();

        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = false;
        try {
            socket = IO.socket(client.getConfig("socketServer"), opts);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    JSONObject params = new JSONObject();
                    params.put("token", client.tokenAuth);
                    params.put("fleetId", client.getConfig("fleetId"));
                    socket.emit("ccLiteLogin", params);
                }
            }).on("ccLiteLogin", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    JSONObject params = new JSONObject();
                    params.put("bookId", client.bookId);
                    socket.emit("cancelBookingCC", params);
                }
            }).on("cancelBookingCC", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    JSONObject object = (JSONObject) objects[0];
                    if (object.get("error").equals(400)) {
                        JSONObject incidentParams = new JSONObject();
                        JSONObject operatorParams = new JSONObject();
                        operatorParams.put("name", "Auto Test");
                        operatorParams.put("userId", "5cd2a36a160edc148d066d4a");
                        incidentParams.put("bookId", client.bookId);
                        incidentParams.put("reason", client.bookId);
                        incidentParams.put("operator", operatorParams);
                        socket.emit("incident", incidentParams);
                    }
                    values.offer(objects);
                }
            }).on("incident", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    values.offer(objects);
                }
            });
            try {
                socket.connect();
                Object[] args = new Object[0];
                args = (Object[]) values.take();
                assertNotNull(args);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
