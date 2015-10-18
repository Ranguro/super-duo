package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Randall on 29/09/2015.
 */
public final class Utils {

    private Utils() {
    }

    //Based on stackoverflow snippet
    public static boolean isNetworkAvailable(Activity activity) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}

