package org.hongxi.sample.webflux.support;

import org.springframework.web.server.ServerWebExchange;

/**
 * Created by shenhongxi on 2021/4/29.
 */
public abstract class WebUtils {

    public static boolean shouldNotFilter(ServerWebExchange exchange) {
        return exchange.getAttributeOrDefault(WebUtils.SHOULD_NOT_FILTER_ATTR, false)
                || exchange.getAttribute(WebUtils.PATH_PATTERN_ATTR) == null;
    }

    public static final String START_TIMESTAMP_ATTR = qualify("startTimestamp");
    public static final String PATH_PATTERN_ATTR = qualify("pathPattern");
    public static final String SHOULD_NOT_FILTER_ATTR = qualify("shouldNotFilter");
    public static final String SESSION_CONTEXT_ATTR = qualify("sessionContext");
    public static final String REQUEST_PARAMS_ATTR = qualify("requestParams");

    private static String qualify(String attr) {
        return WebUtils.class.getName() + "." + attr;
    }
}
