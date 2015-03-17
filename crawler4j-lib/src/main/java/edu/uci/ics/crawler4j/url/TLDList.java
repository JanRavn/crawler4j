package edu.uci.ics.crawler4j.url;

import com.google.common.base.Joiner;
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
            InternetDomainName idn = InternetDomainName.from(str);
            try {
                return idn.topPrivateDomain().name();
            } catch (Throwable ignored) {
            }
            return Joiner.on(".").join(idn.parts().subList(1, idn.parts().size()));
        }
        return "";
    }

}
