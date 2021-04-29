package org.hongxi.sample.webflux.filter;

import lombok.extern.slf4j.Slf4j;
import org.hongxi.sample.webflux.support.WebUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Created by shenhongxi on 2021/4/26.
 */
@Slf4j
@Order(-4)
@Component
public class AccessFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("access start from uri: {}", exchange.getRequest().getPath());
        return chain.filter(exchange)
                .doOnEach(signal -> onEach(exchange));
    }

    private void onEach(ServerWebExchange exchange) {
        log.info("access end, now start clear some context attributes");
        exchange.getAttributes().remove(WebUtils.START_TIMESTAMP_ATTR);
    }
}
