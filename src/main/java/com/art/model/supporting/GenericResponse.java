package com.art.model.supporting;

import java.io.Serializable;

public class GenericResponse implements Serializable{
    private String message;
    private String error;
    private int cnt;

    public GenericResponse() {
        super();
    }

    public GenericResponse(String message) {
        super();
        this.message = message;
    }

    public GenericResponse(String message, String error) {
        super();
        this.message = message;
        this.error = error;
    }

    public GenericResponse(String message, String error, int cnt) {
        super();
        this.message = message;
        this.error = error;
        this.cnt = cnt;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }

    public String getError(){
        return error;
    }
    public void setError(String error){
        this.error = error;
    }

    public int getCnt(){
        return cnt;
    }
    public void setCnt(int cnt){
        this.cnt = cnt;
    }
}
