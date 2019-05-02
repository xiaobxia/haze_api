package com.vxianjin.gringotts.api;

import com.google.gson.GsonBuilder;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: kiro
 * @Date: 2018/7/4
 * @Description:
 */
public class CloseableOkHttp {

    private static final Object LOCK_CLIENT = new Object();
    private static final Object LOCK_REMOTE_SERVICE = new Object();
    private static final String TAG_DEFAULT = "tag_default";
    private static HashMap<String, OkHttpClient> map = new HashMap<>();
    private static HashMap<String, Object> remoteServiceHashMap = new HashMap<>();

    private static OkHttpClient initDefault() {
        ConnectionPool pool = new ConnectionPool(5, 10, TimeUnit.MINUTES);
        return new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .followRedirects(true)
                .readTimeout(3, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .writeTimeout(3, TimeUnit.MINUTES)
                .connectionPool(pool)
                .build();
    }

    private static OkHttpClient obtainDefault() {
        if (!map.containsKey(TAG_DEFAULT)) {
            synchronized (LOCK_CLIENT) {
                if (!map.containsKey(TAG_DEFAULT)) {
                    OkHttpClient client = initDefault();
                    map.put(TAG_DEFAULT, client);
                }
            }
        }
        return map.get(TAG_DEFAULT);
    }

    public static <RemoteService> RemoteService obtainRemoteService(String apiHost, Class<RemoteService> clazz) {
        return obtainRemoteService(apiHost, clazz, true);
    }

    public static <RemoteService> RemoteService obtainRemoteService(String apiHost, Class<RemoteService> clazz, boolean isCached) {
        Config config = new Config(apiHost);
        return obtainRemoteService(config, clazz, isCached);
    }

    private static <RemoteService> RemoteService obtainRemoteService(Config config, Class<RemoteService> clazz, boolean isCached) {

        final String mapKey = clazz.getCanonicalName();
        if (!remoteServiceHashMap.containsKey(mapKey)) {
            synchronized (LOCK_REMOTE_SERVICE) {
                if (!remoteServiceHashMap.containsKey(mapKey)) {
                    final String apiHost = config.apiHost;
                    final retrofit2.Converter.Factory factory = config.factory;
                    final OkHttpClient client = config.client == null ? obtainDefault() : config.client;

                    Retrofit.Builder builder = new Retrofit.Builder();
                    if (apiHost != null) {
                        builder.baseUrl(apiHost);
                    }
                    builder.client(client);
                    if (factory != null) {
                        builder.addConverterFactory(factory);
                    }
                    Retrofit retrofit = builder.build();
                    RemoteService remoteService = retrofit.create(clazz);
                    if (isCached) {
                        remoteServiceHashMap.put(mapKey, remoteService);
                    }
                    System.out.println("create remote service ...");
                }
            }
        }
        return (RemoteService) remoteServiceHashMap.get(mapKey);
    }

    public static final class Config {

        private static final GsonConverterFactory GSON_CONVERTER_FACTORY = GsonConverterFactory.create(new GsonBuilder().create());

        private String apiHost;
        private retrofit2.Converter.Factory factory = GSON_CONVERTER_FACTORY;
        private OkHttpClient client;

        public Config() {
        }

        public Config(String apiHost) {
            this.apiHost = apiHost;
        }

        public Config(String apiHost, OkHttpClient client) {
            this.apiHost = apiHost;
            this.client = client;
        }

        public void setApiHost(String apiHost) {
            this.apiHost = apiHost;
        }

        public void setClient(OkHttpClient client) {
            this.client = client;
        }

        public void setFactory(Converter.Factory factory) {
            this.factory = factory;
        }


    }
}
