package com.pansoft.lvzp.moneymanagerclient.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pansoft.lvzp.moneymanagerclient.base.Apl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lv_zhp on 2018/4/1.
 */

public class OkHttpClientManager {
    private static final int TIME_OUT = 10;
    private static final OkHttpClientManager INSTANCE = new OkHttpClientManager();
    private OkHttpClient mOkHttpClient;

    private OkHttpClientManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }

    public static OkHttpClientManager getInstance() {
        return INSTANCE;
    }

    public <T> void asyncGetParams(String url, Map<String, Object> params, HttpResultCallback<T> callback) {
        url += getGetParams(params);
        asyncGet(url, callback);
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

    public <T> void asyncPostJson(String url, Object jsonBean, final HttpResultCallback<T> callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("JSON"), JSON.toJSONString(jsonBean));
        Request request = getRequest(url, requestBody);
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

    public <T> void asyncPost(String url, Map<String, Object> params, final HttpResultCallback<T> callback) {
        FormBody.Builder formBuilder = _getFormBuilder(params);
        Request request = getRequest(url, formBuilder.build());
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

    private Request getRequest(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(Apl.getInstance().getBaseUrl() + url)
                .post(requestBody)
                .build();
    }

    private <T> void buildResultBean(Response response, HttpResultCallback<T> callback) throws IOException {
        if (response.body() != null) {
            String resultJson = response.body().string();
            Gson gson = new Gson();
            HttpResult<T> resultBean = gson.fromJson(resultJson, new TypeToken<HttpResult<T>>() {
            }.getType());
            if (resultBean.getStatus() == 200)
                callback.onSuccess(resultBean.getData());
            else
                callback.onError(resultBean.getMessage());
        }
    }

    private FormBody.Builder _getFormBuilder(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> entry : set) {
                builder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return builder;
    }

    private String getGetParams(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            sb.append("?");
            Set<Map.Entry<String, Object>> set = params.entrySet();
            int index = 0;
            for (Map.Entry<String, Object> entry : set) {
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                if (index + 1 < params.size()) {
                    sb.append("&");
                }
                index++;
            }
        }
        return sb.toString();
    }

    public interface HttpResultCallback<T> {

        void onSuccess(T data);

        void onError(String msg);
    }
}
