package me.photomap.web.data.repo.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Session implements Serializable{

    private static final long serialVersionUID = 1L;


    public Session(){}

    public Session(String key){
        this.sessionId = key;
    }

    public Session(Map<String,String> map){
        this.setSessionId(map.get("sessionid"));
        this.setUserId(map.get("userid"));
    }

    private String sessionId;
    private String userId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
