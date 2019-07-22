package com.e.audio_sdk.View.UI.Activity;

//import android.support.v7.app.AppCompatActivity;

//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.AutoCompleteTextView;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.e.tremendocSDK.Api.API;
//import com.e.tremendocSDK.Api.StringCall;
//import com.e.tremendocSDK.Api.URLS;
//import com.e.tremendocSDK.R;
//import com.e.tremendocSDK.Service.Model.SDK_User;
//import com.e.tremendocSDK.View.Callback.FragmentChanger;
//import com.e.tremendocSDK.View.UI.Fragment.FragmentTitled;
//import com.e.tremendocSDK.View.UI.UUitil.DeviceName;
//import com.e.tremendocSDK.View.UI.UUitil.IO;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Map;
//
//public class Sign_up extends AppCompatActivity implements FragmentChanger {
//
//    private EditText firstName, lastName, Phone, Dob, Password, ConfirmPassord, username, Email;
//    private AutoCompleteTextView Gender;
//    private ImageButton revnbtn;
//    private TextView Indicator;
//    private boolean dobDialogOpen = false;
//    private SDK_User sdk_user = new SDK_User();
//    private StringCall call;
//
//    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
////        initEntry();
//    }
//
//    private boolean visible = false;
//
////    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//
//
//    public void SignUp(View view) {
//
//        String enterFirstname = firstName.getText().toString();
//        String enterLastname = lastName.getText().toString();
//        String enterPhone = Phone.getText().toString();
//        String enterEmail = Email.getText().toString();
//        String enterDob = Dob.getText().toString();
//        String enterPassword1 = Password.getText().toString();
//        String enterPassword2 = ConfirmPassord.getText().toString();
//        String enterUsername = username.getText().toString();
//        String enterGender = Gender.getText().toString();
//
//
//        if (TextUtils.isEmpty(enterFirstname)) {
//            Toast("First Name is required");
//        } else if (TextUtils.isEmpty(enterLastname)) {
//            Toast("Last Name is required");
//        } else if (TextUtils.isEmpty(enterPhone)) {
//            Toast("Please enter your  phone number ");
//        } else if (enterPhone.length() < 11 || enterPhone.length() > 11 || !Patterns.PHONE.matcher(enterPhone).matches()) {
//            Toast("Please enter a valid phone number");
//        } else if (TextUtils.isEmpty(enterEmail)) {
//            Toast("Please enter your  email address");
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(enterEmail).matches()) {
//            Toast("Invalid Email");
//        } else if (TextUtils.isEmpty(enterPassword1)) {
//            Toast("Password is required");
//
//        } else if (enterPassword1.length() < 6) {
//            Toast("Password should be a minimum of 6 characters");
//
//        } else if (!enterPassword1.equals(enterPassword2)) {
//            Toast("Passwords do not match");
//        } else if (TextUtils.isEmpty(enterUsername)) {
//            Toast("Username is required");
//        }
//
//        if (!TextUtils.isEmpty(enterFirstname) && !TextUtils.isEmpty(enterLastname) && !TextUtils.isEmpty(enterPhone) && !TextUtils.isEmpty(enterEmail)
//                && !TextUtils.isEmpty(enterPassword1) && !TextUtils.isEmpty(enterUsername) && !TextUtils.isEmpty(enterDob) && !TextUtils.isEmpty(enterGender)) {
//
//            sdk_user.setPhone(enterPhone);
//            sdk_user.setLastname(enterLastname);
//            sdk_user.setFirstname(enterFirstname);
//            sdk_user.setEmail(enterEmail);
//            sdk_user.setGender(enterGender);
//            sdk_user.setUsername(enterUsername);
//            sdk_user.setPassword(enterPassword1);
//            sdk_user.setDob(enterDob);
//
//            Map<String, String> getDetail = sdk_user.toMap();
//            getDetail.put("operatingSystem", "ANDROID");
//            getDetail.put("uuid", IO.getData(this, API.MY_UUID));
//            getDetail.put("brand", DeviceName.getDeviceName());
//
//            call.post(URLS.SDK_CREATE_USER, getDetail, response -> {
//                try {
//                    JSONObject obj = new JSONObject();
//                    if (obj.has("code") && obj.getInt("code") == 0) {
//                        Toast("Sign Up Successful");
//                        API.setCredentials(this, response);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }, error -> {
//
//            });
//
//
//        }
//
//    }
//
//    private void Toast(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//
//    }
//
//    @Override
//    public void ChangeFragment(FragmentTitled fragment) {
//
//    }
//}
