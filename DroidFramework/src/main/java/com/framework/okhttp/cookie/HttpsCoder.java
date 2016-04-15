package com.framework.okhttp.cookie;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;

/**
 * Created by CaiYiMing on 2015/12/19.
 */
public class HttpsCoder {

    /**
     * 协议
     * 支持TLS和SSL协议
     */
    public static final String PROTOCOL = "TLS";

    /**
     * @param is
     * @param password
     * @return keyStore
     * @throws Exception
     */
    private static KeyStore getKeyStore(InputStream is, String password) throws Exception {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        keyStore.load(is, password.toCharArray());

        return keyStore;
    }

    private static SSLSocketFactory getSSLSocketFactory(InputStream keyStoreInputStream, String password) throws Exception {

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        KeyStore keyStore = getKeyStore(keyStoreInputStream, password);

        keyManagerFactory.init(keyStore, password.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        trustManagerFactory.init(keyStore);

        SSLContext context = SSLContext.getInstance(PROTOCOL);

        context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        keyStoreInputStream.close();

        return context.getSocketFactory();
    }

    public static void configSSLSocketFactory(OkHttpClient client, InputStream keyStoreInputStream, String password) throws Exception {
        SSLSocketFactory socketFactory = getSSLSocketFactory(keyStoreInputStream, password);
        client.newBuilder().sslSocketFactory(socketFactory);
    }

    public static void configSSLSocketFactory(HttpsURLConnection conn, InputStream keyStoreInputStream, String password) throws Exception {
        SSLSocketFactory socketFactory = getSSLSocketFactory(keyStoreInputStream, password);
        conn.setSSLSocketFactory(socketFactory);
    }

}
