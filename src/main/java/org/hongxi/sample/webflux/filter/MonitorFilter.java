package org.hongxi.sample.webflux.filter;

import lombok.extern.slf4j.Slf4j;
import org.hongxi.sample.webflux.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by shenhongxi on 2021/4/22.
 */
@Slf4j
@Order(-3)
@Component
public class MonitorFilter implements WebFilter, InitializingBean {

    private static final String UNKNOWN_URI = "/unknown";

    // 针对只有一个uri映射到的method
    // key: methodSign value: uriPattern
    private final Map<String, String> uriPatterns = new HashMap<>(128);
    // 多个uri映射到同一个method  @RequestMapping({"/hi", "/hello"})
    // key: methodSign value: RequestMappingInfo
    private final Map<String, RequestMappingInfo> requestMappings = new HashMap<>();
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        preHandle(exchange);
        return chain.filter(exchange)
                .doOnSuccess(signal -> postHandle(exchange, null))
                .doOnError(cause -> postHandle(exchange, cause));
    }

    private void preHandle(ServerWebExchange exchange) {
        log.info("preHandle");
        exchange.getAttributes().put(WebUtils.START_TIMESTAMP_ATTR, System.currentTimeMillis());
        String uriPattern = getUriPattern(exchange);
        if (uriPattern != null && !uriPattern.equals(UNKNOWN_URI)) {
            exchange.getAttributes().put(WebUtils.URI_PATTERN_ATTR, uriPattern);
        }
//        throw new RuntimeException("test exception");
    }

    private void postHandle(ServerWebExchange exchange, Throwable throwable) {
        log.info("postHandle");
        Long start = exchange.getAttribute(WebUtils.START_TIMESTAMP_ATTR);
        if (start != null) {
            long cost = System.currentTimeMillis() - start;
            log.info("uri: {}, cost: {}, error: {}",
                    exchange.getRequest().getPath(), cost, throwable != null);
        }
    }

    private void preLoadAllUris() {
        Map<RequestMappingInfo, HandlerMethod> requestMapInfoHandlerMap = requestMappingHandlerMapping.getHandlerMethods();
        for (RequestMappingInfo info : requestMapInfoHandlerMap.keySet()) {
            PatternsRequestCondition patternsRequestCondition = info.getPatternsCondition();
            Set<PathPattern> patterns = patternsRequestCondition.getPatterns();
            String methodSignal = requestMapInfoHandlerMap.get(info).getMethod().toGenericString();
            if (patterns.size() == 1) {
                uriPatterns.put(methodSignal, patterns.iterator().next().getPatternString());
            } else {
                requestMappings.put(methodSignal, info);
            }
        }
    }

    private String getUriPattern(ServerWebExchange exchange) {
        String uri = exchange.getAttribute(WebUtils.REQUEST_URI_ATTR);
        if (!StringUtils.hasLength(uri)) {
            return UNKNOWN_URI;
        }
        if (uriPatterns.containsValue(uri)) {
            return uri;
        }
        try {
            Mono<Object> handlerMono = requestMappingHandlerMapping.getHandler(exchange);
            String handlerKey = uri + "_handler";
            handlerMono.subscribe(handler -> exchange.getAttributes().put(handlerKey, handler));
            Object handler = exchange.getAttribute(handlerKey);
            if (handler == null) {
                return UNKNOWN_URI;
            }
            exchange.getAttributes().remove(handlerKey);
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                String methodSign = handlerMethod.getMethod().toGenericString();
                String uriPattern = uriPatterns.get(methodSign);
                if (!StringUtils.hasLength(uriPattern)) {
                    return uriPattern;
                }
                if (requestMappings.containsKey(methodSign)) {
                    RequestMappingInfo mappingInfo = requestMappings.get(methodSign);
                    PatternsRequestCondition patternsRequestCondition = mappingInfo.getPatternsCondition();
                    Set<PathPattern> pathPatterns = patternsRequestCondition.getPatterns();
                    if (!CollectionUtils.isEmpty(pathPatterns)) {
                        return pathPatterns.iterator().next().getPatternString();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("get uri pattern error, {}", e.getClass());
        }
        return UNKNOWN_URI;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        preLoadAllUris();
    }
}
