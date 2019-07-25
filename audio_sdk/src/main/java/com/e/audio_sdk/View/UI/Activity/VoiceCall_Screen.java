package com.e.audio_sdk.View.UI.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.e.audio_sdk.R;
import com.e.audio_sdk.View.UI.UUitil.AudioPlayer;
import com.e.audio_sdk.View.UI.UUitil.ToastUtili;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.e.audio_sdk.View.UI.UUitil.CallContants.CALL_ID;
import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_AVATAR;
import static com.e.audio_sdk.View.UI.UUitil.CallContants.DOCTOR_NAME;

public class VoiceCall_Screen extends BaseActivity {


    private Bundle bundle;
    private final static String TAG_LOG = VoiceCall_Screen.class.getSimpleName();
    private final String ADDED_LISTENER = "addlistener";
    private final String CALL_START_TIME = "callstarttime";


    private String mCallId;
    private String mDoctorName, mDoctorImage, mDoctorToken;
    private boolean mAddedListener = false;
    private long mCallStart = 0;
    private boolean isOnCall = false;
    private boolean isAnsRateDoc =false;
    private int rating = 0;

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTrack mTimeCallTrack;


    private Button speakerBtn, muteBtn, endCallbtn;
    private CircleImageView doctorImage;
    private TextView doctorName,  connectionStatue,callDuration;


    public class UpdateCallDurationTrack extends TimerTask {

        @Override
        public void run() {
            VoiceCall_Screen.this.runOnUiThread(VoiceCall_Screen.this::updateCallDuration);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putBoolean(ADDED_LISTENER, mAddedListener);
        saveInstanceState.putLong(CALL_START_TIME, mCallStart);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
        mCallStart = savedInstanceState.getLong(CALL_START_TIME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);

        mAudioPlayer = new AudioPlayer(this);

        doctorImage = findViewById(R.id.avatar);
        doctorName = findViewById(R.id.doctor_name);
        callDuration = findViewById(R.id.timer);
        connectionStatue = findViewById(R.id.call_status);
        speakerBtn = findViewById(R.id.speaker_btn);
        muteBtn = findViewById(R.id.mute_btn);
        endCallbtn = findViewById(R.id.end_btn);


        bundle = getIntent().getExtras();
        mDoctorName = bundle.getString(DOCTOR_NAME);
        mDoctorImage = bundle.getString(DOCTOR_AVATAR);


        mCallId = bundle.getString(CALL_ID);
        OnCall(mCallId);


    }

    private boolean OnCall( String getCallId){
        if(getCallId !=null){

            Picasso.get().load(mDoctorImage).into(doctorImage);
            doctorName.setText(mDoctorName);
            endCallbtn.setOnClickListener(btn->{ endCall(true); });
            speakerBtn.setOnClickListener(btn->{
                toggleSpeaker();
            });
            muteBtn.setOnClickListener(btn->{toggleMute();});
            isOnCall=true;
            return true;
        }
        else {
            return false;
        }

    }

