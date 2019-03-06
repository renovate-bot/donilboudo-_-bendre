package com.admedia.bendre.api;

import com.admedia.bendre.model.Post;
import com.admedia.bendre.model.media.Media;
import com.admedia.bendre.model.user.RegistrationUser;
import com.admedia.bendre.model.woocommerce.Order;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WordPressService {
    @GET("posts")
    Call<JsonArray> getPosts(@Query(value = "categories", encoded = true) String categories);


    @GET("posts")
    Call<JsonArray> getPosts(@Header("Authorization") String authorization,
                             @Query("_embed") boolean includeEmbed,
                             @Query(value = "categories", encoded = true) String categories,
                             @Query(value = "status", encoded = true) String status,
                             @Query("orderby") String orderBy,
                             @Query("order") String order,
                             @Query("per_page") int per_page);

    @GET("posts")
    Call<JsonArray> getPostsByAuthor(@Header("Authorization") String authorization,
                                     @Query("_embed") boolean includeEmbed,
                                     @Query(value = "status", encoded = true) String status,
                                     @Query("page") int pageNumber,
                                     @Query("author") int authorId);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("posts")
    Call<Post> savePost(@Header("Authorization") String authorization, @Body JsonObject post);

    @GET("posts/{post_id}")
    Single<Post> getPost(@Path("post_id") int postId);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @DELETE("posts/{post_id}")
    Call<Object> deletePost(@Header("Authorization") String authorization, @Path("post_id") int postId);

//    @GET("media/{media_id}")
//    Call<JsonElement> getMedia(@Path("media_id") int mediaId);

    @GET("media/{media_id}")
    Call<Media> getMedia(@Path("media_id") int mediaId);

    @GET("users/{user_id}")
    Observable<JsonObject> getUser(@Path("user_id") int userId);

    @POST("jwt-auth/v1/token")
    @FormUrlEncoded
    Call<Object> login(@Field("username") String username, @Field("password") String password);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("users/me")
    Call<LinkedTreeMap> getMe(@Header("Authorization") String authorization);

    @GET("posts")
    Call<JsonArray> searchPosts(@Header("Authorization") String authorization,
                                @Query("_embed") boolean includeEmbed,
                                @Query("status") String status,
                                @Query("page") int pageNumber,
                                @Query("search") String query);

//    @Headers({"Content-Type: application/json;charset=UTF-8"})
//    @GET("products")
//    Call<JsonArray> getProducts(
//            @Header("Authorization") String authorization,
//            @Query("orderby") String orderBy,
//            @Query("order") String order,
//            @Query("per_page") int per_page);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("products")
    Call<JsonArray> getProducts(@Query("consumer_key") String username,
                                @Query("consumer_secret") String password,
                                @Query("orderby") String orderBy,
                                @Query("order") String order,
                                @Query("page") int pageNumber);

    @POST("/orders")
    Call<Object> saveOrder(@Body Order order);

    @POST("users/register")
    Call<Object> register(@Body RegistrationUser user);

    @GET("users/{user_id}")
    Call<Object> getAuthor(@Path("user_id") Long authorId);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("users")
    Call<Object> updateUser(@Header("Authorization") String authorization, @Body JsonObject post);
}
