package io.github.boldijar.cosasapp.server;


import java.util.List;

import io.github.boldijar.cosasapp.itecdata.CommentModel;
import io.github.boldijar.cosasapp.itecdata.Issue;
import io.github.boldijar.cosasapp.itecdata.LoginResponse;
import io.github.boldijar.cosasapp.itecdata.Response;
import io.github.boldijar.cosasapp.itecdata.Stats;
import io.github.boldijar.cosasapp.itecdata.User;
import io.github.boldijar.cosasapp.itecdata.UserResponse;
import io.github.boldijar.cosasapp.itecdata.VoteModel;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Paul
 * @since 2018.10.12
 */
public interface SwaggerService {

    @POST("Account/Register")
    Observable<Response> register(@Body User user);

    @POST("Token")
    @FormUrlEncoded
    Observable<LoginResponse> login(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);

    @GET("Issue/GetAll")
    Observable<List<Issue>> getIssues();

    @GET("Account/GetUserByEmail")
    Observable<UserResponse> getUser(@Query("email") String email);

    @POST("Issue/Create")
    Observable<Issue> createIssue(@Body Issue issue);
    @POST("Issue/Update")
    Completable updateIssue(@Body Issue issue);

    @POST("Comment/Create/{id}")
    Observable<CommentModel> createComment(@Path("id") String id, @Body CommentModel commentModel);

    @GET("Statistics/GetStats")
    Observable<Stats> getStats();

    @POST("Account/Update")
    Completable updateProfile(@Body User user);

    @POST("Issue/Vote/{Id}")
    Completable voteIssue(@Path("Id") String issueId, @Body VoteModel voteModel);
}