    private String formatTimespan(int totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private  void updateCallDuration(){

        Call call= getSinchServiceInterface().getCall(mCallId);
        if(call !=null){
            callDuration.setText(formatTimespan(call.getDetails().getDuration()));
        }
    }

    @Override
    protected void onServiceConnected(IBinder iBinder) {
        super.onServiceConnected(iBinder);

        if(mCallId !=null){
            Call call = getSinchServiceInterface().getCall(mCallId);
            if(call !=null){
                call.addCallListener(new SinchCallListener());
                mAddedListener=true;
            }
            else {
                Log.d(TAG_LOG, "Start with invalid callid abort call");
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isOnCall){
            stopTimer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isOnCall){
            startTimer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isOnCall){
            stopTimer();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isOnCall){
            startTimer();
        }
    }

    private void startTimer(){
        mTimer= new Timer();
        mTimeCallTrack = new UpdateCallDurationTrack();
        mTimer.schedule(mTimeCallTrack,0,500);
    }

    private void stopTimer(){
        mTimeCallTrack.cancel();
        mTimer.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // caller is not allow to press back buttom but have to end call

    }

    public  class SinchCallListener implements CallListener{

        @Override
        public void onCallProgressing(Call call) {
            log("Call on progress");
            connectionStatue.setText("Ringing...");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onCallEstablished(Call call) {
            log("Call is established");
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController= getSinchServiceInterface().getAudioController();
            audioController.enableSpeaker();
            mCallStart=System.currentTimeMillis();
            connectionStatue.setText("Connected");

        }

        @Override
        public void onCallEnded(Call call) {
            CallEndCause endCause = call.getDetails().getEndCause();
            Log.d(TAG_LOG,"Call ended Reason" + endCause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String causeMsg= " Call Ended " + call.getDetails().toString();
            log(causeMsg);
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            //this can be manager by setManagerSupport in the Sinch client e.g GCM
        }

    }

    private void endCall(boolean closeScreen){
        Call call= getSinchServiceInterface().getCall(mCallId);
        onEndCall(call,closeScreen);
    }

    private void onEndCall(Call call , boolean closeScreen){
        mAudioPlayer.stopProgressTone();
        if(call !=null){
            call.hangup();
//            closeScreen();
        }
        if(closeScreen){
           if(isAnsRateDoc){
               ratingModel(call);
           }
           else closeScreen();
        }
    }
    private void closeScreen(){
        Intent intent = new Intent(this, Finddoctor.class);
        startActivity(intent);
        finish();
    }
//    private void showCallbackModal(Call call) {
//        int remaingTime = TEN_MINUTES - call.getDetails().getDuration();
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Consultation")
//                .setMessage("You still have " + formatTimespan(remaingTime) + " " +
//                        "of consultation time, will you like to continue the consultation?")
//                .setPositiveButton("Continue", (dialog, i) -> {
//                    callback();
//                    dialog.cancel();
//                })
//                .setNegativeButton("No", (dialog, i) -> {
//                    dialog.dismiss();
//
//                    if (isAnsRateDoc)
//                        ratingModal(call);
//                    else {
//                        getSinchServiceInterface().updateConsultation(call);
//                        closeScreen();
//                    }
//                });
//
//        builder.create().show();
//    }

    private void ratingModel(Call call){
        DialogInterface.OnClickListener onClickPositv =(dialogInterface, i) -> {
            getSinchServiceInterface().updateConsultation(call,rating);
            dialogInterface.dismiss();
            closeScreen();

        };

        DialogInterface.OnClickListener onClickNegitv= (dialogInterface , i)-> {
            getSinchServiceInterface().updateConsultation(call);
            dialogInterface.dismiss();
            closeScreen();
        };

        ratingModal(onClickPositv,onClickNegitv);

    }


    private void ratingModal(DialogInterface.OnClickListener onPosClick, DialogInterface.OnClickListener onNegClick) {
        int[] stars = {R.id.star_1, R.id.star_2, R.id.star_3, R.id.star_4, R.id.star_5};
        ImageButton[] starBtns = new ImageButton[5];


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null, false);
        TextView textView = view.findViewById(R.id.rating_result);

        for (int i = 0; i < stars.length; i++) {
            starBtns[i] = view.findViewById(stars[i]);
            final int index = i;
            starBtns[i].setOnClickListener(btn -> {
                rating = index + 1;
                for (int j = 0; j < stars.length; j++) {
                    if (j <= index) {
                        starBtns[j].setImageResource(R.drawable.ic_star);
                    } else {
                        starBtns[j].setImageResource(R.drawable.ic_star_empty);
                    }
                }

                switch (rating) {
                    case 1 :
                        textView.setText("Very poor");
                        textView.setTextColor(Color.parseColor("#dd5555"));
                        break;
                    case 2:
                        textView.setText("Poor");
                        textView.setTextColor(Color.parseColor("#dddd55"));
                        break;
                    case 3:
                        textView.setText("Average");
                        textView.setTextColor(Color.parseColor("#555555"));
                        break;
                    case 4:
                        textView.setText("Good");
                        textView.setTextColor(Color.parseColor("#55dddd"));
                        break;
                    case 5:
                        textView.setText("Excellent");
                        textView.setTextColor(Color.parseColor("#55dd99"));
                        break;
                }
            });
        }

        builder.setView(view)
                .setPositiveButton("Ok", (dialog, i) -> {
                    if (rating > 0)
                        onPosClick.onClick(dialog, i);
                    else
                    ToastUtili.showModal(this,"Please rate the consultation");
                })
                .setNegativeButton("Cancel", (dialog, i) -> {
                    onNegClick.onClick(dialog, i);
                })
                .create()
                .show();
    }

    private void  toggleSpeaker(){
        if(mAudioPlayer.isOnSpeaker()){
           getSinchServiceInterface().getAudioController().disableSpeaker();
           speakerBtn.setTextColor(getResources().getColor(R.color.colorGray));
           speakerBtn.setText("Normal");
           speakerBtn.setCompoundDrawables(null,getDrawable(R.drawable.ic_volume_down_gray),null,null);

        }
        else {
            getSinchServiceInterface().getAudioController().enableSpeaker();
            speakerBtn.setTextColor(getResources().getColor(R.color.colorWhite));
            speakerBtn.setText("Speaker");
            speakerBtn.setCompoundDrawables(null,getDrawable(R.drawable.ic_volume_up_white),null,null);
        }
    }

    private void toggleMute(){
        if(mAudioPlayer.isMute()){
            getSinchServiceInterface().getAudioController().unmute();
            muteBtn.setTextColor(getResources().getColor(R.color.colorWhite));
            muteBtn.setText("Unmute");
            muteBtn.setCompoundDrawables(null, getDrawable(R.drawable.ic_mic_white),null,null);

        }
        else {
            getSinchServiceInterface().getAudioController().mute();
            muteBtn.setText("Mute");
            muteBtn.setCompoundDrawables(null,getDrawable(R.drawable.ic_mic_off_gray),null,null);
            muteBtn.setTextColor(getResources().getColor(R.color.colorGray));
        }
    }

    private void log(String msg) {
        Log.d("VoiceCall_Screen", "_--___----___---___---___----__---" + msg);
    }
}
