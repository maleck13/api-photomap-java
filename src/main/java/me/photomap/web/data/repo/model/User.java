package me.photomap.web.data.repo.model;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;


@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NotEmpty
    private String userName;

    @NotEmpty
    @Indexed(unique = true)
    private String email;

    @NotEmpty
    private String password;

    private boolean active = true;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public Map<String,String> toMap(){
        Map<String,String> userMap = new HashMap<String,String>();
        userMap.put("userName",this.getUserName());
        userMap.put("id",this.getId().toString());
        return userMap;
    }
}
