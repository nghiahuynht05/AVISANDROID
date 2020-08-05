package controllerPackage;

import interfacePackage.interfaceSocket;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertNotNull;

public class controllerSocket implements interfaceSocket {
    private Socket socket;
    String bookId;
    private static final Logger LOGGER = LogManager.getLogger(controllerSocket.class);

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
        return IO.socket(getConfig("socketServer") + path, opts);
    }

    String nsp() {
        return "/";
    }

    IO.Options createOptions() {
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        return opts;
    }

    String getConfig(String string) {
        InputStream inputStream;
        Properties prop = new Properties();
        String propFileName = "config.properties";

        inputStream = controllerSocket.class.getClassLoader().getResourceAsStream(propFileName);
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

    public void connectSocket(String string, List<String> table) throws URISyntaxException {
        String rqEvent = "";
        String acceptEvent = "";

        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = false;
        socket = IO.socket(getConfig("socketServer"), opts);

        if (string.equals("Now")) {
            rqEvent = "rqJob";
            acceptEvent = "accept";
        } else {
            rqEvent = "rqJobPre";
            acceptEvent = "acceptPre";
        }

        String finalAcceptEvent = acceptEvent;
        String finalRqEvent = rqEvent;

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... objects) {

                JSONObject phone = new JSONObject();
                try {
                    phone.put("number", table.get(8));
                    phone.put("country", table.get(9));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject obj = new JSONObject();
                try {
                    obj.put("platform", table.get(13));
                    obj.put("phone", phone);
                    obj.put("fleetId", table.get(12));
                    obj.put("appType", table.get(14));
                    obj.put("verifyCode", table.get(11));
                    obj.put("ime", table.get(10));
                    obj.put("password", table.get(15));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("register", obj);

            }
        }).on("register", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject) args[0];
                LOGGER.info("Event: 'register': {}", obj);
                JSONObject location = new JSONObject();
                try {
                    location.put("geo", "[108.21174351895205,16.059379170460254]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("updateLocation");
            }

        }).on(rqEvent, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {

                JSONObject object = (JSONObject) objects[0];
                JSONObject params = new JSONObject();
                try {
                    String bookId = object.getString("bookId");
                    String bookFrom = object.getString("bookFrom");
                    params.put("bookId", bookId);
                    params.put("bookFrom", bookFrom);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                values.offer(objects);
                LOGGER.info("Event: '{}': {}", finalRqEvent, object);
                socket.emit(finalAcceptEvent, params);
            }
        }).on(finalAcceptEvent, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject response = (JSONObject) objects[0];
                LOGGER.info("Event: '{}': {}", finalAcceptEvent, response);
                try {
                    bookId = response.getString("BookId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            socket.connect();
            Object[] args = (Object[]) values.take();
            assertNotNull(args);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void gListBooking() {
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = false;
        try {
            socket = IO.socket(getConfig("socketServer"), opts);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {

                }
            }).on("register", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {

                }
            }).on("gListBooking", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {

                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
