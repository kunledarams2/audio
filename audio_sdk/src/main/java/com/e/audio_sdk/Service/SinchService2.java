package com.e.audio_sdk.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.video.VideoController;

public class SinchService2 extends Service {

    private static final String APP_KEY = "2f8e3720-de18-428e-b5fc-966511f9e475";
    private static final String SECRET_KEY = "s/05UFBCVUOVeolAl1zP8g==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";

    public SinchServiceInterface mSinchServiceInterface = new SinchServiceInterface();
    private static final String TAG = SinchService.class.getSimpleName();

    private SinchClient mSinchClient;
    private String userId;

//    private SinchClientListener mListener;
    private StartFailedListener startFailedListener;

    @Override
    public IBinder onBind(Intent intent) {
        return mSinchServiceInterface;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {

        if(mSinchClient!=null && mSinchClient.isStarted()){
            mSinchClient.terminate();
        }
        super.onDestroy();
    }

    private void stop() {
        if (mSinchClient != null) {
            mSinchClient.terminate();
            mSinchClient = null;
        }
    }

    private boolean isStarted() {
        return (mSinchClient ==null && mSinchClient.isStarted());
    }

    private void start(String userName){
        if(mSinchClient==null){
            userId=userName;
            mSinchClient= Sinch.getSinchClientBuilder().context(getApplicationContext())
                    .userId(userName)
                    .applicationKey(APP_KEY)
                    .applicationSecret(SECRET_KEY)
                    .environmentHost(ENVIRONMENT)
                    .build();

            mSinchClient.setSupportCalling(true);
//            mSinchClient.setSupportManagedPush(true);

            mSinchClient.addSinchClientListener(new MySinchClientListener());
            mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        }
    }
    public class SinchServiceInterface extends Binder {

        public Call userCall(String userId){
            return  mSinchClient.getCallClient().callUser(userId);
        }

        public String getUserName() {
            return mSinchClient!=null ? mSinchClient.getLocalUserId(): "";
        }

        public boolean isStarted() {
            return SinchService2.this.isStarted();
        }

        public void startClient(String userName){
            start(userName);
        }
        public void onStopClient(){
            stop();
        }

        public void startListener(StartFailedListener failedListener){
            startFailedListener=failedListener;
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

    }

    public interface  StartFailedListener{
        void onStartFailed(SinchError error);

        void onStarted();
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientStarted(SinchClient sinchClient) {
            if(startFailedListener !=null){
                startFailedListener.onStarted();
            }

        }

        @Override
        public void onClientStopped(SinchClient sinchClient) {
            Log.d(TAG ,"SinchClient stopped ");
        }

        @Override
        public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {

            if(startFailedListener!=null){
                startFailedListener.onStartFailed(sinchError);
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

    private class  SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {

        }
    }

}
