package edu.uci.ics.crawler4j.url;

import com.google.common.net.InternetDomainName;

public class TLDList {

    public static boolean contains(String str) {
        if (InternetDomainName.isValid(str)) {
            return InternetDomainName.from(str).isTopPrivateDomain();
        }
        return false;
    }

    public static String domain(String str) {
        if (InternetDomainName.isValid(str)) {
            return InternetDomainName.from(str).topPrivateDomain().name();
        }
        return "";
    }

}
