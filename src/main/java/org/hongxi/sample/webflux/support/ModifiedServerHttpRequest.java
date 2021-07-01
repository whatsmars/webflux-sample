package org.hongxi.sample.webflux.support;

import io.netty.buffer.ByteBufAllocator;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenhongxi on 2021/4/29.
 */
public class ModifiedServerHttpRequest extends ServerHttpRequestDecorator {

    private final byte[] rawBody;

    public ModifiedServerHttpRequest(ServerHttpRequest delegate, byte[] rawBody) {
        super(delegate);
        this.rawBody = rawBody;
    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        Map<String, List<String>> targetMap = new HashMap<>();
        MultiValueMap<String, String> all = new MultiValueMapAdapter<>(targetMap);
        all.addAll(super.getQueryParams());
        Map<String, Object> params = JacksonUtils.deserialize(this.rawBody, Map.class);
        params.forEach((k, v) -> {
            if (v != null) {
                all.add(k, v.toString());
            }
        });
        return all;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        return Flux.just(nettyDataBufferFactory.wrap(this.rawBody));
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = HttpHeaders.writableHttpHeaders(super.getHeaders());
        headers.setContentLength(this.rawBody.length);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * @return body json string
     */
    public String bodyString() {
        return new String(rawBody, StandardCharsets.UTF_8);
    }
}
