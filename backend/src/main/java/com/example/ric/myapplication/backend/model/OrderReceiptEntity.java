package com.example.ric.myapplication.backend.model;

/**
 * Created by ric on 9/04/16.
 */
public class OrderReceiptEntity {
    Boolean success;
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
