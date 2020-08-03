package AVISANDROID;

import controllerPackage.*;
import hooksPackage.hooks;
import interfacePackage.*;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class StepDefinitions {
    AndroidDriver driver;
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

    @Given("Touch pickup address and input data")
    public void touchPickUp(List<String> table) {
        client.touchPickUp(table.get(1));
    }

    @Given("Touch destination address and input data")
    public void touchDestination(List<String> table) {
        client.touchDestination(table.get(1));
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
    public void requestBook(String string) {
        client.requestBook();
    }

    @Given("findBookInCUE")
    public void findBookInCUE() {
        api.findBookInCUE();
    }

    @Given("I should get the response ETA message matches with")
    public void matchesETAFare(DataTable table) {
        api.matchesETAFare(table);
    }

}
