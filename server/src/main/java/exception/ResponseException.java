package exception;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static ResponseException fromJson(InputStream inputStream) {
        var map = new Gson().fromJson(new InputStreamReader(inputStream), HashMap.class);

        var status = ((Double)map.get("status")).intValue();

        String message = map.get("message").toString();

        return new ResponseException(status, message);
    }

}
