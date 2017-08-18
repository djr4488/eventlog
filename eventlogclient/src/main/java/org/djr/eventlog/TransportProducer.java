package org.djr.eventlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class TransportProducer {
    public static Retrofit getTransport(ObjectMapper objectMapper, String baseUrl) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl);
        setConverterFactory(objectMapper, retrofitBuilder);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit;
    }

    private static void setConverterFactory(ObjectMapper objectMapper, Retrofit.Builder retrofitBuilder) {
        if (null != objectMapper) {
            retrofitBuilder.addConverterFactory(JacksonConverterFactory.create(objectMapper))  ;
        }
    }
}