package esiapp.esi.model;

import java.io.Serializable;

/**
 * Created on 17-06-2015.
 *
 * @author anil
 */
public class BaseModel implements Serializable {
    private String Response;
    private String message;
    private int statusCode;
    private String ResponseCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }
}
