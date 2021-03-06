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

package edu.uci.ics.crawler4j.crawler;

public class CrawlConfig {

    /**
     * The folder which will be used by crawler for storing the intermediate
     * crawl data. The content of this folder should not be modified manually.
     */
    private String crawlStorageFolder;

    /**
     * If this feature is enabled, you would be able to resume a previously
     * stopped/crashed crawl. However, it makes crawling slightly slower
     */
    private boolean resumableCrawling = false;

    /**
     * Maximum depth of crawling For unlimited depth this parameter should be
     * set to -1
     */
    private int maxDepthOfCrawling = -1;

    /**
     * Maximum number of pages to fetch For unlimited number of pages, this
     * parameter should be set to -1
     */
    private int maxPagesToFetch = -1;

    /**
     * user-agent string that is used for representing your crawler to web
     * servers. See http://en.wikipedia.org/wiki/User_agent for more details
     */
    private String userAgentString = "crawler4j (http://code.google.com/p/crawler4j/)";

    /**
     * Politeness delay in milliseconds (delay between sending two requests to
     * the same host).
     */
    private int politenessDelay = 200;

    /**
     * Should we also crawl https pages?
     */
    private boolean includeHttpsPages = false;

    /**
     * Should we fetch binary content such as images, audio, ...?
     */
    private boolean includeBinaryContentInCrawling = false;

    /**
     * Maximum Connections per host
     */
    private int maxConnectionsPerHost = 100;

    /**
     * Maximum total connections
     */
    private int maxTotalConnections = 100;

    /**
     * Socket timeout in milliseconds
     */
    private int socketTimeout = 20000;

    /**
     * Connection timeout in milliseconds
     */
    private int connectionTimeout = 30000;

    /**
     * Max number of outgoing links which are processed from a page
     */
    private int maxOutgoingLinksToFollow = 5000;

    /**
     * Max allowed size of a page. Pages larger than this size will not be
     * fetched.
     */
    private int maxDownloadSize = 1048576;

    /**
     * Should we follow redirects?
     */
    private boolean followRedirects = true;

    /**
     * If crawler should run behind a proxy, this parameter can be used for
     * specifying the proxy host.
     */
    private String proxyHost = null;

    /**
     * If crawler should run behind a proxy, this parameter can be used for
     * specifying the proxy port.
     */
    private int proxyPort = 80;

    /**
     * If crawler should run behind a proxy and user/pass is needed for
     * authentication in proxy, this parameter can be used for specifying the
     * username.
     */
    private String proxyUsername = null;

    /**
     * If crawler should run behind a proxy and user/pass is needed for
     * authentication in proxy, this parameter can be used for specifying the
     * password.
     */
    private String proxyPassword = null;

    /**
     * The Proxy Server domain. If set, the authentication method will
     * switch to NTLM Authentication.
     */
    private String proxyDomain = null;

    /**
     * If the crawler should remain on the domain of the provided seeds, this
     * parameters should be set to true.
     */
    private boolean stayOnDomain = false;


    /*
     * If the crawler needs to authenticate during the crawl, a single set
     * of username/password can be provided (with Authentication scope ALL).
     * Set this value to null to disable authentication.
     */
    private String username = null;

    /*
    * If the crawler needs to authenticate during the crawl, a single set
    * of username/password can be provided (with Authentication scope ALL).
    * Set this value to null to disable authentication.
    */
    private String password = null;


    /**
     * The NT domain to which the user belongs. If the domain is set, the authentication
     * method will switch to NTLM authentication.
     */
    private String domain = null;

    /**
     * Path the the client key store
     */
    private String keyStorePath = null;

    /**
     * Password to open the client key store
     */
    private String keyStorePass = null;

    /**
     * Type of the client key store
     */
    private String keyStoreType = "JKS";

    /**
     * Password to read the client certificate from the key store
     */
    private String keyPass = null;

    /**
     * Path to the trust store
     */
    private String trustStorePath = null;

    /**
     * Password to read the trust store
     */
    private String trustStorePass = null;

    /**
     * Type of the trust store
     */
    private String trustStoreType = "JKS";

    /**
     * Trust self-signed certificates
     */
    private boolean trustSelfSigned = false;

    /**
     * Trust all server certificates, including
     * self-signed and invalid ones.
     */
    private boolean trustAll = true;

    public CrawlConfig() {
    }

    /**
     * Validates the configs specified by this instance.
     *
     * @throws Exception
     */
    public void validate() throws Exception {
        if (crawlStorageFolder == null) {
            throw new Exception("Crawl storage folder is not set in the CrawlConfig.");
        }
        if (politenessDelay < 0) {
            throw new Exception("Invalid value for politeness delay: " + politenessDelay);
        }
        if (maxDepthOfCrawling < -1) {
            throw new Exception("Maximum crawl depth should be either a positive number or -1 for unlimited depth.");
        }
        if (maxDepthOfCrawling > Short.MAX_VALUE) {
            throw new Exception("Maximum value for crawl depth is " + Short.MAX_VALUE);
        }

    }

