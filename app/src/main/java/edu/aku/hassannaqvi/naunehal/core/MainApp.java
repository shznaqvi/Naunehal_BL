package edu.aku.hassannaqvi.naunehal.core;

import android.app.Application;
import android.view.View;

import com.jakewharton.threetenabp.AndroidThreeTen;

import edu.aku.hassannaqvi.naunehal.models.Child;
import edu.aku.hassannaqvi.naunehal.models.ChildInformation;
import edu.aku.hassannaqvi.naunehal.models.Form;
import edu.aku.hassannaqvi.naunehal.models.Immunization;
import edu.aku.hassannaqvi.naunehal.models.Users;

public class MainApp extends Application {

    public static final String PROJECT_NAME = "Naunehal Baseline Survey";
    public static final String DIST_ID = null;
    public static final String SYNC_LOGIN = "sync_login";
    public static final String _IP = "https://vcoe1.aku.edu";// .LIVE server
//    public static final String _IP = "http://f38158";// .TEST server
    //public static final String _IP = "http://43.245.131.159:8080";// .TEST server
    public static final String _HOST_URL = MainApp._IP + "/naunehal/api/";// .TEST server;
    public static final String _SERVER_URL = "sync.php";
    public static final String _SERVER_GET_URL = "getData.php";
    public static final String _PHOTO_UPLOAD_URL = MainApp._IP + _HOST_URL + "uploads.php";
    public static final String _UPDATE_URL = MainApp._IP + "/naunehal/app/";
    public static Form form;
    public static Child child;
    public static Immunization immunization;
    public static ChildInformation childInfo;
    public static String DeviceURL = "devices.php";
    public static AppInfo appInfo;
    public static Users user;
    public static Boolean admin = false;

    public static void hideSystemUI(View decorView) {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Initiate DateTime
        AndroidThreeTen.init(this);
        //Initializ App info
        appInfo = new AppInfo(this);
    }
}
