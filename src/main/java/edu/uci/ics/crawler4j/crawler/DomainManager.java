package edu.uci.ics.crawler4j.crawler;

import edu.uci.ics.crawler4j.url.WebURL;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ravn Systems
 * Date: 28/11/12
 * Time: 10:03
 * <p/>
 * The domain manager keeps track of the different domains which are visited during the
 * crawl.
 * For now it is only design to keep track of the domains, but this could be extended
 * to include statistics or any other metadata.
 *
 * @author Jan Van Hoecke
 */
public class DomainManager {
    /// State information
    private Set<String> domains;

    public DomainManager() {
        domains = new HashSet<String>();
    }

    /**
     * Registers a domain with the domain manager. From now
     * on checks for this domain will return true.
     *
     * @param url
     */
    public void registerUrl(String url) {
        WebURL webUrl = new WebURL(url);
        domains.add(webUrl.getDomain());
    }

    /**
     * Registers the specified domain with the domain manager.
     * @param domain
     */
    public void registerDomain(String domain) {
        domains.add(domain);
    }



    /**
     * Checks if the specified domain is in the set of domains
     * registered with the domain manager.
     *
     * @param domain
     * @return
     */
    public boolean isRegisteredDomain(String domain) {
        return domains.contains(domain);
    }

    /**
     * Checks if the specified url has a domain which is registered
     * with the domain manager.
     *
     * @param url
     * @return
     */
    public boolean isInRegistredDomain(String url) {
        return isInRegistredDomain(new WebURL(url));
    }

    /**
     * Checks if the specified url has a domain which is registered
     * with the domain manager.
     *
     * @param url
     * @return
     */
    public boolean isInRegistredDomain(WebURL url) {
        return isRegisteredDomain(url.getDomain());
    }

    /**
     * Resets the domain manager. All registered domains will be
     * deleted.
     */
    public void reset() {
        domains = new HashSet<String>();
    }
}
