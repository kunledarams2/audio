package com.e.audio_sdk.Api;

import com.e.audio_sdk.BuildConfig;

public class URLS {

//    private static final String IP = BuildConfig.DEBUG ?  "188.166.14.154" : "178.128.8.26";
    private static final String IP = BuildConfig.DEBUG ?  "188.166.14.154" : "188.166.14.154";
//    private static final String IP= BuildConfig.DEBUG  ? "192.168.88.26" : "192.168.88.26";  // localhost ip address
//     private static final String IP= BuildConfig.DEBUG  ? "192.168.43.204" : "192.168.43.204";
//    private static  final String IP = BuildConfig.DEBUG ? "localhost" : "localhost";

    public static  final String SERVER = "http://" + IP + ":9000/tremendoc/api/";



    public static String SPECIALTY_DOCTORS = SERVER + "doctor/search/speciality/";
    public static String DOCTORS_SEARCH = SERVER + "doctor/search/name/";
    public static String DOCTORS_RANDOM = SERVER + "doctor/search/random";

    public static String SDK_AUTHENICATION =SERVER +"sdk-provider/authenticate";
    public static String SDK_CREATE_USER= SERVER + "sdk-provider/create";

    public static String DOCTOR_AVAILABLE = SERVER + "doctor/available/";
    public static String INITIATE_CONSULTATION = SERVER + "consultation/initiate";
    public static String UPDATE_CONSULTATION = SERVER + "consultation/update";

}
