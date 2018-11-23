package io.github.boldijar.cosasapp.server;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import io.github.boldijar.cosasapp.util.Prefs;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Paul
 * @since 2018.10.12
 */
public class Http {

    private static boolean DEBUG = true;

    private static final String ENDPOINT = "http://itec-api.deventure.co/api/";
    private ApiService mApiService;
    private SwaggerService mSwaggerService;

    private Http() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mSwaggerService = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .create())
                )
                .client(new OkHttpClient.Builder()
                        .addInterceptor(interceptor).addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                Request.Builder requestBuilder = original.newBuilder();
                                requestBuilder.addHeader("Authorization", "Bearer " + Prefs.Token.get());
                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        }).build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(ENDPOINT)
                .build().create(SwaggerService.class);

        mApiService = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .create())
                )

                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(ENDPOINT)
                .client(new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor(chain -> {
                            Request original = chain.request();
                            HttpUrl originalHttpUrl = original.url();
                            HttpUrl.Builder urlBuilder = originalHttpUrl.newBuilder();
                            if (DEBUG) {
                                urlBuilder = urlBuilder.addQueryParameter("XDEBUG_SESSION_START", "PHPSTORM");
                            }
                            HttpUrl url = urlBuilder.build();

                            Request.Builder requestBuilder = original.newBuilder()
                                    .url(url);

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        })
                        .addInterceptor(chain -> {
                            final Request original = chain.request();
                            Request.Builder builder = original.newBuilder();
                            builder = builder.header("Authorization",
                                    "Bearer " + Prefs.Token.get());
                            return chain.proceed(builder.build());
                        })
                        .build())
                .build().create(ApiService.class);
    }

    public SwaggerService getSwaggerService() {
        return mSwaggerService;
    }

    public ApiService getApiService() {
        return mApiService;
    }

    private static Http instance;

    public static Http getInstance() {
        if (instance == null) {
            instance = new Http();
        }
        return instance;
    }
}
