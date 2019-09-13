package com.maoqis.test.androidnew.net;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by maoqis on 17/12/31.
 */

public interface WeekServiceAPI {
    @GET
    public Call<ResponseBody> loadWeek(@Url String url);
    @GET
    public Flowable<String> loadRxWeek(@Url String url);
}
