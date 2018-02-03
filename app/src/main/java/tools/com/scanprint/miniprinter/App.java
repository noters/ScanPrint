package tools.com.scanprint.miniprinter;

import android.app.Application;
import android.posapi.PosApi;
import android.util.Log;


public class App extends Application {


    private String mCurDev = "";

    static App instance = null;
    //PosSDK mSDK = null;
    PosApi mPosApi = null;

    public App() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //mDb = Database.getInstance(this);
        Log.v("App", "APP onCreate init");
        mPosApi = PosApi.getInstance(this);

    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }


    public String getCurDevice() {
        return mCurDev;
    }

    public void setCurDevice(String mCurDev) {
        this.mCurDev = mCurDev;
    }

    public PosApi getPosApi() {
        return mPosApi;
    }


}
