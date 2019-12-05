
import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anthony on 2017. 5. 12..
 */

public class RetrofitManager {
    private static Retrofit retrofit;
    private static boolean sIsChangedAPIServer;
    private static String sFrontSa;

    public static Retrofit getRetrofit(Context context) {
        if (null == retrofit || sIsChangedAPIServer) {
            synchronized (RetrofitManager.class) {
                if (null == retrofit || sIsChangedAPIServer) {
                    init(Url.getApiDomain(context));
                }
            }
        }

        return retrofit;
    }

    public static void changedAPIServer() {
        sIsChangedAPIServer = true;
    }

    public static void setFrontSa(String frontSa) {
        if (sFrontSa == null || !sFrontSa.equals(frontSa)) {
            sIsChangedAPIServer = true;
            sFrontSa = frontSa;
        }
    }

    public static String getFrontSa() {
        return sFrontSa;
    }

    private static void init(String url) {
        // Log
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Header : request
        Interceptor requestInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request request = chain.request().newBuilder()

                        .addHeader(Constants.HEADER_HASH_KEY, sFrontSa)
                        .build();

                return chain.proceed(request);
            }
        };

        // Header : response
        Interceptor responseInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (!originalResponse.headers(Constants.HEADER_HASH_KEY).isEmpty()) {
                    String frontSa = null;
                    for (String hashKey : originalResponse.headers(Constants.HEADER_HASH_KEY)) {
                        frontSa = hashKey;
                    }
                    sFrontSa = frontSa;
                }
                return originalResponse;
            }
        };

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(httpLoggingInterceptor);
        okHttpClient.addInterceptor(requestInterceptor);
        okHttpClient.addInterceptor(responseInterceptor);

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(url);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.client(okHttpClient.connectTimeout(15, TimeUnit.SECONDS).build());	// 위캐닝 라이브러리 불러오는 시간이 길어서 따로 추가

        retrofit = builder.build();
    }
}
