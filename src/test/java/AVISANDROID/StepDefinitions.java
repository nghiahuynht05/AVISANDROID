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
import io.cucumber.java.en.Given;
import io.socket.client.Socket;

import java.util.List;

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
        driver = hooks.setUp();
        controller = new controllerClient(driver);
        controllerSocket = new controllerSocket();
        controllerAPI = new controllerAPI();
        client = new controllerClient(driver);
        api = new controllerAPI();
        socket = new controllerSocket();
    }

    @After
    public void clear() {
        try {
            hooks.clear();
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

    @Given("Set time pickup before {string} hours and {string} minustes from the current time")
    public void selectPickUpTime(String hours, String mins) {
        client.selectPickUpTime(hours, mins);
    }

    @Given("Select service type {string}")
    public void selectServiceType(String string) {
        client.selectServiceType(string);
    }

    @Given("Select payment method {string}")
    public void selectPaymentMethod(String string) {
        client.selectPaymentMethod(string);
    }

    @Given("Select type of ride {string}")
    public void selectTypeRide(String string) {
        client.selectTypeRide(string);
    }

    @Given("Select car type {string}")
    public void selectCarType(String string) {
        client.selectCarType(string);
    }

    @Given("I want to get content message")
    public void getContentMsg() {
        client.getContentMsg();
    }

    @Given("I should get the response message matches with")
    public void matchResponseMsg(List<String> table) {
        assertTrue(client.matchResponseMsg(table.get(1)));
        ;
    }

    @Given("Waiting open app success")
    public void waitingHomeScreen() {
        assertTrue(client.waitingHomeScreen());
    }

    @Given("I want to get info ETA from CUE")
    public void getETAFare() {
        api.getETAFare();
    }

    @Given("I want to get info payment method from CUE")
    public void getPaymentMethod() {
        api.getPaymentMethod();
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
        assertTrue(api.matchesETAFare(table));
    }

    @Given("I should get the response payment method message matches with")
    public void matchesPaymentMethod(DataTable table) {
        assertTrue(api.matchesPaymentMethod(table));
    }

    @Given("Test Socket")
    public void testSocket() {
        socket.gListBooking();
    }
}
