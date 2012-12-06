/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.fetcher;

import edu.uci.ics.crawler4j.crawler.Configurable;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.*;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.zip.GZIPInputStream;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class PageFetcher extends Configurable {

    protected static final Logger logger = LoggerFactory.getLogger(PageFetcher.class);

    protected PoolingClientConnectionManager connectionManager;

    protected DefaultHttpClient httpClient;

    protected final Object mutex = new Object();

    protected long lastFetchTime = 0;

    protected IdleConnectionMonitorThread connectionMonitorThread = null;

    public PageFetcher(CrawlConfig config) {
        super(config);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
        paramsBean.setVersion(HttpVersion.HTTP_1_1);
        paramsBean.setContentCharset("UTF-8");
        paramsBean.setUseExpectContinue(false);

        params.setParameter(CoreProtocolPNames.USER_AGENT, config.getUserAgentString());
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, config.getSocketTimeout());
        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, config.getConnectionTimeout());

        params.setBooleanParameter("http.protocol.handle-redirects", false);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

        if (config.isIncludeHttpsPages()) {
            schemeRegistry.register(new Scheme("https", 443, new SSLSocketFactory(getSSLContext())));
        }

        connectionManager = new PoolingClientConnectionManager(schemeRegistry);
        connectionManager.setMaxTotal(config.getMaxTotalConnections());
        connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerHost());
        httpClient = new DefaultHttpClient(connectionManager, params);

        // Set proxy details
        if (config.getProxyHost() != null) {
            String password = config.getProxyPassword() != null ? config.getProxyPassword() : "";
            // Set Proxy authentication if provided.
            if (config.getProxyUsername() != null) {

                // Set Proxy authentication to NTLM if provided
                if (config.getProxyDomain() != null) {
                    httpClient.getCredentialsProvider().setCredentials(
                            new AuthScope(config.getProxyHost(), config.getProxyPort(), AuthScope.ANY_REALM, "ntlm"),
                            new NTCredentials(config.getProxyUsername(), password, "127.0.0.1", config.getProxyDomain()));
                } else {
                    httpClient.getCredentialsProvider().setCredentials(
                            new AuthScope(config.getProxyHost(), config.getProxyPort()),
                            new UsernamePasswordCredentials(config.getProxyUsername(), password));
                }

            }

            HttpHost proxy = new HttpHost(config.getProxyHost(), config.getProxyPort());
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        if (config.getUsername() != null) {
            String password = config.getPassword() != null ? config.getPassword() : "";
            // Set to NTLM authentication if domain is provided
            if (config.getDomain() != null) {
                httpClient.getCredentialsProvider().setCredentials(
                        new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, "ntlm"),
                        new NTCredentials(config.getUsername(), password, "127.0.0.1", config.getDomain()));
            } else {
                httpClient.getCredentialsProvider().setCredentials(
                        AuthScope.ANY,
                        new UsernamePasswordCredentials(config.getUsername(), password));
            }


        }

        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {

            @Override
            public void process(final HttpResponse response, final HttpContext context) throws HttpException,
                    IOException {
                HttpEntity entity = response.getEntity();
                Header contentEncoding = entity.getContentEncoding();
                if (contentEncoding != null) {
                    HeaderElement[] codecs = contentEncoding.getElements();
                    for (HeaderElement codec : codecs) {
                        if (codec.getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }

        });

        if (connectionMonitorThread == null) {
            connectionMonitorThread = new IdleConnectionMonitorThread(connectionManager);
        }
        connectionMonitorThread.start();

    }

    public PageFetchResult fetchHeader(WebURL webUrl) {
        PageFetchResult fetchResult = new PageFetchResult();
        String toFetchURL = webUrl.getURL();
        HttpGet get = null;
        try {
            get = new HttpGet(toFetchURL);
            synchronized (mutex) {
                long now = (new Date()).getTime();
                if (now - lastFetchTime < config.getPolitenessDelay()) {
                    Thread.sleep(config.getPolitenessDelay() - (now - lastFetchTime));
                }
                lastFetchTime = (new Date()).getTime();
            }
            get.addHeader("Accept-Encoding", "gzip");
            HttpResponse response = httpClient.execute(get);
            fetchResult.setEntity(response.getEntity());

            // Setting all headers
            for (Header header : response.getAllHeaders()) {
                fetchResult.addHeader(header.getName(), header.getValue());
            }

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                if (statusCode != HttpStatus.SC_NOT_FOUND) {
                    if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                        Header header = response.getFirstHeader("Location");
                        if (header != null) {
                            String movedToUrl = header.getValue();
                            movedToUrl = URLCanonicalizer.getCanonicalURL(movedToUrl, toFetchURL);
                            fetchResult.setMovedToUrl(movedToUrl);
                        }
                        fetchResult.setStatusCode(statusCode);
                        return fetchResult;
                    }
                    logger.info("Failed: " + response.getStatusLine().toString() + ", while fetching " + toFetchURL);
                }
                fetchResult.setStatusCode(response.getStatusLine().getStatusCode());
                return fetchResult;
            }

            fetchResult.setFetchedUrl(toFetchURL);
            String uri = get.getURI().toString();
            if (!uri.equals(toFetchURL)) {
                if (!URLCanonicalizer.getCanonicalURL(uri).equals(toFetchURL)) {
                    fetchResult.setFetchedUrl(uri);
                }
            }

            if (fetchResult.getEntity() != null) {
                long size = fetchResult.getEntity().getContentLength();
                if (size == -1) {
                    Header length = response.getLastHeader("Content-Length");
                    if (length == null) {
                        length = response.getLastHeader("Content-length");
                    }
                    if (length != null) {
                        size = Integer.parseInt(length.getValue());
                    } else {
                        size = -1;
                    }
                }
                if (config.getMaxDownloadSize() > -1 && size > config.getMaxDownloadSize()) {
                    fetchResult.setStatusCode(CustomFetchStatus.PageTooBig);
                    return fetchResult;
                }

                fetchResult.setStatusCode(HttpStatus.SC_OK);
                return fetchResult;

            } else {
                get.abort();
            }
        } catch (IOException e) {
            logger.error("Fatal transport error: " + e.getMessage() + " while fetching " + toFetchURL
                    + " (link found in doc #" + webUrl.getParentDocid() + ")");
            fetchResult.setStatusCode(CustomFetchStatus.FatalTransportError);
            return fetchResult;
        } catch (IllegalStateException e) {
            // ignoring exceptions that occur because of not registering https
            // and other schemes
        } catch (Exception e) {
            if (e.getMessage() == null) {
                logger.error("Error while fetching " + webUrl.getURL());
            } else {
                logger.error(e.getMessage() + " while fetching " + webUrl.getURL());
            }
        } finally {
            try {
                if (fetchResult.getEntity() == null && get != null) {
                    get.abort();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fetchResult.setStatusCode(CustomFetchStatus.UnknownError);
        return fetchResult;
    }

    /**
     * SSL Context which accepts any certificate.
     * @return
     */
    private SSLContext getSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs,
                                               String authType) {
                }
            }}, new SecureRandom());
            return sslContext;
        } catch (KeyManagementException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }

    public synchronized void shutDown() {
        if (connectionMonitorThread != null) {
            connectionManager.shutdown();
            connectionMonitorThread.shutdown();
        }
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    private static class GzipDecompressingEntity extends HttpEntityWrapper {

        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }

        @Override
        public InputStream getContent() throws IOException, IllegalStateException {

            // the wrapped entity's getContent() decides about repeatability
            InputStream wrappedin = wrappedEntity.getContent();

            return new GZIPInputStream(wrappedin);
        }

        @Override
        public long getContentLength() {
            // length of ungzipped content is not known
            return -1;
        }

    }
}
