package cn.wbnull.androiddemo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.wbnull.androiddemo.constant.CommonConstants;
import cn.wbnull.androiddemo.util.LoggerUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 检查更新服务
 *
 * @author dukunbiao(null)  2020-01-26
 * https://github.com/dkbnull/AndroidDemo
 */
public class UpdateService {

    private static OkHttpClient okHttpClient;

    public static void download(final String fileName, final UpdateCallback callback) {
        String url = "http://127.0.0.1:8090/springbootdemo/log/download/" + fileName;
        Request request = new Request.Builder()
                .addHeader("Accept-Encoding", "identity")
                .url(url).build();

        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() == null) {
                    callback.onFailure();
                    return;
                }

                File filePath = new File(CommonConstants.DOWNLOAD_PATH);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }

                long contentLength = response.body().contentLength();
                byte[] buffer = new byte[1024];
                File file = new File(filePath.getCanonicalPath(), fileName);
                try (InputStream is = response.body().byteStream();
                     FileOutputStream fos = new FileOutputStream(file)) {
                    LoggerUtils.getLogger().info("保存路径：" + file);

                    int length;
                    long sum = 0;
                    while ((length = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, length);
                        sum += length;
                        int progress = (int) (sum * 1.0f / contentLength * 100);
                        callback.onProgress(progress);
                    }
                    fos.flush();

                    callback.onSuccess();
                } catch (Exception e) {
                    callback.onFailure();
                }
            }
        });
    }

    public interface UpdateCallback {

        void onSuccess();

        void onProgress(int progress);

        void onFailure();
    }
}
