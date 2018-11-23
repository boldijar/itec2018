package io.github.boldijar.cosasapp.server;


import io.github.boldijar.cosasapp.itecdata.Response;
import io.github.boldijar.cosasapp.itecdata.User;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Paul
 * @since 2018.10.12
 */
public interface SwaggerService {

    @POST("Account/Register")
    Observable<Response> register(@Body User user);

}
