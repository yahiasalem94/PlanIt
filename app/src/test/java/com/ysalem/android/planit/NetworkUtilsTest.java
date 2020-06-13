package com.ysalem.android.planit;

import android.util.Log;

import com.ysalem.android.planit.utils.NetworkUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class NetworkUtilsTest {


//    private Retrofit retrofit = null;
//
//    @Before
//    public void setUp() throws Exception {
//        retrofit = new retrofit2.Retrofit.Builder()
//                .baseUrl(Constants.GOOGLE_PLACES_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit).build();
//
//    }

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void buildGooglePhotoUrl_GoodCase() {
        int maxWidth = 512;
        String photoReference = "reference";
        String expectedUrl = Constants.GOOGLE_PLACES_PHOTO + "?maxwidth="+maxWidth+"&photoreference="+photoReference+"&key="+Constants.GOOGLE_API_KEY;

        String result = NetworkUtils.buildGooglePhotoUrl(maxWidth, photoReference);
        System.out.println(result);
//        assertEquals(expectedUrl,result);
    }

    @Test
    public void buildGooglePhotoUrl_ReferenceError() {
        int maxWidth = 512;
        String photoReference = "reference";
        String expectedUrl = Constants.GOOGLE_PLACES_PHOTO + "?maxwidth="+maxWidth+"&photoreference="+photoReference+"&key="+Constants.GOOGLE_API_KEY;

        String result = NetworkUtils.buildGooglePhotoUrl(maxWidth, null);
        System.out.println(result);
        assertNotEquals(expectedUrl,result);
    }
}
