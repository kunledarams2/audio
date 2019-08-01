//package com.e.audio_sdk.Service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.Messenger;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//
//import com.e.audio_sdk.Api.API;
//import com.e.audio_sdk.Api.StringCall;
//import com.e.audio_sdk.Api.URLS;
//import com.e.audio_sdk.View.UI.UUitil.IO;
//import com.sinch.android.rtc.AudioController;
//import com.sinch.android.rtc.ClientRegistration;
//import com.sinch.android.rtc.NotificationResult;
//import com.sinch.android.rtc.Sinch;
//import com.sinch.android.rtc.SinchClient;
//import com.sinch.android.rtc.SinchClientListener;
//import com.sinch.android.rtc.SinchError;
//import com.sinch.android.rtc.calling.Call;
//import com.sinch.android.rtc.calling.CallClient;
//import com.sinch.android.rtc.calling.CallClientListener;
//import com.sinch.android.rtc.calling.CallEndCause;
//import com.sinch.android.rtc.video.VideoController;
//
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.e.audio_sdk.View.UI.UUitil.CallContants.CALL_ID;
//import static com.e.audio_sdk.View.UI.UUitil.CallContants.CALL_TYPE;
//import static com.e.audio_sdk.View.UI.UUitil.CallContants.CONSULTATION_ID;
//import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_AVATAR;
//import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_ID;
//import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_NAME;
//import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_TOKEN;
//import static com.e.audio_sdk.View.UI.UUitil.CallContants.PATIENT_TOKEN;
//
//
//public class CallService extends Service {
//
//    private static final String APP_KEY = "2f8e3720-de18-428e-b5fc-966511f9e475";
//    private static final String APP_SECRET = "s/05UFBCVUOVeolAl1zP8g==";
//    private static final String ENVIRONMENT = "sandbox.sinch.com";
//
//    public static final int MESSAGE_PERMISSIONS_NEEDED = 1;
//    public static final String REQUIRED_PERMISSION = "REQUIRED_PERMISSION";
//    public static final String MESSENGER = "MESSENGER";
//    private Messenger messenger;
//
//
//    static final String TAG = CallService.class.getSimpleName();
//
//    private CallServiceInterface mCallServiceInterface = new CallServiceInterface();
//    private SinchClient mSinchClient;
//
//    private StartFailedListener mListener;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        log("onCreate()");
//        attemptAutoStart();
//    }
//
//    private void attemptAutoStart(){
//        String userName = IO.getData(this, API.MY_UUID);
//        if (!userName.isEmpty() && messenger != null) {
//            start(userName);
//        }
//    }
//
//    private void createClient(String username) {
//        mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext())
//                .userId(username)
//                .applicationKey(APP_KEY)
//                .applicationSecret(APP_SECRET)
//                .environmentHost(ENVIRONMENT)
//                .build();
//
//        mSinchClient.setSupportCalling(true);
//        mSinchClient.setSupportManagedPush(true);
//        //mSinchClient.startListeningOnActiveConnection();
//        mSinchClient.addSinchClientListener(new MySinchClientListener());
//        mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
//    }
//
//    @Override
//    public void onDestroy() {
//        if (mSinchClient != null && mSinchClient.isStarted()) {
//            mSinchClient.terminate();
//        }
//        super.onDestroy();
//    }
//
//    private void start(String username) {
//        boolean permissionGranted = true;
//        if (mSinchClient == null && username != null && !username.isEmpty()) {
//            //mSettings.setUsername(username);
//            log("mSinchClient is null, trying to create another with username = " +username);
//            createClient(username);
//            log("trying to start sinch client");
//            mSinchClient.start();
//            if (isStarted()) {
//                log("Sinch client started");
//            } else {
//                log("Sinch client failed to start");
//            }
//        }
//
//        /*try {
//            mSinchClient.checkManifest();
//        } catch (MissingPermissionException e) {
//            log("Check manifest error "+ e.getMessage());
//            permissionGranted = false;
//            if (messenger != null) {
//                log("messanger is NOT null");
//                Message message = Message.obtain();
//                Bundle bundle = new Bundle();
//                bundle.putString(REQUIRED_PERMISSION, e.getRequiredPermission());
//                message.setData(bundle);
//                message.what = MESSAGE_PERMISSIONS_NEEDED;
//
//                try {
//                    messenger.send(message);
//                } catch (RemoteException re) {
//                    log("Error sending message " + re.getMessage());
//                    re.printStackTrace();
//                }
//            } else {
//                log("messanger is null");
//            }
//        }
//
//        if (permissionGranted) {
//            Log.d(TAG, "Starting SinchClient");
//            log("permissions granted. Starting sinch client");
//            mSinchClient.start();
//        } else {
//            log("Permission NOT granted");
//        }*/
//    }
//
//    private void stop() {
//        if (mSinchClient != null) {
//            mSinchClient.terminate();
//            mSinchClient = null;
//        }
//    }
//
//    private boolean isStarted() {
//        return (mSinchClient != null && mSinchClient.isStarted());
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        log("onBind()");
//        messenger = intent.getParcelableExtra(MESSENGER);
//        if (!isStarted()) {
//            log("AttemptAutoStart()");
//            attemptAutoStart();
//        } else {
//            log("CLIENT STARTED");
//        }
//        return mCallServiceInterface;
//    }
//
//    public class CallServiceInterface extends Binder {
//        String[] keys = {CONSULTATION_ID, PATIENT_TOKEN, DOCTOR_TOKEN};
//
//        public Call callUser(String userId, Bundle bundle) {
//            Map<String, String> payload = new HashMap<>();
//            payload.put("patientId", API.getCustomerId(CallService.this));
//            payload.put("patientName", API.getUsername(CallService.this));
//
//            for (String key : keys) {
//                payload.put(key, bundle.getString(key));
//            }
//
//            CallClient client = mSinchClient.getCallClient();
//            if (client == null || !isStarted()) {
//                startClient(getUsername());
//            }
//            if (isStarted()) {
//                return client.callUser(userId, payload);
//            } else {
//                return null;
//            }
//        }
//
//        public Call videoCallUser(String userId, Bundle bundle) {
//            Map<String, String> payload = new HashMap<>();
//            payload.put("patientId", API.getCustomerId(CallService.this));
//            payload.put("patientName", API.getUsername(CallService.this));
//
//            for (String key : keys) {
//                payload.put(key, bundle.getString(key));
//            }
//
//            CallClient client = mSinchClient.getCallClient();
//            if (client == null || !isStarted()) {
//                startClient(getUsername());
//            }
//            if (isStarted()) {
//                return client.callUserVideo(userId, payload);
//            } else {
//                return null;
//            }
//
//            //return .callUserVideo(userId);
//        }
//
//        public String getUsername() {
//            return mSinchClient != null ? mSinchClient.getLocalUserId() : "";
//        }
//
//        public void retryStartAfterPermissionGranted() {
//            log("retrying AutoStart");
//            if (!isStarted())
//            CallService.this.attemptAutoStart();
//        }
//
//        public boolean isStarted() {
//            return CallService.this.isStarted();
//        }
//
//        public void startClient(String userName) {
//            start(userName);
//        }
//
//        public void stopClient() {
//            stop();
//        }
//
//        public void setStartListener(StartFailedListener listener) {
//            mListener = listener;
//        }
//
//        public Call getCall(String callId) {
//            return mSinchClient.getCallClient().getCall(callId);
//        }
//
//        public AudioController getAudioController() {
//            if (!isStarted()) {
//                return null;
//            }
//            return mSinchClient.getAudioController();
//        }
//
//        public VideoController getVideoController() {
//            if (!isStarted()) {
//                return null;
//            }
//            return mSinchClient.getVideoController();
//        }
//
//        public NotificationResult relayRemotePushNotificationPayload(final Map payload) {
//            String myCallerId = IO.getData(CallService.this, API.MY_UUID);
//            if (mSinchClient == null && !myCallerId.isEmpty()) {
//                createClient(myCallerId);
//            } else if (mSinchClient == null && myCallerId.isEmpty()) {
//                Log.e(TAG, "Can't start a SinchClient as no username is available, unable to relay push.");
//                return null;
//            }
//            return mSinchClient.relayRemotePushNotificationPayload(payload);
//        }
//
//        /* Only call this method at the end of outgoing calls */
//        public void updateConsultation(Call call, int rating, String consultationId) {
////            String consultationId = IO.getData(CallService.this, CONSULTATION_ID);
//            ConsultationStatus status = null;// = ConsultationStatus.IGNORED;
//
//            CallEndCause cause = call.getDetails().getEndCause();
//            int duration = call.getDetails().getDuration();
//
//            if (cause == CallEndCause.NO_ANSWER) {
//                status = ConsultationStatus.DOCTOR_MISSED_CALL;
//            } else if (cause == CallEndCause.HUNG_UP /*&& duration >= ContactActivity.FIFTEEN_MINUTES*/) {
//                status = ConsultationStatus.COMPLETED;
//            //} else if (cause == CallEndCause.HUNG_UP /*&& duration < ContactActivity.FIFTEEN_MINUTES */) {
//                //
//            } else if (cause == CallEndCause.DENIED) {
//                status = ConsultationStatus.DOCTOR_REJECTED;
//            }
//            else if (cause == CallEndCause.CANCELED) {
//                status = ConsultationStatus.CUSTOMER_REJECTED;
//            } else {
//                status = ConsultationStatus.TERMINATED;
//            }
//
//            Map<String, String> params = new HashMap<>();
//            params.put("consultationId", consultationId);
//            params.put("status", status.name());
//            if (rating > -1)
//                params.put("rating", String.valueOf(rating));
//
//            StringCall apiCall = new StringCall(CallService.this);
//            apiCall.post(URLS.UPDATE_CONSULTATION, params, response -> {
//                Log.d("updateConsultation()", response);
//            }, error -> {
//                if (error.getMessage() != null)
//                    Log.d("updateConsutation Error", error.getMessage());
//
//                if (error.networkResponse == null) {
//                    Log.d("updateConsulation error", "Network response is null. No network connection");
//                } else {
////                    Log.d("updateConsultation err", Formatter.bytesToString(error.networkResponse.data));
//                }
//            });
//
//        }
//
//        public void updateConsultation(Call call ,  String consultationId) {
//            updateConsultation(call, -1, consultationId);
//        }
//
//
//    }
//
//    public interface StartFailedListener {
//        void onStartFailed(SinchError error);
//
//        void onStarted();
//    }
//
//    private class MySinchClientListener implements SinchClientListener {
//
//        @Override
//        public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
//            if (mListener != null) {
//                mListener.onStartFailed(sinchError);
//            }
//            mSinchClient.terminate();
//            mSinchClient = null;
//        }
//
//        @Override
//        public void onClientStarted(SinchClient sinchClient) {
//            Log.d(TAG, "SinchClient started");
//            if (mListener != null) {
//                mListener.onStarted();
//            }
//        }
//
//        @Override
//        public void onClientStopped(SinchClient sinchClient) {
//
//            Log.d(TAG, "SinchClient stopped");
//        }
//
//        @Override
//        public void onLogMessage(int level, String area, String message) {
//            switch (level) {
//                case Log.DEBUG:
//                    Log.d(area, message);
//                    break;
//                case Log.ERROR:
//                    Log.e(area, message);
//                    break;
//                case Log.INFO:
//                    Log.i(area, message);
//                    break;
//                case Log.VERBOSE:
//                    Log.v(area, message);
//                    break;
//                case Log.WARN:
//                    Log.w(area, message);
//                    break;
//            }
//        }
//
//        @Override
//        public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {
//
//        }
//    }
//
//    private class SinchCallClientListener implements CallClientListener {
//        @Override
//        public void onIncomingCall(CallClient callClient, Call call) {
//
//            Bundle bundle = new Bundle();
//
//            if (call.getHeaders().size() > 0) {
//                //Headers should contain DOCTOR_ID, DOCTOR_NAME, CONSULTATION_ID, DOCTOR_TOKEN, PATIENT_TOKEN (for both audio and video) and
//                //DOCTOR_AVATAR (is it's an audio call)
//                for (String key: call.getHeaders().keySet()) {
//                    bundle.putString(key, call.getHeaders().get(key));
//                    log(key + "          " + call.getHeaders().get(key));
//                }
//            } else  {
//                bundle.putString(DOCTOR_ID, IO.getData(CallService.this, DOCTOR_ID));
//                bundle.putString(DOCTOR_NAME, IO.getData(CallService.this, DOCTOR_NAME));
//                bundle.putString(DOCTOR_TOKEN, IO.getData(CallService.this, DOCTOR_TOKEN));
//                bundle.putString(PATIENT_TOKEN, IO.getData(CallService.this, PATIENT_TOKEN));
//                bundle.putString(CONSULTATION_ID, IO.getData(CallService.this, CONSULTATION_ID));
//
//                if (!call.getDetails().isVideoOffered()) {
//                    bundle.putString(DOCTOR_AVATAR, IO.getData(CallService.this, DOCTOR_AVATAR));
//                }
//            }
//            if (!bundle.containsKey(DOCTOR_ID) || !bundle.containsKey(DOCTOR_NAME))
//                return;
//
//            //Log.d(TAG, "onIncomingCall: " + call.getCallId());
//            bundle.putString(CALL_ID, call.getCallId());
//
////            bundle.putString(CALL_TYPE,
////                    call.getDetails().isVideoOffered() ? IncomingCallActivity.VIDEO : IncomingCallActivity.VOICE);
////            Intent intent = new Intent(CallService.this, IncomingCallActivity.class);
////            intent.putExtras(bundle);
////            intent.putExtra("incoming", true);
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            startActivity(intent);
//        }
//    }
//
//    private void log(String log){
//        Log.d("CallService", "--__-_--_--_--_-_--_--_--_--" + log);
//    }
//}
