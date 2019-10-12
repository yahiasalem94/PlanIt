package com.example.android.planit.utils;

import android.content.Context;
import android.util.Log;

import com.example.android.planit.Constants;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String HEADER_PRAGMA = "Pragma";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    /* The format we want our API to return */
    private static final String format = "json";

    private static Retrofit retrofit = null;
    private static Picasso picasso = null;
    private static int cacheSize = 10 * 1024 * 1024; // 10 MB

    public static Retrofit getRetrofitInstance(Context mContext) {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.GOOGLE_PLACES_BASE_URL)
                    .client(okHttpClient(mContext))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Picasso picassoClient(Context mContext) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(retryInterceptor())
                .build();

        if (picasso == null) {
            picasso = new Picasso
                    .Builder(mContext)
                    .downloader(new OkHttp3Downloader(okHttpClient))
                    .build();

            Picasso.setSingletonInstance(picasso);
        }

        return picasso;
    }
    private static OkHttpClient okHttpClient(Context mContext) {
        return new OkHttpClient.Builder()
                .cache(cache(mContext))
                .addInterceptor(httpLoggingInterceptor())
                .addNetworkInterceptor(networkInterceptor())
                .build();
    }

    private static Cache cache(Context mContext) {
        return new Cache(new File(mContext.getCacheDir(), "PlanItCache"), cacheSize);
    }

    private static HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d(TAG, "log http: " + message);
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private static Interceptor networkInterceptor() {
        return chain -> {
            Log.d(TAG, "Network interceptor called");

            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(30, TimeUnit.DAYS)
                    .build();

            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();
        };
    }

    private static Interceptor retryInterceptor() {
        return chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);
            int tryCount = 0;
            Log.d(TAG, "message is " + response.message());

            Log.d(TAG, "Response is successful " + response.isSuccessful()+"");
            while (!response.isSuccessful() && tryCount < 3) {
                tryCount++;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                response = chain.proceed(request);
            }
            return response;
        };
    }
    public static String buildGooglePhotoUrl(int maxWidth, String photoReference) {

        String url = Constants.GOOGLE_PLACES_PHOTO + "?maxwidth="+600
                +"&maxheight"+375
                +"&photoreference="+photoReference
                +"&key="+Constants.GOOGLE_API_KEY;

        return url;
    }
}