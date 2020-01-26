package cn.wbnull.androiddemo.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import cn.wbnull.androiddemo.anno.ActivityLayoutInject;
import cn.wbnull.androiddemo.anno.ActivityViewInject;

/**
 * Activity 基类
 *
 * @author dukunbiao(null)  2020-01-26
 * https://github.com/dkbnull/AndroidDemo
 */
public class BaseActivity extends AppCompatActivity {

    private int layoutId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayInjectLayout();
        displayInjectView();

        ButterKnife.bind(this);
    }

    private void displayInjectLayout() {
        Class<?> clazz = this.getClass();
        if (clazz.isAnnotationPresent(ActivityLayoutInject.class)) {
            ActivityLayoutInject inject = clazz.getAnnotation(ActivityLayoutInject.class);

            if (inject != null) {
                layoutId = inject.value();
                setContentView(layoutId);
            }
        }
    }

    private void displayInjectView() {
        if (layoutId <= 0) {
            return;
        }

        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(ActivityViewInject.class)) {
                    field.setAccessible(true);
                    ActivityViewInject inject = field.getAnnotation(ActivityViewInject.class);
                    field.set(this, this.findViewById(inject.value()));
                }
            }
        } catch (IllegalAccessException e) {
        }
    }

    protected String getVersion() {
        String version = "";

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);

            if (packageInfo != null) {
                version = packageInfo.versionName == null ? "" : packageInfo.versionName + "." + packageInfo.versionCode;
            }
        } catch (Exception e) {

        }

        return version;
    }
}
