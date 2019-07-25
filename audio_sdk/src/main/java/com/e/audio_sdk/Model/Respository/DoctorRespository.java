package com.e.audio_sdk.Model.Respository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.android.volley.Response;
import com.e.audio_sdk.Api.Result;
import com.e.audio_sdk.Api.StringCall;
import com.e.audio_sdk.Api.URLS;
import com.e.audio_sdk.Model.Doctor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorRespository {

 private Context context;
 private StringCall stringCall;
 private static  DoctorRespository instance;

    public static DoctorRespository getInstance(Context ctx) {
        if(instance==null){
            instance=new DoctorRespository(ctx);
        }
        return instance;
    }

    public DoctorRespository(Context context) {
        this.context = context;
        stringCall= new StringCall(context);
    }

    public LiveData<Result<Doctor>> getDoctorSpecialy(int specialId, int page) {

        MutableLiveData<Result<Doctor>> data = new MutableLiveData<>();
        Result<Doctor> result=new Result();
        Map<String, String> params=new HashMap<>();
        params.put("page", String.valueOf(page));

        stringCall.get(URLS.SPECIALTY_DOCTORS + specialId, params, response -> {
            log("RESPONSE " + response);
            try {

                JSONObject obj = new JSONObject(response);
                if (obj.has("code") && obj.getInt("code") == 0 && !obj.isNull("doctors")) {
                    List<Doctor> List = new ArrayList<>();
                    if (!obj.isNull("doctors")) {
                        JSONArray jsonArray = obj.getJSONArray("doctors");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Doctor doctor = Doctor.parse(jsonArray.getJSONObject(i));
                            List.add(doctor);
                        }
                        result.setDatalist(List);
                        log("SUCCESSFUL");
                    } else {
                        result.setMessage(obj.getString("description"));
                    }

                }

            } catch (JSONException e) {
                log(e.getMessage());
                result.setMessage(e.getMessage());
            }
            data.setValue(result);

        }, error -> {
            log("VOLLEY ERROR");
            log(error.getMessage());
            if(error.networkResponse==null){
                log("");
                result.setMessage("Please check your network");
            }
        });
        return data;
    }

    public LiveData<Result<Doctor>> getRandomDoctor() {
        MutableLiveData<Result<Doctor>> data=new MutableLiveData<>();
        Result<Doctor> result=new Result();
        Map<String, String> params=new HashMap<>();


        stringCall.get(URLS.DOCTORS_RANDOM,params, response -> {
            log("RESPONSE " + response);
            try {
                JSONObject obj =new JSONObject(response);
                if (obj.has("code") && obj.getInt("code")==0){
                    List<Doctor> list = new ArrayList<>();
                    if(!obj.isNull("doctors") && obj.getInt("totalCount")>0){
                        JSONArray jsonArray= obj.getJSONArray("doctors");
                        for(int i =0; i<jsonArray.length(); i++){
                           Doctor doctor= Doctor.parse(jsonArray.getJSONObject(i));
                            list.add(doctor);
                        }
                        result.setDatalist(list);
                        log(" SUCCESSFUL" );
                    }
                    else {
                        result.setMessage(obj.getString(""));
                    }

                }
            }catch (JSONException e){
                log(e.getMessage());
                result.setMessage(e.getMessage());

            }
            data.setValue(result);

        },error -> {
            log("VOLLEY ERROR");
            log(error.getMessage());
            if(error.networkResponse==null){
                result.setMessage("Please Check Your Internet Connection");

            }
            data.setValue(result);

        });
        return data;

    }

    public LiveData<Result<Doctor>> getAppointedDoctor(int page) {

        MutableLiveData<Result<Doctor>> data=new MutableLiveData<>();
        Result<Doctor> result=new Result();
        Map<String, String> params= new HashMap<>();

//        stringCall.get(URLS.);
        return data;
    }

    public LiveData<Result<Doctor>> getSearchDoctor(String doctorname, int page){

        MutableLiveData<Result<Doctor>> data=new MutableLiveData<>();
        Result<Doctor>result=new Result<>();

        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        stringCall.get(URLS.DOCTORS_SEARCH + doctorname ,params,response -> {
            log("RESPONSE " + response);
            try {
                JSONObject obj = new JSONObject(response);
                if(obj.has("code")&& !obj.isNull("doctors")&& obj.getInt("code")==0){
                    List<Doctor> list =new ArrayList<>();
                    if(!obj.isNull("doctors")){
                        JSONArray notes= obj.getJSONArray("doctors");
                        for(int i =0 ; i<notes.length();i++){
                            Doctor doctor =Doctor.parse(notes.getJSONObject(i));
                            list.add(doctor);
                        }
                        result.setDatalist(list);
                        log("SUCCESSFUL" );
                    }else {
                        result.setMessage(obj.getString("descriptions"));
                    }
                }
            }catch (JSONException e){
                log(e.getMessage());
            }
            data.setValue(result);

        },error -> {
            log("VOLLEY ERROR");
            log(error.getMessage());
            if(error.networkResponse==null){
                log("Network Error");
                result.setMessage("Please Check Your Network");
            }
            data.setValue(result);

        });
        return data;

    }

    private static void log(String string){
        Log.d("DoctorRepository ", "_--__-_-_-_---_-_-_-_-_-_-_-_-_-_-_-_-_-_-_____-_-_-_-_-_  " + string);
    }
}
