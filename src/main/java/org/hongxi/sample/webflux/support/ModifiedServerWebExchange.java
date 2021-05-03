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

    private final Map<String, Object> body;

    public ModifiedServerWebExchange(ServerWebExchange delegate, Map<String, Object> body) {
        super(delegate);
        this.body = body;
    }

    @Override
    public Mono<MultiValueMap<String, String>> getFormData() {
        Map<String, List<String>> target = toListMap(body);
        return Mono.just(new MultiValueMapAdapter<>(target));
    }

    private Map<String, List<String>> toListMap(Map<String, Object> body) {
        Map<String, List<String>> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            String key = entry.getKey();
            int index = entry.getKey().indexOf("[");
            if (index > 0) {
                key = entry.getKey().substring(0, index);
            }
            if (map.containsKey(key)) {
                map.get(key).add(entry.getValue().toString());
            } else {
                List<String> list = new ArrayList<>();
                list.add(entry.getValue().toString());
                map.put(key, list);
            }
        }
        return map;
    }
}
