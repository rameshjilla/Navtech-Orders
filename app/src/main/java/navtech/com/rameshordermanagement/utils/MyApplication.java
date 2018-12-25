package navtech.com.rameshordermanagement.utils;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by admin on 22/12/2018.
 */

public class MyApplication extends Application {
    MyApplication myApplication;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        getInstance();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name(Realm.DEFAULT_REALM_NAME).schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(config);

    }


    public static MyApplication getInstance() {
        if (instance == null) {
            synchronized (MyApplication.class) {
                if (instance == null)
                    instance = new MyApplication();
            }
        }
        // Return the instance
        return instance;
    }
}
