package com.example.android.planit.Utils;

import com.example.android.planit.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /* The format we want our API to return */
    private static final String format = "json";

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.GOOGLE_PLACES_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String buildGooglePhotoUrl(int maxWidth, String photoReference) {
        String url = Constants.GOOGLE_PLACES_PHOTO + "?maxwidth="+maxWidth
                +"&photoreference="+photoReference
                +"&key="+Constants.GOOGLE_API_KEY;

        return url;
    }
}