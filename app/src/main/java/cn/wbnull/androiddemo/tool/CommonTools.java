package cn.wbnull.androiddemo.tool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cn.wbnull.androiddemo.App;

/**
 * 通用工具类
 *
 * @author dukunbiao(null)  2020-01-26
 * https://github.com/dkbnull/AndroidDemo
 */
public class CommonTools {

    private static Toast toast = null;

    /**
     * 短时间显示 Toast
     *
     * @param context
     * @param message
     */
    public static void showToastShort(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showToastShort(String message) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示 Toast
     *
     * @param context
     * @param message
     */
    public static void showToastLong(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showToastLong(String message) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 启动新 Activity
     *
     * @param context
     * @param clazz
     */
    public static void startNewActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    public static void startNewActivity(Class clazz) {
        Intent intent = new Intent(App.getContext(), clazz);
        App.getContext().startActivity(intent);
    }

    public static void startNewActivityForResult(Class clazz, int requestCode) {
        App.getContext().startActivityForResult(new Intent(App.getContext(), clazz), requestCode);
    }

    public static void startNewActivityForResult(Class clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(App.getContext(), clazz);
        intent.putExtras(bundle);
        App.getContext().startActivityForResult(intent, requestCode);
    }

    /**
     * 提示信息弹窗
     *
     * @param context
     * @param message
     */
    public static void showInfoDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    public static void showInfoDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(App.getContext());
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    /**
     * 提示信息弹窗
     *
     * @param context
     * @param title
     * @param message
     * @param clickListener
     */
    public static void showInfoDialog(Context context, String title, String message, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", clickListener);
        builder.show();
    }

    public static void showInfoDialog(String title, String message, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(App.getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", clickListener);
        builder.show();
    }

    /**
     * 确认操作弹窗
     *
     * @param context
     * @param title
     * @param message
     * @param positiveClickListener
     * @param negativeClickListener
     */
    public static void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", positiveClickListener);
        builder.setNegativeButton("取消", negativeClickListener);
        builder.show();
    }

    public static void showConfirmDialog(String title, String message, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(App.getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", positiveClickListener);
        builder.setNegativeButton("取消", negativeClickListener);
        builder.show();
    }

    public static void showConfirmDialog(String message, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(App.getContext());
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("确定", positiveClickListener);
        builder.setNegativeButton("取消", negativeClickListener);
        builder.show();
    }

    public static void showConfirmDialog(String message, DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(App.getContext());
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("确定", positiveClickListener);
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
