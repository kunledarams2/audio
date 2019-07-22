package com.e.audio_sdk.View.UI.Activity;

//import android.support.v7.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.e.audio_sdk.Api.API;
import com.e.audio_sdk.Api.StringCall;
import com.e.audio_sdk.Api.URLS;
import com.e.audio_sdk.R;
import com.e.audio_sdk.Model.SDK_User;
import com.e.audio_sdk.View.Callback.FragmentChanger;
import com.e.audio_sdk.View.Callback.ModelSaver;
import com.e.audio_sdk.View.UI.Fragment.FragmentTitled;
import com.e.audio_sdk.View.UI.Fragment.Signup.Stepone;
import com.e.audio_sdk.View.UI.Fragment.Signup.Steptwo;
import com.e.audio_sdk.View.UI.UUitil.Checker;
import com.e.audio_sdk.View.UI.UUitil.DeviceName;
import com.e.audio_sdk.View.UI.UUitil.IO;
import com.e.audio_sdk.View.UI.UUitil.ToastUtili;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Signup extends AppCompatActivity implements FragmentChanger, ModelSaver<SDK_User> {

    public static final String STEP_ONE = "step_one";
    public static final String STEP_TWO = "step_two";
    private SDK_User sdk_user = new SDK_User();
    private StringCall call;
    private ProgressDialog dialog;
    private Checker checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        changeView(STEP_ONE);
        dialog = new ProgressDialog(this);
        checker=new Checker(this);
    }

    @Override
    public void ChangeFragment(FragmentTitled fragment) {
        changeView(fragment);
    }

    private void changeView(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.signupFrame, fragment);
        fragmentTransaction.commit();
    }

    private void changeView(String fragmentName) {
        Fragment fragment;
        setTitle(fragmentName);
        switch (fragmentName) {
            case STEP_ONE:

                fragment = Stepone.newInstance();
                break;
            case STEP_TWO:
                fragment = Steptwo.newInstance();
                break;
            default:
                fragment = Stepone.newInstance();
        }
        changeView(fragment);

    }

    @Override
    public void onSave(SDK_User sdk_user) {
        this.sdk_user = sdk_user;
        if (checker.isOnline()) {
            Signup();
        }

    }


    private void Signup() {

        dialog = ProgressDialog.show(this, "Sign Up", "Please Wait....", false, false);

        call = new StringCall(this);
        Map<String, String> loadData = sdk_user.toMap();
        loadData.put("operatingSystem", "ANDROID");
        loadData.put("uuid", IO.getData(this, API.MY_UUID));
        loadData.put("brand", DeviceName.getDeviceName());
        loadData.put("sdkType","chat");
        loadData.put("provider","1");

        call.post(URLS.SDK_CREATE_USER, loadData, response -> {
            try {
                dialog.dismiss();
                JSONObject obj = new JSONObject(response);
                if (obj.has("code") && obj.getInt("code") == 0) {


                    Intent intent = new Intent(this, Finddoctor.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {
                    dialog.dismiss();
                    String dec = obj.getString("description");
                    Toast.makeText(this, dec, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                ToastUtili.showModal(this, e.getMessage());

            }


        }, error -> {
            dialog.dismiss();

            if (error.networkResponse == null) {
                ToastUtili.showModal(this, "Please check your internet connection");
            }
        });
    }


}
