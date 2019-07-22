package com.e.audio_sdk.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SDK_User {

    private String firstname;
    private String lastname;
    private String dob;
    private String phone;
    private String email;
    private String gender;
    private String password;
    private String username;

    public SDK_User(String firstname, String lastname, String dob, String phone, String email, String gender, String password, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.password = password;
    }

    public SDK_User() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JSONObject toJSON() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("dob", getDob());
        obj.put("gender", getGender());
        obj.put("email", getEmail());
        obj.put("firstname", getFirstname());
        obj.put("lastname", getLastname());
        obj.put("username", getUsername());
        obj.put("phone", getPhone());
        obj.put("password", getPassword());

        return obj;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("dob", getDob());
        map.put("email", getEmail());
        map.put("firstname", getFirstname());
        map.put("gender", getGender());
        map.put("lastname", getLastname());
        map.put("phone", getPhone());
        map.put("password", getPassword());
        map.put("username", getUsername());


        return map;
    }


    public static SDK_User parse(JSONObject object) throws JSONException {

        SDK_User sdk_user = new SDK_User();

        if (object.has("dob")) {
            sdk_user.setDob(object.getString("dob"));
        }
        if (object.has("gender")) {
            sdk_user.setGender(object.getString("gender"));
        }
        if (object.has("email")) {
            sdk_user.setEmail(object.getString("email"));
        }
        if (object.has("firstname")) {
            sdk_user.setFirstname(object.getString("firstname"));
        }
        if (object.has("lastname")) {
            sdk_user.setLastname(object.getString("lastname"));
        }
        if (object.has("phone")) {
            sdk_user.setPhone(object.getString("phone"));

        }
        return sdk_user;
    }
}
