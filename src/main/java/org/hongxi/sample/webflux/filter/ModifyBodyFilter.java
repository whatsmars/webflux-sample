package org.hongxi.sample.webflux.filter;

import lombok.extern.slf4j.Slf4j;
import org.hongxi.sample.webflux.support.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Created by shenhongxi on 2021/4/29.
 */
@Slf4j
@Order(-1)
@Component
public class ModifyBodyFilter implements WebFilter {

    @Autowired
    private Crypto crypto;
    @Autowired
    private ServerCodecConfigurer codecConfigurer;

    public ModifyBodyFilter(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ParamUtils.from(exchange)
                .map(params -> decrypt(exchange, params))
                .map(params -> decorate(exchange, params))
                .flatMap(chain::filter);
    }

    private Map<String, Object> decrypt(ServerWebExchange exchange, Map<String, Object> params) {
        Map<String, Object> decrypted = crypto.decrypt(params);
        exchange.getAttributes().put(WebUtils.REQUEST_PARAMS_ATTR, decrypted);
        return decrypted;
    }

    private ServerWebExchange decorate(ServerWebExchange exchange, Map<String, Object> params) {
        MediaType contentType = exchange.getRequest().getHeaders().getContentType();
        if (contentType != null && contentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            ServerHttpRequest serverHttpRequest = new ModifiedServerHttpRequest(exchange.getRequest(), params);
            ServerHttpResponse serverHttpResponse = new ModifiedServerHttpResponse(exchange, codecConfigurer.getReaders(), crypto);
            return exchange.mutate().request(serverHttpRequest).response(serverHttpResponse).build();
        } else {
            return new ModifiedServerWebExchange(exchange, params);
        }
    }
}
