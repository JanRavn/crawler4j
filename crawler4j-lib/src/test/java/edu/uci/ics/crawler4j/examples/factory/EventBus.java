package edu.uci.ics.crawler4j.examples.factory;

/**
 * Created by Ravn Systems
 * User: janvh
 * Date: 27/11/12
 * Time: 23:55
 *
 * Example Event bus. Simply prints the events to stdout.
 *
 * @author Jan Van Hoecke
 */
public class EventBus {
    public void notify(Event event) {
        System.out.println("URL: " + event.url);
        System.out.println("Domain: '" + event.domain + "'");
        System.out.println("Sub-domain: '" + event.subDomain + "'");
        System.out.println("Path: '" + event.path + "'");
        System.out.println("Parent page: " + event.parentUrl);
    }

    public static class Event {
        private String url;
        private String domain;
        private String path;
        private String subDomain;
        private String parentUrl;

        public Event() {
        }

        public Event(String url, String domain, String path, String subDomain, String parentUrl) {
            this.url = url;
            this.domain = domain;
            this.path = path;
            this.subDomain = subDomain;
            this.parentUrl = parentUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSubDomain() {
            return subDomain;
        }

        public void setSubDomain(String subDomain) {
            this.subDomain = subDomain;
        }

        public String getParentUrl() {
            return parentUrl;
        }

        public void setParentUrl(String parentUrl) {
            this.parentUrl = parentUrl;
        }
    }
}
