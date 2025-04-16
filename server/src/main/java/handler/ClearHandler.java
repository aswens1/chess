package handler;

import com.google.gson.Gson;
import gamerecords.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }


    public Object clearAllDatabases(Request request, Response response) {
        ClearResult clearResult = clearService.clearAllDatabases();
        response.status(200);
        return new Gson().toJson(clearResult);
    }
}
