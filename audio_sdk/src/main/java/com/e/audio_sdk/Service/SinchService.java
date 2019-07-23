package com.e.audio_sdk.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.e.audio_sdk.Api.API;
import com.e.audio_sdk.Api.StringCall;
import com.e.audio_sdk.Api.URLS;
import com.e.audio_sdk.View.UI.UUitil.IO;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Formatter;

import static com.e.audio_sdk.View.UI.UUitil.CallContants.CONSULTATION_ID;
import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_ID;
import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_TOKEN;
import static com.e.audio_sdk.View.UI.UUitil.CallContants.PATIENT_TOKEN;

public class SinchService extends Service {

    private static final String APP_KEY = "2f8e3720-de18-428e-b5fc-966511f9e475";
    private static final String SECRET_KEY = "s/05UFBCVUOVeolAl1zP8g==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";


    private static final String TAG = SinchService.class.getSimpleName();
    private SinchServiceInterface mServiceInterface = new SinchServiceInterface();


    private SinchClient mSinchClient;

    private StartFailedListener mListener;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void stop() {
        if (mSinchClient != null) {
            mSinchClient.terminate();
            mSinchClient = null;
        }
    }

    private boolean isStarted() {
        return (mSinchClient==null && mSinchClient.isStarted());
    }

    @Override
    public void onDestroy() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.terminate();
        }
        super.onDestroy();
    }

    private void createClient(String username) {

        mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext())
                .userId(username)
                .applicationKey(APP_KEY)
                .applicationSecret(SECRET_KEY)
                .environmentHost(ENVIRONMENT)
                .build();
        mSinchClient.setSupportCalling(true);
//        mSinchClient.setSupportManagedPush(true);

        mSinchClient.addSinchClientListener(new MySinchClientListener());
        mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
    }

    private void startClicentcall(String username){

        if(mSinchClient==null && !username.isEmpty() ){

            Log.d("mSinchClient is null","Create new client ");
            createClient(username);
            mSinchClient.start();
        }


    }
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceInterface ;
    }

    public class SinchServiceInterface extends Binder {

        String [] keys ={ CONSULTATION_ID, DOCTOR_TOKEN, PATIENT_TOKEN};

        public Call VoiceCall(String userId,Bundle bundle){

            Map<String, String > payLoad= new HashMap<>();
            payLoad.put("patientName", API.getUsername(SinchService.this));
            payLoad.put("patientId",API.getCustomerId(SinchService.this));

            for( String key:keys){

                payLoad.put(key,bundle.getString(key));
            }

            CallClient callClient= mSinchClient.getCallClient();

            if(callClient==null || !isStarted()){
                startClient(getUserName());
            }
            if(isStarted()){
                return callClient.callUser(userId,payLoad);
            }
            else return null;
        }


        public String getUserName() {
            return mSinchClient!=null ? mSinchClient.getLocalUserId(): "";
        }

        public boolean isStarted() {
            return SinchService.this.isStarted();
        }

        public void onStopClient(){
            stop();
        }

        public void startListener(StartFailedListener failedListener){
            mListener=failedListener;
        }

        public Call getCall(String callId) {
            return mSinchClient.getCallClient().getCall(callId);
        }

        public VideoController getVideoController() {
            if (!isStarted()) {
                return null;
            }
            return mSinchClient.getVideoController();
        }

        public AudioController getAudioController() {
            if (!isStarted()) {
                return null;
            }
            return mSinchClient.getAudioController();
        }
//        public void startClient(String userName){
//
//        }

        public  void startClient(String username){
            startClicentcall(username);
        }

        public void toast(String msg){
            Log.d("uud from service",msg);
        }

        /* Only call this method at the end of outgoing calls */
        public void updateConsultation(Call call, int rating) {
            String consultationId = IO.getData(SinchService.this, CONSULTATION_ID);
            ConsultationStatus status = null;// = ConsultationStatus.IGNORED;

            CallEndCause cause = call.getDetails().getEndCause();
            int duration = call.getDetails().getDuration();

            if (cause == CallEndCause.NO_ANSWER) {
                status = ConsultationStatus.IGNORED;
            } else if (cause == CallEndCause.HUNG_UP /*&& duration >= ContactActivity.FIFTEEN_MINUTES*/) {
                status = ConsultationStatus.COMPLETED;
                //} else if (cause == CallEndCause.HUNG_UP /*&& duration < ContactActivity.FIFTEEN_MINUTES */) {
                //
            } else if (cause == CallEndCause.DENIED) {
                status = ConsultationStatus.DOCTOR_REJECTED;
            }
            else if (cause == CallEndCause.CANCELED) {
                status = ConsultationStatus.CUSTOMER_REJECTED;
            } else {
                status = ConsultationStatus.TERMINATED;
            }

            Map<String, String> params = new HashMap<>();
            params.put("consultationId", consultationId);
            params.put("status", status.name());
            if (rating > -1)
                params.put("rating", String.valueOf(rating));

            StringCall apiCall = new StringCall(SinchService.this);
            apiCall.post(URLS.UPDATE_CONSULTATION, params, response -> {
                Log.d("updateConsultation()", response);
            }, error -> {
                if (error.getMessage() != null)
                    Log.d("updateConsutation Error", error.getMessage());

                if (error.networkResponse == null) {
                    Log.d("updateConsulation error", "Network response is null. No network connection");
                } else {
//                    Log.d("updateConsultation err", Formatter.bytesToString(error.networkResponse.data));
                }
            });

        }

        public void updateConsultation(Call call) {
            updateConsultation(call, -1);
        }

    }





    public interface StartFailedListener {
        void onStartFailed(SinchError error);

        void onStarted();
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientStarted(SinchClient sinchClient) {
            Log.d(TAG, "SinchClient started");
            if(mListener !=null){
                mListener.onStarted();
            }

        }

        @Override
        public void onClientStopped(SinchClient sinchClient) {
            Log.d(TAG ,"SinchClient stopped ");

        }

        @Override
        public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
            if(mListener!=null){
                mListener.onStartFailed(sinchError);
            }
            mSinchClient.terminate();
            mSinchClient=null;

        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {

        }

        @Override
        public void onLogMessage(int i, String s, String s1) {

            switch (i) {
                case Log.DEBUG:
                    Log.d(s, s1);
                    break;
                case Log.ERROR:
                    Log.e(s, s1);
                    break;
                case Log.INFO:
                    Log.i(s, s1);
                    break;
                case Log.VERBOSE:
                    Log.v(s, s1);
                    break;
                case Log.WARN:
                    Log.w(s, s1);
                    break;
            }

        }
    }

    private class SinchCallClientListener implements CallClientListener{

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {



        }
    }


}
