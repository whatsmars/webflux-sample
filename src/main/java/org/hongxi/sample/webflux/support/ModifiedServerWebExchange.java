package org.hongxi.sample.webflux.support;

import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenhongxi on 2021/4/29.
 */
public class ModifiedServerWebExchange extends ServerWebExchangeDecorator {

    public ModifiedServerWebExchange(ServerWebExchange delegate) {
        super(delegate);
    }

    @Override
    public Mono<MultiValueMap<String, String>> getFormData() {
        return super.getFormData()
                .map(Crypto::decrypt);
    }
}
