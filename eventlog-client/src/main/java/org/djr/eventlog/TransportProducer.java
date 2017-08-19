package org.djr.eventlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class TransportProducer {
    private static Logger log = LoggerFactory.getLogger(TransportProducer.class);
    public static Retrofit getTransport(ObjectMapper objectMapper, String baseUrl, boolean enableTrafficLogging) {
        log.debug("getTransport() baseUrl:{}, enableTrafficLogging:{}", baseUrl, enableTrafficLogging);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        setLoggingInterceptor(enableTrafficLogging, httpClient);
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl);
        retrofitBuilder.client(httpClient.build());
        if (null != objectMapper) {
            setConverterFactory(objectMapper, retrofitBuilder);
        }
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit;
    }

    private static void setConverterFactory(ObjectMapper objectMapper, Retrofit.Builder retrofitBuilder) {
        if (null != objectMapper) {
            retrofitBuilder.addConverterFactory(JacksonConverterFactory.create(objectMapper))  ;
        }
    }

    private static void setLoggingInterceptor(boolean enableTrafficLogging, OkHttpClient.Builder builder) {
        if (enableTrafficLogging) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
    }
}