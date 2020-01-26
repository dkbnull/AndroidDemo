package cn.wbnull.androiddemo.boot;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cn.wbnull.androiddemo.BuildConfig;
import cn.wbnull.androiddemo.constant.CommonConstants;
import cn.wbnull.androiddemo.util.LoggerUtils;
import cn.wbnull.androiddemo.util.ThreadFactoryUtils;
import cn.wbnull.helloutil.util.DateUtils;

/**
 * UncaughtException 处理类
 * 当程序发生UncaughtException 时，使用该类接管程序，并记录错误报告
 *
 * @author dukunbiao(null)  2020-01-26
 * https://github.com/dkbnull/AndroidDemo
 */
public class GlobalCrashHandler implements UncaughtExceptionHandler {

    private Context context;
    private volatile static GlobalCrashHandler globalCrashHandler;

    private UncaughtExceptionHandler uncaughtExceptionHandler;

    private Map<String, String> crashInfo = new HashMap<>();

    private GlobalCrashHandler() {
    }

    public static GlobalCrashHandler getGlobalCrashHandler() {
        if (globalCrashHandler == null) {
            synchronized (GlobalCrashHandler.class) {
                if (globalCrashHandler == null) {
                    globalCrashHandler = new GlobalCrashHandler();
                }
            }
        }

        return globalCrashHandler;
    }

    public void init(Context context) {
        this.context = context;

        // 获取系统默认的 UncaughtException 处理器
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置该 GlobalCrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!exceptionHandler(ex) && uncaughtExceptionHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            uncaughtExceptionHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LoggerUtils.error("", e);
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean exceptionHandler(Throwable e) {
        if (e == null) {
            return false;
        }

        if (BuildConfig.DEBUG) {
            LoggerUtils.error("", e);
        }

        ThreadFactoryUtils.getExecutorService("exception-handler-%d").execute(
                () -> {
                    Looper.prepare();
                    Toast.makeText(context, "很抱歉，程序出现异常，即将退出。", Toast.LENGTH_LONG).show();
                    Looper.loop();
                });

        collectDeviceInfo(context);
        saveCrashInfo2File(e);
        return true;
    }

    public void collectDeviceInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);

            if (packageInfo != null) {
                String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String versionCode = packageInfo.versionCode + "";
                crashInfo.put("versionName", versionName);
                crashInfo.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            LoggerUtils.error("collect package info exception", e);
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                crashInfo.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                LoggerUtils.error("collect crash info exception", e);
            }
        }
    }

    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : crashInfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);
        try {
            String fileName = "crash_" + DateUtils.dateFormat("yyyyMMddHHmmss") + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(CommonConstants.CRASH_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(CommonConstants.CRASH_PATH + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }

            return fileName;
        } catch (Exception e) {
            LoggerUtils.error("writing file exception", e);
        }
        return null;
    }
}