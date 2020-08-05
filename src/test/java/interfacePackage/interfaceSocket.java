package interfacePackage;

import io.appium.java_client.android.AndroidDriver;

import java.net.URISyntaxException;
import java.util.List;

public interface interfaceSocket {
    public void gListBooking();
    public void connectSocket(String string, List<String> table) throws URISyntaxException;
}
