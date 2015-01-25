package me.photomap.web.data.repo.model;

import java.io.Serializable;

/**
 * Created by craigbrookes on 04/01/15.
 */
public class LoginDetails implements Serializable{


    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
