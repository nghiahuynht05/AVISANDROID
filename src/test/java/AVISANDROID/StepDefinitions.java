package AVISANDROID;

import controllerPackage.controllerAPI;
import controllerPackage.controllerClient;
import controllerPackage.controllerSocket;
import hooksPackage.hooks;
import interfacePackage.interfaceAPI;
import interfacePackage.interfaceClient;
import interfacePackage.interfaceSocket;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.socket.client.Socket;

import static org.junit.Assert.assertTrue;

public class StepDefinitions {
    AndroidDriver driver;
    Socket Socket;
    hooks hooks;
    interfaceClient client;
    interfaceAPI api;
    interfaceSocket socket;
    controllerClient controller;
    controllerAPI controllerAPI;
    controllerSocket controllerSocket;

    public StepDefinitions() {
//        driver = hooks.setUp();
        controller = new controllerClient(driver);
        controllerSocket = new controllerSocket();
        controllerAPI = new controllerAPI();
        client = new controllerClient(driver);
        api = new controllerAPI();
        socket = new controllerSocket();
    }

    @Before
    public void startDriver() {
        try {
            driver = hooks.setUp();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @After
    public void clear() {
        try {
            Socket = hooks.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Given("Touch pickup address and input data")
    public void touchPickUp(DataTable table) {
        client.touchPickUp(table);
    }

    @Given("Touch destination address and input data")
    public void touchDestination(DataTable table) {
        client.touchDestination(table);
    }

    @Given("Touch book {string} button")
    public void touchBookType(String string) {
        client.touchBookType(string);
    }

    @Given("Waiting open app success")
    public void waitingHomeScreen() {
        assertTrue(client.waitingHomeScreen());
    }

    @Given("I want to get info ETA from CUE")
    public void getETAFare() {
        api.getETAFare();
    }

    @Given("an api token after login command center")
    public void tokenAuthorization() {
        api.tokenAuthorization();
    }

    @Given("Touch request book button")
    public void requestBook() {
        assertTrue(client.requestBook());
    }

    @Given("I should get the response ETA message matches with")
    public void matchesETAFare(DataTable table) {
        api.matchesETAFare(table);
    }

}
