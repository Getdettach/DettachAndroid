package com.wwc.jajing.system;

import android.content.Context;
import android.util.Log;
import android.content.res.Configuration;
import com.orm.SugarApp;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.wwc.jajing.domain.entity.CallerImpl;
import com.wwc.jajing.domain.entity.UserImpl;
import com.wwc.jajing.permissions.CallerPermission;


import io.realm.Realm;
import io.realm.RealmConfiguration;

public class JJApplication extends SugarApp {

    private static JJApplication jjApplication;
    private static RealmConfiguration realmConfiguration;
    private static SugarApp sugarContext;
    //  SugarApp sugarContext;

    SugarDb database;

    public JJApplication() {
        super();

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //try
        //{
            Log.d("Application", "App initialization - entered!");
            jjApplication = this;
            Log.d("Application", "After jjApplication");
            sugarContext = this;
            Log.d("Application", "After sugarContext");
            this.database = new SugarDb(this);
            Log.d("Application", "After this.database");
            realmConfiguration = new RealmConfiguration.Builder(jjApplication).deleteRealmIfMigrationNeeded().build();
            Log.d("Application", "After realmConfiguration");
            initSystem(jjApplication);
            Log.d("Application", "After initSystem");
            UserImpl.findById(UserImpl.class, (long) 1);
            Log.d("Application", "After UserImpl");
        //}catch(Exception e)
        //{
        //    e.printStackTrace();
        //}


//        CallerImpl.findById(CallerImpl.class,(long)1);

    }


    public void onTerminate() {
        super.onTerminate();
    }

    public static SugarApp getSugarContext() {
        return sugarContext;
    }

    protected SugarDb getDatabase() {
        return database;
    }

    public static Realm getRealmInstance() {

        return Realm.getInstance(realmConfiguration);
    }

    private void initSystem(Context context) {
        if (!JJSystemImpl.hasBeenIntialized()) {
            JJSystemImpl.getInstance().initSystem(this.getApplicationContext());
            Log.d("Application", "App was initialized!");

        } else {
            Log.d("Application", "App was not initialized!");

        }
    }
}
