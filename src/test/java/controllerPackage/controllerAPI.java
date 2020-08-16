package controllerPackage;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import interfacePackage.interfaceAPI;
import io.cucumber.datatable.DataTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertNotNull;

public class controllerAPI implements interfaceAPI {
    public static Logger LOGGER = LogManager.getLogger(controllerAPI.class);
    JSONObject actualData = new JSONObject();
    JSONObject expectData = new JSONObject();
    public static String tokenAuth;
    public static String bookId;

    public static String getConfig(String string) {
        InputStream inputStream;
        Properties prop = new Properties();
        String propFileName = "config.properties";

        inputStream = controllerAPI.class.getClassLoader().getResourceAsStream(propFileName);
        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return prop.getProperty(string);
    }

    public String tokenAuthorization() throws JSONException {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();
        HttpResponse<JsonNode> response = null;

        JSONObject param = new JSONObject();
        param.put("username", getConfig("userNameCC"));
        param.put("password", getConfig("passwordCC"));
        String paramString = param.toString();
        try {
            response = Unirest.post(getConfig("apiServer") + "/api/user/login")
                    .header("Content-Type", "application/json")
                    .body(paramString)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = response.getBody().getObject();
        values.offer(jsonObject);
        try {
            JSONObject args = (JSONObject) values.take();
            assertNotNull(args);
            JSONObject resObj = jsonObject.getJSONObject("res");
            String token = resObj.getString("token");
            tokenAuth = token;
            LOGGER.info("Token Authorization: {}", token);
            return token;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findBookInCUE() {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.post(getConfig("apiServer") + "/api/booking/find")
                    .header("Authorization", tokenAuth)
                    .header("Content-Type", "application/json")
                    .body("{\"limit\":50,\"page\":0,\"sort\":{\"time\":-1},\"query\":{\"txtSearch\":\"\",\"bookingService\":\"all\",\"supportService\":\"all\",\"corporateId\":null,\"bookingType\":\"all\",\"rideType\":\"all\",\"dateFrom\":null,\"dateTo\":null,\"operator\":\"\",\"bookFrom\":[],\"carType\":[],\"status\":[\"pending\",\"pre\",\"queue\",\"offered\",\"confirmed\",\"booked\",\"engaged\",\"droppedOff\",\"arrived\",\"action\"],\"fleetId\":\"avisbudget\",\"vip\":null,\"searchBy\":\"bookId\"}}")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = response.getBody().getObject();
        values.offer(jsonObject);
        try {
            JSONObject args = (JSONObject) values.take();
            assertNotNull(args);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("/api/booking/find result: {}", jsonObject);
        JSONObject resObj = jsonObject.getJSONObject("res");
        JSONArray listArray = resObj.getJSONArray("list");
        JSONObject object = listArray.getJSONObject(0);
        LOGGER.info("/api/booking/find result: {}", listArray);
        LOGGER.info("/api/booking/find result: {}", object.get("bookId"));
        return (String) object.get("bookId");
    }

    public void getETAFare() {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();
        bookId = findBookInCUE();
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(getConfig("apiServer") + "/api/booking/details?bookId=" + bookId)
                    .header("Authorization", tokenAuth)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = response.getBody().getObject();
        values.offer(jsonObject);
        try {
            JSONObject args = (JSONObject) values.take();
            assertNotNull(args);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject resObj = jsonObject.getJSONObject("res");
        JSONObject requestObj = resObj.getJSONObject("request");
        actualData.put("typeRate", requestObj.get("typeRate"));
        actualData.put("type", requestObj.get("type"));
        JSONObject estimateObj = requestObj.getJSONObject("estimate");
        JSONObject fareObj = estimateObj.getJSONObject("fare");
        actualData.put("distance", estimateObj.get("distance"));
        try {
            actualData.put("etaFare", fareObj.get("etaFare"));
        } catch (Exception ex) {
            actualData.put("etaFare", 0);
        }
        actualData.put("time", estimateObj.get("time"));
        LOGGER.info("ETA fare find booking: {}: {}", bookId, actualData);
    }

    public Boolean matchesETAFare(DataTable dataTable) {
        for (Map<Object, Object> data : dataTable.asMaps(String.class, String.class)) {
            try {
                expectData.put("distance", data.get("distance"));
                expectData.put("etaFare", data.get("etaFare"));
                expectData.put("time", data.get("time"));
                expectData.put("typeRate", (data.get("typeRate") != null) ? data.get("typeRate") : null);
                expectData.put("type", (data.get("type") != null) ? data.get("type") : null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("Expected data: {}", expectData);
        LOGGER.info("Actual data: {}", actualData);
        String expectDataString = expectData.toString();
        String responseDataString = actualData.toString();
        if (expectDataString.equals(responseDataString)) {
            LOGGER.info("Expected data matching is response data");
            System.out.println();
            return true;
        } else {
            LOGGER.info("Expected data NOT matching is response data");
            return false;
        }
    }

}
