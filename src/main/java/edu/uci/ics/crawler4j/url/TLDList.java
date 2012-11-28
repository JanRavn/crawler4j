package edu.uci.ics.crawler4j.url;

import com.google.common.net.InternetDomainName;

public class TLDList {

    public static boolean contains(String str) {
        return InternetDomainName.from(str).isTopPrivateDomain();
    }

    public static String domain(String str) {
        return InternetDomainName.from(str).topPrivateDomain().name();
    }

}
