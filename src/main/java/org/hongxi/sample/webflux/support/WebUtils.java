package org.hongxi.sample.webflux.support;

import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * Created by shenhongxi on 2021/4/29.
 */
public abstract class WebUtils {

    /**
     * a convenient way to acquire request params
     *
     * @see ParamUtils
     *
     * @param exchange
     * @return
     */
    public static Map<String, Object> getRequestParams(ServerWebExchange exchange) {
        return exchange.getAttribute(REQUEST_PARAMS_ATTR);
    }

    public static SessionContext getSessionContext(ServerWebExchange exchange) {
        return exchange.getAttribute(SESSION_CONTEXT_ATTR);
    }

    public static String getUserId(ServerWebExchange exchange) {
        SessionContext sessionContext = getSessionContext(exchange);
        return sessionContext != null ? sessionContext.getUserId() : null;
    }

    public static boolean shouldNotFilter(ServerWebExchange exchange) {
        return exchange.getAttributeOrDefault(WebUtils.SHOULD_NOT_FILTER_ATTR,
                exchange.getAttribute(WebUtils.PATH_PATTERN_ATTR) == null);
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
