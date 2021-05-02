package org.hongxi.sample.webflux.support;

import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Created by shenhongxi on 2021/4/29.
 */
public class ModifiedServerWebExchange extends ServerWebExchangeDecorator {

    private Map<String, Object> body;

    public ModifiedServerWebExchange(ServerWebExchange delegate, Map<String, Object> body) {
        super(delegate);
        this.body = body;
    }

    @Override
    public Mono<MultiValueMap<String, String>> getFormData() {
        return super.getFormData();
    }
}
