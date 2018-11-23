package io.github.boldijar.cosasapp.server;


import io.github.boldijar.cosasapp.itecdata.LoginResponse;
import io.github.boldijar.cosasapp.itecdata.Response;
import io.github.boldijar.cosasapp.itecdata.User;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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

}
