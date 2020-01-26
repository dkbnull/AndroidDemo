package cn.wbnull.androiddemo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.File;

import butterknife.OnClick;
import cn.wbnull.androiddemo.BuildConfig;
import cn.wbnull.androiddemo.R;
import cn.wbnull.androiddemo.anno.ActivityLayoutInject;
import cn.wbnull.androiddemo.constant.CommonConstants;
import cn.wbnull.androiddemo.service.UpdateService;
import cn.wbnull.androiddemo.util.LoggerUtils;

/**
 * 检查更新界面
 *
 * @author dukunbiao(null)  2020-01-26
 * https://github.com/dkbnull/AndroidDemo
 */
@ActivityLayoutInject(R.layout.activity_update)
public class UpdateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.menuBtnCheckUpdate)
    public void onClickCheckUpdate() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setTitle("正在下载");
        dialog.setMessage("请稍后...");
        dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();

        String fileName = "AndroidDemo-v1.0.0.0.apk";
        UpdateService.download(fileName, new UpdateService.UpdateCallback() {
            @Override
            public void onSuccess() {
                dialog.dismiss();

                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }

                File file = new File(CommonConstants.DOWNLOAD_PATH + fileName);
                try {
                    LoggerUtils.getLogger().info("安装文件目录：" + file);
                    LoggerUtils.getLogger().info("准备安装");
                    installApk(file);
                } catch (Exception e) {
                    LoggerUtils.getLogger().info("获取打开方式错误", e);
                }
            }

            @Override
            public void onProgress(int progress) {
                dialog.setProgress(progress);
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
            }
        });
    }

    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri fileUri;

        //Android7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            fileUri = Uri.fromFile(file);
        }

        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
