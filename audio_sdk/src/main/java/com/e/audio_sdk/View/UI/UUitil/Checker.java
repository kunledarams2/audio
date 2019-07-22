package com.e.audio_sdk.View.UI.UUitil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Checker {
private Context context;

    public Checker(Context context) {
        this.context = context;
    }

    public  boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
