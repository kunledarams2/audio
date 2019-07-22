package com.e.audio_sdk.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Doctor {

    private String specialization;
    private String firstname;
    private String lastname;
    private String avatar;
    private int id;
    private double rating;
    private String hospital;
    private boolean available;
    private String specailtyId;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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

    public String getFullname(){
        return  " " + firstname  + " " + lastname;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getSpecailtyId() {
        return specailtyId;
    }

    public void setSpecailtyId(String specailtyId) {
        this.specailtyId = specailtyId;
    }

    public static Doctor parse(JSONObject object)throws JSONException {
        Doctor doctor=new Doctor();
        doctor.setId(object.getInt("id"));
        doctor.setFirstname(object.getString("firstname"));
        doctor.setLastname(object.getString("lastname"));
        doctor.setHospital(object.getString("hospital"));
        doctor.setAvatar(object.getString("image"));
        doctor.setSpecailtyId(object.getString("specialty"));

        if (object.has("onlineStatus") && !object.isNull("onlineStatus")
                && object.getString("onlineStatus").equals("ONLINE")){
            doctor.setAvailable(true);
        }
        else doctor.setAvailable(false);
        doctor.setRating(object.getDouble("rating"));
        return doctor;
    }
}
