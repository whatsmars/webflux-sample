package org.hongxi.sample.webflux.support;

import io.netty.buffer.ByteBufAllocator;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Created by shenhongxi on 2021/4/29.
 */
public class ModifiedServerHttpRequest extends ServerHttpRequestDecorator {

    private Map<String, Object> params;

    private Flux<DataBuffer> body;

    private long contentLength;

    public ModifiedServerHttpRequest(ServerHttpRequest delegate, Map<String, Object> params) {
        super(delegate);
        this.params = params;

        byte[] bytes = JacksonUtils.serialize(params);
        contentLength = bytes.length;

        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        body = Flux.just(buffer);
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        // 必须 new，不能直接操作 super.getHeaders()（readonly）
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(super.getHeaders());
        headers.setContentLength(contentLength);
        return headers;
    }

    /**
     * @return body json string
     */
    public String bodyString() {
        return JacksonUtils.toJson(this.params);
    }
}