    public String getCrawlStorageFolder() {
        return crawlStorageFolder;
    }

    /**
     * The folder which will be used by crawler for storing the intermediate
     * crawl data. The content of this folder should not be modified manually.
     */
    public void setCrawlStorageFolder(String crawlStorageFolder) {
        this.crawlStorageFolder = crawlStorageFolder;
    }

    public boolean isResumableCrawling() {
        return resumableCrawling;
    }

    /**
     * If this feature is enabled, you would be able to resume a previously
     * stopped/crashed crawl. However, it makes crawling slightly slower
     */
    public void setResumableCrawling(boolean resumableCrawling) {
        this.resumableCrawling = resumableCrawling;
    }

    public int getMaxDepthOfCrawling() {
        return maxDepthOfCrawling;
    }

    /**
     * Maximum depth of crawling For unlimited depth this parameter should be
     * set to -1
     */
    public void setMaxDepthOfCrawling(int maxDepthOfCrawling) {
        this.maxDepthOfCrawling = maxDepthOfCrawling;
    }

    public int getMaxPagesToFetch() {
        return maxPagesToFetch;
    }

    /**
     * Maximum number of pages to fetch For unlimited number of pages, this
     * parameter should be set to -1
     */
    public void setMaxPagesToFetch(int maxPagesToFetch) {
        this.maxPagesToFetch = maxPagesToFetch;
    }

    public String getUserAgentString() {
        return userAgentString;
    }

    /**
     * user-agent string that is used for representing your crawler to web
     * servers. See http://en.wikipedia.org/wiki/User_agent for more details
     */
    public void setUserAgentString(String userAgentString) {
        this.userAgentString = userAgentString;
    }

    public int getPolitenessDelay() {
        return politenessDelay;
    }

    /**
     * Politeness delay in milliseconds (delay between sending two requests to
     * the same host).
     *
     * @param politenessDelay the delay in milliseconds.
     */
    public void setPolitenessDelay(int politenessDelay) {
        this.politenessDelay = politenessDelay;
    }

    public boolean isIncludeHttpsPages() {
        return includeHttpsPages;
    }

    /**
     * Should we also crawl https pages?
     */
    public void setIncludeHttpsPages(boolean includeHttpsPages) {
        this.includeHttpsPages = includeHttpsPages;
    }

    public boolean isIncludeBinaryContentInCrawling() {
        return includeBinaryContentInCrawling;
    }

    /**
     * Should we fetch binary content such as images, audio, ...?
     */
    public void setIncludeBinaryContentInCrawling(boolean includeBinaryContentInCrawling) {
        this.includeBinaryContentInCrawling = includeBinaryContentInCrawling;
    }

    public int getMaxConnectionsPerHost() {
        return maxConnectionsPerHost;
    }

    /**
     * Maximum Connections per host
     */
    public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    /**
     * Maximum total connections
     */
    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * Socket timeout in milliseconds
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Connection timeout in milliseconds
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getMaxOutgoingLinksToFollow() {
        return maxOutgoingLinksToFollow;
    }

    /**
     * Max number of outgoing links which are processed from a page
     */
    public void setMaxOutgoingLinksToFollow(int maxOutgoingLinksToFollow) {
        this.maxOutgoingLinksToFollow = maxOutgoingLinksToFollow;
    }

    public int getMaxDownloadSize() {
        return maxDownloadSize;
    }

