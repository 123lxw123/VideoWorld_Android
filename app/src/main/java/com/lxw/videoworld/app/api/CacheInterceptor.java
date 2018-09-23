package com.lxw.videoworld.app.api;

import android.text.TextUtils;
import android.util.Log;

import com.lxw.videoworld.framework.application.BaseApplication;
import com.lxw.videoworld.framework.http.CacheManager;
import com.lxw.videoworld.framework.util.NetUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Zion on 2017/11/10.
 */

public class CacheInterceptor implements Interceptor {

    public static final String SPLIT = "SYSTEM_CURRENT_TIME_MILLIS";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        RequestBody requestBody = request.body();
        Charset charset = Charset.forName("UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (request.method().equals("POST")) {
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            Buffer buffer = new Buffer();
            try {
                requestBody.writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(buffer.readString(charset));
            buffer.close();
        }
        String key = sb.toString();
        String cache = CacheManager.getInstance().getCache(key);
        if (TextUtils.isEmpty(cache) || url.contains("addAdmire")) {// 没缓存
            return getResponseAndSaveCache(key, cache, chain, request);
        } else if (NetUtil.isNetworkAvailable(BaseApplication.getAppContext())) {
            String[] splits = cache.split(SPLIT);
            if (System.currentTimeMillis() - Long.valueOf(splits[1]) > 1000 * 60 * 15) {// 缓存过期了
                return getResponseAndSaveCache(key, cache, chain, request);
            } else {// 返回缓存
                return new Response.Builder()
                        .code(200)
                        .message("SUCCESS")
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create(MediaType.parse("application/json"), splits[0]))
                        .addHeader("content-type", "application/json")
                        .build();
            }
        } else {
            String[] splits = cache.split(SPLIT);
            return new Response.Builder()
                    .code(200)
                    .message("SUCCESS")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), splits[0]))
                    .addHeader("content-type", "application/json")
                    .build();
        }
    }

    public Response getResponseAndSaveCache(String key, String cache, Chain chain, Request request) throws IOException {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 服务器返回正确结果才缓存
        if (response != null && response.code() == 200) {
            ResponseBody responseBody = response.body();
            MediaType contentType = responseBody.contentType();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = Charset.forName("UTF-8");
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            String newCache = buffer.clone().readString(charset) + SPLIT + System.currentTimeMillis();
            CacheManager.getInstance().putCache(key, newCache);
            Log.d(CacheManager.TAG, "put cache-> key:" + key + "-> cache:" + cache);
        } else if (!TextUtils.isEmpty(cache)) {
            String[] splits = cache.split(SPLIT);
            response = new Response.Builder()
                    .code(200)
                    .message("SUCCESS")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), splits[0]))
                    .addHeader("content-type", "application/json")
                    .build();
        }
        return response;
    }
}
