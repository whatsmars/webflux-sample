package org.hongxi.sample.webflux.support;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Mono;

/**
 * Created by shenhongxi on 2021/4/29.
 */
public class SimpleServerHttpResponseDecorator extends ServerHttpResponseDecorator {

    private Crypto crypto;

    public SimpleServerHttpResponseDecorator(ServerHttpResponse delegate, Crypto crypto) {
        super(delegate);
        this.crypto = crypto;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

        return super.writeWith(body);
    }
}
