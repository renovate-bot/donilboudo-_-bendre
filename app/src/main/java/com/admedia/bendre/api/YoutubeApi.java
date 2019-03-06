package com.admedia.bendre.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface YoutubeApi {
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("search")
    Call<JsonObject> getChannelVideos(@Query("part") String part,
                                      @Query("channelId") String channelId,
                                      @Query("key") String key,
                                      @Query("type") String type,
                                      @Query("order") String order,
                                      @Query("maxResults") int pageNumber);
}
