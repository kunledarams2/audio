package com.e.audio_sdk.View.UI.Fragment.Signup;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.e.audio_sdk.R;
import com.e.audio_sdk.Model.SDK_User;
import com.e.audio_sdk.View.Callback.ModelSaver;
import com.e.audio_sdk.View.UI.Activity.Signup;
import com.e.audio_sdk.View.UI.Fragment.FragmentTitled;
import com.e.audio_sdk.View.UI.UUitil.IO;

import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 */
public class Steptwo extends FragmentTitled implements View.OnClickListener {

    private EditText firstname, lastname, phone, email, dob;
    private AutoCompleteTextView gender;
    private boolean dobDialogOpen = false;
    private Button signUp;
    private ModelSaver modelSaver;

    private static final String FIRSTNAME = "signup_firstname";
    private static final String LASTNAME = "signup_lastname";
    private static final String PHONE = "signup_phone";
    private static final String EMAIL = "signup_email";
    private static final String DOB = "signup_dob";
    private static final String GENDER = "signup_gender";


    public Steptwo() {
        // Required empty public constructor
    }

    public static Steptwo newInstance() {
        Steptwo fragment = new Steptwo();
        fragment.setTitle(Signup.STEP_TWO);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_steptwo, container, false);
        setView(view);
        modelSaver = (Signup) getActivity();
        return view;
    }

    private void setView(View view) {

        firstname = view.findViewById(R.id.firstname);
        lastname = view.findViewById(R.id.lastname);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);

        dob = view.findViewById(R.id.dob);
        dob.setShowSoftInputOnFocus(false);
        dob.setOnFocusChangeListener((v, b) -> {
            if (b && !dobDialogOpen) {
                openDatepicker();
            }
        });

        dob.setOnClickListener(view1 -> {
            if (!dobDialogOpen) {
                openDatepicker();
            }

        });


        gender = view.findViewById(R.id.gender);
        gender.setShowSoftInputOnFocus(false);
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, genders);
        gender.setAdapter(adapter);

        gender.setOnFocusChangeListener((v, b) -> {
            if (b) {
                gender.showDropDown();
            }
        });
        gender.setOnClickListener(view1 -> {
            gender.showDropDown();
        });


        signUp = view.findViewById(R.id.signup_btn);
        signUp.setOnClickListener(this);
    }


    public void onButtonPressed(Uri uri) {

    }


    private void openDatepicker() {
        Calendar now = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog, (datePicker, year, month, day) -> {
            String strDay = String.valueOf(day).length() == 1 ? "0" + day : day + "";
            month = month + 1;
            String strMonth = String.valueOf(month).length() == 1 ? "0" + month : month + "";
            String date = year + "-" + strMonth + "-" + strDay;
            dob.setText(date);
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setOnCancelListener(d -> dobDialogOpen = false);
        dialog.setOnDismissListener(d -> dobDialogOpen = false);
        dialog.show();
        dobDialogOpen = true;
    }

    @Override
    public void onClick(View view) {
        if (view == signUp) {
            processSignup();
        }
    }

    private void processSignup() {

        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String memail = email.getText().toString();
        String mphone = phone.getText().toString();
        String mdob = dob.getText().toString();
        String mgender = gender.getText().toString();


        if (TextUtils.isEmpty(fname)) {
            toastMessage("First Name is required");
        } else if (TextUtils.isEmpty(lname)) {
            toastMessage("Last Name is required");
        } else if (TextUtils.isEmpty(mphone)) {
            toastMessage("Please enter your  phone number ");
        } else if (mphone.length() < 11 || mphone.length() > 11 || !Patterns.PHONE.matcher(mphone).matches()) {
            toastMessage("Please enter a valid phone number");
        } else if (TextUtils.isEmpty(memail)) {
            toastMessage("Please enter your  email address");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(memail).matches()) {
            toastMessage("Invalid Email");
        }

        if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname)
                && !TextUtils.isEmpty(mphone) && !TextUtils.isEmpty(memail)
                && !TextUtils.isEmpty(mdob) && Patterns.PHONE.matcher(mphone).matches() && Patterns.EMAIL_ADDRESS.matcher(memail).matches()) {

            IO.setData(getContext(), FIRSTNAME, fname);
            IO.setData(getContext(), LASTNAME, lname);
            IO.setData(getContext(), PHONE, mphone);
            IO.setData(getContext(), EMAIL, memail);
            IO.setData(getContext(), DOB, mdob);


            SDK_User sdkUser = new SDK_User();
            sdkUser.setFirstname(IO.getData(getContext(), Steptwo.FIRSTNAME));
            sdkUser.setUsername(IO.getData(getContext(), Stepone.USERNAME));
            sdkUser.setPassword(IO.getData(getContext(), Stepone.PASSWORD));
            sdkUser.setEmail(memail);
            sdkUser.setPhone(mphone);
            sdkUser.setLastname(lname);
            sdkUser.setDob(mdob);
            sdkUser.setGender(mgender.substring(0, 1));

            modelSaver.onSave(sdkUser);

        }

    }

    private void toastMessage(String Msg) {
        Toast.makeText(getContext(), Msg, Toast.LENGTH_LONG).show();
    }


}
