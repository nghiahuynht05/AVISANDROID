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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class controllerAPI implements interfaceAPI {
    public static Logger LOGGER = LogManager.getLogger(controllerAPI.class);
    JSONObject actualData = new JSONObject();
    JSONObject expectData = new JSONObject();

    public String getConfig(String string) {
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
        String config = prop.getProperty(string);
        return config;
    }

    public String httpRequestAPI() throws JSONException {
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
            assertThat(args.length(), is(1));
            JSONObject resObj = jsonObject.getJSONObject("res");
            String token = resObj.getString("token");
            LOGGER.info("Token Authorization: {}", token);
            return token;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void findBookInCUE() {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();

        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.post(getConfig("apiServer") + "/api/booking/find")
                    .header("Authorization", httpRequestAPI())
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
//            assertThat(args.length(), is(1));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject resObj = jsonObject.getJSONObject("res");
        JSONArray listArray = resObj.getJSONArray("list");
        Object object = listArray.get(0);
        LOGGER.info("ETA fare find booking: {}", object);
    }

    public void getETAFare() {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();

        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(getConfig("apiServer") + "/api/booking/details?bookId=%d" + getConfig("bookId"))
                    .header("Authorization", httpRequestAPI())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = response.getBody().getObject();
        values.offer(jsonObject);
        try {
            JSONObject args = (JSONObject) values.take();
            assertThat(args.length(), is(1));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject resObj = jsonObject.getJSONObject("res");
        JSONObject requestObj = resObj.getJSONObject("request");
        JSONObject estimateObj = requestObj.getJSONObject("estimate");
        actualData.put("estimateValue", estimateObj.get("estimateValue"));
        actualData.put("estimateValue", estimateObj.get("estimateValue"));
        actualData.put("estimateValue", estimateObj.get("estimateValue"));
        LOGGER.info("ETA fare find booking: {}: {}", getConfig("bookId"), actualData);
    }

    public Boolean matchesETAFare(DataTable dataTable) {
        for (Map<Object, Object> data : dataTable.asMaps(String.class, String.class)) {
            try {
                expectData.put("estimateValue", data.get("estimateValue"));
                expectData.put("distanceValue", data.get("distanceValue"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("Expected data: {}", expectData);
        LOGGER.info("Actual data: {}", actualData);
        String expectDateString = expectData.toString();
        String responseDateString = actualData.toString();
        if (expectDateString.equals(responseDateString)) {
            LOGGER.info("Expected data matching is response data");
            return true;
        } else {
            LOGGER.info("Expected data NOT matching is response data");
            return false;
        }
    }
}
