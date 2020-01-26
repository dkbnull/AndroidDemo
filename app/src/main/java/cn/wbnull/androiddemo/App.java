package cn.wbnull.androiddemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.HashSet;
import java.util.Set;

import cn.wbnull.androiddemo.boot.GlobalCrashHandler;

/**
 * 应用初始化
 *
 * @author dukunbiao(null)  2020-01-26
 * https://github.com/dkbnull/AndroidDemo
 */
public class App extends Application {

    private Activity currentActivity;
    private Set<Activity> allActivities = new HashSet<>();
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initActivityLifecycleCallbacks();

        GlobalCrashHandler crashHandler = GlobalCrashHandler.getGlobalCrashHandler();
        crashHandler.init(getApplicationContext());
    }

    private void initActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                allActivities.add(activity);
                setCurrentActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity.equals(currentActivity)) {
                    setCurrentActivity(null);
                }
                allActivities.remove(activity);
            }
        });
    }

    public static App getInstance() {
        return instance;
    }

    private void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public static Activity getContext() {
        return getInstance().getCurrentActivity();
    }

    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }

    public void exit() {
        for (Activity activity : allActivities) {
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
