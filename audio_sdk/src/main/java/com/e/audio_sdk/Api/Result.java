package com.e.audio_sdk.Api;

import java.util.List;

public class Result<T> {

    private boolean successful;
    private List<T> datalist;
    private String message;
    private T data;


    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public List<T> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<T> datalist) {
        this.datalist = datalist;
        setSuccessful(true);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.successful=false;
    }
    public void setMessage(String mg, boolean status){
        this.message =mg;
        setSuccessful(status);
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        setSuccessful(true);
    }
}