    /**
     * Max allowed size of a page. Pages larger than this size will not be
     * fetched.
     * Set to a negative number to disable.
     */
    public void setMaxDownloadSize(int maxDownloadSize) {
        this.maxDownloadSize = maxDownloadSize;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    /**
     * Should we follow redirects?
     */
    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * If crawler should run behind a proxy, this parameter can be used for
     * specifying the proxy host.
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * If crawler should run behind a proxy, this parameter can be used for
     * specifying the proxy port.
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    /**
     * If crawler should run behind a proxy and user/pass is needed for
     * authentication in proxy, this parameter can be used for specifying the
     * username.
     */
    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * If crawler should run behind a proxy and user/pass is needed for
     * authentication in proxy, this parameter can be used for specifying the
     * password.
     */
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public boolean isStayOnDomain() {
        return stayOnDomain;
    }

    public String getUsername() {
        return username;
    }

    public String getProxyDomain() {
        return proxyDomain;
    }

    /**
     * The Proxy Server domain. If set, the authentication method will
     * switch to NTLM Authentication.
     */
    public void setProxyDomain(String proxyDomain) {
        this.proxyDomain = proxyDomain;
    }

    /**
     * If the crawler needs to authenticate during the crawl, a single set
     * of username/password can be provided (with Authentication scope ALL).
     * Set this value to null to disable authentication.
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * If the crawler needs to authenticate during the crawl, a single set
     * of username/password can be provided (with Authentication scope ALL).
     * Set this value to null to disable authentication.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    /**
     * The NT domain to which the user belongs. If the domain is set, the authentication
     * method will switch to NTLM authentication.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * If the crawler should remain on the domain of the provided seeds, this
     * parameters should be set to true.
     *
     * @param stayOnDomain if true the crawler will remain on the seed domains.
     */
    public void setStayOnDomain(boolean stayOnDomain) {
        this.stayOnDomain = stayOnDomain;
    }


    public String getKeyStorePath() {
        return keyStorePath;
    }

    /**
     * Set the path to the key store used for SSL client authentication.
     * Defaults to null.
     * @param keyStorePath file path to the key store.
     */
    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePass() {
        return keyStorePass;
    }

    /**
     * Password to the read from the client key store.
     * Defaults to null.
     * @param keyStorePass
     */
    public void setKeyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    /** The type of key certificate store.
     *  Defaults to "JKS". Other possible values are "PKCS12" and "JCEKS".
     * @param keyStoreType
     */
    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyPass() {
        return keyPass;
    }

    /**
     * Password to read the client certificate.
     * @return
     */
    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    /**
     * Set the path to the key store used for SSL client authentication.
     * Defaults to null.
     * @param trustStorePath
     */
    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getTrustStorePass() {
        return trustStorePass;
    }

    /**
     * Set the password to read from the trust key store
     * @param trustStorePass
     */
    public void setTrustStorePass(String trustStorePass) {
        this.trustStorePass = trustStorePass;
    }

    public String getTrustStoreType() {
        return trustStoreType;
    }

    /**
     * The type of trust certificate store.
     * Defaults to "JKS". Other possible values are "PKCS12" and "JCEKS".
     * @param trustStoreType
     */
    public void setTrustStoreType(String trustStoreType) {
        this.trustStoreType = trustStoreType;
    }

    public boolean isTrustSelfSigned() {
        return trustSelfSigned;
    }

    /**
     * Possibility to enable/disable trusting self-signed
     * server certificates.
     * This setting is overridden by {@link #setTrustAll(boolean)}
     * Disabled by default.
     * @param trustSelfSigned
     */
    public void setTrustSelfSigned(boolean trustSelfSigned) {
        this.trustSelfSigned = trustSelfSigned;
    }

    public boolean isTrustAll() {
        return trustAll;
    }

    /**
     * Possibility to enable/disable trusting all certificates,
     * including self-signed and invalid certificates.
     * Enabled by default as this was the legacy behaviour.
     * @param trustAll
     */
    public void setTrustAll(boolean trustAll) {
        this.trustAll = trustAll;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Crawl storage folder: " + getCrawlStorageFolder() + "\n");
        sb.append("Resumable crawling: " + isResumableCrawling() + "\n");
        sb.append("Max depth of crawl: " + getMaxDepthOfCrawling() + "\n");
        sb.append("Max pages to fetch: " + getMaxPagesToFetch() + "\n");
        sb.append("User agent string: " + getUserAgentString() + "\n");
        sb.append("Include https pages: " + isIncludeHttpsPages() + "\n");
        sb.append("Include binary content: " + isIncludeBinaryContentInCrawling() + "\n");
        sb.append("Max connections per host: " + getMaxConnectionsPerHost() + "\n");
        sb.append("Max total connections: " + getMaxTotalConnections() + "\n");
        sb.append("Socket timeout: " + getSocketTimeout() + "\n");
        sb.append("Max total connections: " + getMaxTotalConnections() + "\n");
        sb.append("Max outgoing links to follow: " + getMaxOutgoingLinksToFollow() + "\n");
        sb.append("Max download size: " + getMaxDownloadSize() + "\n");
        sb.append("Should follow redirects?: " + isFollowRedirects() + "\n");
        sb.append("Stay on domain?: " + isStayOnDomain() + "\n");
        sb.append("Proxy host: " + getProxyHost() + "\n");
        sb.append("Proxy port: " + getProxyPort() + "\n");
        sb.append("Proxy username: " + getProxyUsername() + "\n");
        sb.append("Proxy password: " + getProxyPassword() + "\n");
        sb.append("Proxy domain: " + getProxyDomain() + "\n");
        sb.append("Username: " + getUsername() + "\n");
        sb.append("Password: " + getPassword() + "\n");
        sb.append("Domain: " + getDomain() + "\n");
        sb.append("Key store path: " + getKeyStorePath() + "\n");
        sb.append("Key store password: " + getKeyStorePass() + "\n");
        sb.append("Key store type: " + getKeyStoreType() + "\n");
        sb.append("Key password: " + getKeyPass() + "\n");
        sb.append("Trust store path: " + getTrustStorePath() + "\n");
        sb.append("Trust store password: " + getTrustStorePass() + "\n");
        sb.append("Trust store type: " + getTrustStoreType() + "\n");
        sb.append("Trust self-signed: " + isTrustSelfSigned() + "\n");
        sb.append("Trust all server cerst: " + isTrustAll() + "\n");
        return sb.toString();
    }

}
