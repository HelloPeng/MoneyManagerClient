package com.pansoft.lvzp.moneymanagerclient.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.pansoft.lvzp.moneymanagerclient.base.Apl;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lv_zhp on 2018/4/1.
 */

public class OkHttpClientManager {

    private static final OkHttpClientManager INSTANCE = new OkHttpClientManager();
    private OkHttpClient mOkHttpClient;

    private OkHttpClientManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(7, TimeUnit.SECONDS);
        builder.writeTimeout(7, TimeUnit.SECONDS);
        builder.connectTimeout(7, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }

    public static OkHttpClientManager getInstance() {
        return INSTANCE;
    }

    public <T> void asyncGet(String url, final HttpResultCallback<T> callback) {
        Request request
                = new Request.Builder()
                .url(Apl.getInstance().getBaseUrl() + url)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                buildResultBean(response, callback);
            }
        });
    }

    public <T> void asyncPost(String url, Map<String, String> params, final HttpResultCallback<T> callback) {
        FormBody.Builder formBuilder = _getFormBuilder(params);
        Request request = new Request.Builder()
                .url(Apl.getInstance().getBaseUrl() + url)
                .post(formBuilder.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                buildResultBean(response, callback);
            }
        });
    }

    private <T> void buildResultBean(Response response, HttpResultCallback<T> callback) throws IOException {
        if (response.body() != null) {
            String resultJson = response.body().string();
            HttpResult<T> resultBean = JSON.parseObject(resultJson, new TypeReference<HttpResult<T>>() {
            });
            if (resultBean.getStatus() == 200)
                callback.onSuccess(resultBean.getData());
            else
                callback.onError(resultBean.getMessage());
        }
    }

    private FormBody.Builder _getFormBuilder(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> set = params.entrySet();
            for (Map.Entry<String, String> entry : set) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }

    public interface HttpResultCallback<T> {
        void onSuccess(T data);

        void onError(String msg);
    }
}
