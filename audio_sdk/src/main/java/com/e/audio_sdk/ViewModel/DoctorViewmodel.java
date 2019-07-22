package com.e.audio_sdk.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;


import com.e.audio_sdk.Api.Result;
import com.e.audio_sdk.Model.Doctor;
import com.e.audio_sdk.Model.Respository.DoctorRespository;

import java.util.List;

public class DoctorViewmodel extends AndroidViewModel {

    private Application application;
    private LiveData<Result<Doctor>> liveData;
    private MutableLiveData<List<Doctor>> mutableLiveData;
    private MediatorLiveData<Result<Doctor>> mediatorLiveData;

    public DoctorViewmodel(@NonNull Application application) {
        super(application);
        this.application= application;
        liveData =new MutableLiveData<>();
        mediatorLiveData=new MediatorLiveData<>();
    }


    public void fetchSpecialyDoctor( int SpecialyDoctorId, int page){
        mediatorLiveData.removeSource(liveData);
        liveData= DoctorRespository.getInstance(application.getApplicationContext()).
                getDoctorSpecialy(SpecialyDoctorId,page);
        mediatorLiveData.addSource(liveData,mediatorLiveData::setValue);
    }

    public void fetchRadomDoctor(){
        mediatorLiveData.removeSource(liveData);
        liveData =DoctorRespository.getInstance(application.getApplicationContext())
                .getRandomDoctor();
        mediatorLiveData.addSource(liveData,mediatorLiveData::setValue);
    }

    public void fetchAppointeddoctor(int page){
        mediatorLiveData.removeSource(liveData);
        liveData=DoctorRespository.getInstance(application.getApplicationContext())
                .getAppointedDoctor(page);
        mediatorLiveData.addSource(liveData,mediatorLiveData::setValue);
    }

    public void fetchSearchDoctor(String docName, int page){

        mediatorLiveData.removeSource(liveData);
        liveData = DoctorRespository.getInstance(application.getApplicationContext())
                .getSearchDoctor(docName,page);
        mediatorLiveData.addSource(liveData, mediatorLiveData::setValue);
    }

    public MediatorLiveData<Result<Doctor>> getMediatorLiveData() {
        return mediatorLiveData;
    }
}
