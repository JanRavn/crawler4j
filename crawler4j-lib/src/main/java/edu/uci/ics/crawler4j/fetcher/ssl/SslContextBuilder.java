package edu.uci.ics.crawler4j.fetcher.ssl;

import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SslContextBuilder {

    static final String TLS = "TLS";
    static final String SSL = "SSL";

    private String keyStorePath;
    private String keyStorePass;
    private String keyPass;
    private String trustStorePath;
    private String trustStorePass;
    private String keyStoreType;
    private String trustStoreType;

    private String protocol;
    private SecureRandom secureRandom;

    private boolean trustSelfSigned = false;
    private boolean trustAll = false;

    public SslContextBuilder useTLS() {
        this.protocol = TLS;
        return this;
    }

    public SslContextBuilder useSSL() {
        this.protocol = SSL;
        return this;
    }

    public SslContextBuilder useProtocol(final String protocol) {
        this.protocol = protocol;
        return this;
    }

    public SslContextBuilder setSecureRandom(final SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
        return this;
    }

    public SslContextBuilder trustStoreType(String instance) {
        this.trustStoreType = instance;
        return this;
    }

    public SslContextBuilder keyStoreType(String instance) {
        this.keyStoreType = instance;
        return this;
    }

    public SslContextBuilder keyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
        return this;
    }

    public SslContextBuilder keyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass;
        return this;
    }

    public SslContextBuilder keyPass(String keyPass) {
        this.keyPass = keyPass;
        return this;
    }

    public SslContextBuilder trustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
        return this;
    }

    public SslContextBuilder trustStorePass(String trustStorePass) {
        this.trustStorePass = trustStorePass;
        return this;
    }

    public SslContextBuilder trustSelfSignedCerts(boolean enabled) {
        this.trustSelfSigned = enabled;
        return this;
    }

    public SslContextBuilder trustAllCerts(boolean enabled) {
        this.trustAll = enabled;
        return this;
    }

    SslContextBuilder() {
    }

    /**
     * HELPERS **
     */

    private KeyStore loadKeyStore(String path, String password, String instance) throws SSLException {
        if (path == null || path.isEmpty()) {
            return null;
        }

        try {
            KeyStore keyStore = KeyStore.getInstance(instance);
            keyStore.load(new FileInputStream(path), password == null ? null : password.toCharArray());
            return keyStore;
        } catch (KeyStoreException e) {
            throw new SSLException("KeyStoreException while loading KeyStore" + path, e);
        } catch (CertificateException e) {
            throw new SSLException("CertificateException while loading KeyStore" + path, e);
        } catch (NoSuchAlgorithmException e) {
            throw new SSLException("NoSuchAlgorithmException while loading KeyStore" + path, e);
        } catch (IOException e) {
            throw new SSLException("IOException while loading KeyStore" + path, e);
        }
    }

    private TrustManager[] loadTrustMaterial(final KeyStore truststore, final char[] truststorePassword, final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        final TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmfactory.init(truststore);
        final TrustManager[] trustmanagers = tmfactory.getTrustManagers();
        if (trustmanagers != null && trustStrategy != null) {
            for (int i = 0; i < trustmanagers.length; i++) {
                final TrustManager tm = trustmanagers[i];
                if (tm instanceof X509TrustManager) {
                    trustmanagers[i] = new TrustManagerDecorator((X509TrustManager) tm, trustStrategy);
                }
            }
        }
        return trustmanagers;
    }

    private TrustManager[] loadTrustMaterial(final KeyStore truststore, final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        return loadTrustMaterial(truststore, null, trustStrategy);
    }


    private TrustManager[] loadTrustMaterial(final KeyStore truststore) throws NoSuchAlgorithmException, KeyStoreException {
        return loadTrustMaterial(truststore, null, null);
    }

    private KeyManager[] loadKeyMaterial(final KeyStore keystore, final char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        final KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, keyPassword);
        return kmfactory.getKeyManagers();
    }

    public SSLContext build() throws SSLException {
        try {
            final SSLContext sslcontext = SSLContext.getInstance(this.protocol != null ? this.protocol : TLS);

            TrustStrategy trustStrategy = null;
            if (trustAll) {
                trustStrategy = new TrustAllStrategy();
            } else if (trustSelfSigned) {
                trustStrategy = new TrustSelfSignedStrategy();
            }

            TrustManager[] trustManagers = loadTrustMaterial(loadKeyStore(trustStorePath, trustStorePass, trustStoreType), trustStrategy);
            KeyManager[] keyManagers = loadKeyMaterial(loadKeyStore(keyStorePath, keyStorePass, keyStoreType), keyPass == null ? null : keyPass.toCharArray());

            sslcontext.init(keyManagers, trustManagers, secureRandom);

            return sslcontext;
        } catch (NoSuchAlgorithmException e) {
            throw new SSLException("NoSuchAlgorithmException while loading KeyStore", e);
        } catch (KeyManagementException e) {
            throw new SSLException("KeyManagementException while loading KeyStore", e);
        } catch (KeyStoreException e) {
            throw new SSLException("KeyStoreException while loading KeyStore", e);
        } catch (UnrecoverableKeyException e) {
            throw new SSLException("UnrecoverableKeyException while loading KeyStore", e);
        }
    }

    public static SslContextBuilder create() {
        return new SslContextBuilder();
    }

    static class TrustAllStrategy implements TrustStrategy {

        @Override
        public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            return true;
        }
    }

    static class TrustManagerDecorator implements X509TrustManager {

        private final X509TrustManager trustManager;
        private final TrustStrategy trustStrategy;

        TrustManagerDecorator(final X509TrustManager trustManager, final TrustStrategy trustStrategy) {
            super();
            this.trustManager = trustManager;
            this.trustStrategy = trustStrategy;
        }

        public void checkClientTrusted(
                final X509Certificate[] chain, final String authType) throws CertificateException {
            this.trustManager.checkClientTrusted(chain, authType);
        }

        public void checkServerTrusted(
                final X509Certificate[] chain, final String authType) throws CertificateException {
            if (!this.trustStrategy.isTrusted(chain, authType)) {
                this.trustManager.checkServerTrusted(chain, authType);
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return this.trustManager.getAcceptedIssuers();
        }

    }
}
