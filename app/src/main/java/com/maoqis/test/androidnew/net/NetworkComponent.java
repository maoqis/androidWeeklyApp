package com.maoqis.test.androidnew.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Flowable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by maoqis on 17/12/31.
 */

public class NetworkComponent {
    private static final NetworkComponent ourInstance = new NetworkComponent();
    private final WeekServiceAPI weekServiceAPI;

    public static NetworkComponent getInstance() {
        return ourInstance;
    }

    private NetworkComponent() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl("https://your.api.url/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        weekServiceAPI = retrofit.create(WeekServiceAPI.class);

    }

    public static String getUrlById(long i) {



        String url = "https://androidweekly.io/android-dev-weekly-issue-"+i;

        if (i == 210) {
            url = "https://www.androidweekly.cn/android-weekly-issue-"+i;
        }
        if (i==193){
            url = "https://www.androidweekly.cn/android-dev-wwekly-issue-"+i;
        }
        if (i == 154) {
            url = "https://www.androidweekly.cn/android-dev-wekly-issue-154/";
        }
        if (i <= 51 && i != 21) {
            url = "https://www.androidweekly.cn/android-dev-weekly-issue" + i;
        }
        return url;
    }

    public Call<ResponseBody> loadWeek(long i) {
        String url = getUrlById(i);
        Call<ResponseBody> responseBodyCall = weekServiceAPI.loadWeek(url);
        return responseBodyCall;
    }

    public Flowable<String> loadRxWeek(long i) {
        String url = getUrlById(i);
        Flowable<String> responseBodyCall = weekServiceAPI.loadRxWeek(url);
        return responseBodyCall;
    }


}
