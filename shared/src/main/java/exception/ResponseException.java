package exception;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ResponseException extends RuntimeException {

    final int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int statusCode() {
        return statusCode;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", statusCode));
    }

    public static ResponseException fromJson(String json) {
        var map = new Gson().fromJson(json, HashMap.class);

        Double status = (map.get("status") instanceof Double) ? (Double) map.get("status") :
                        (map.get("statusCode") instanceof Double) ? (Double) map.get("statusCode") :
                        500;

//        var status = ((Double)map.get("status")).intValue();

        String message = map.get("message").toString();

        return new ResponseException(status.intValue(), message);
    }
}
