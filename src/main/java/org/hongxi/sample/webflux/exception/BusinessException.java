package org.hongxi.sample.webflux.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by shenhongxi on 2020/8/16.
 */
public class BusinessException extends RuntimeException {

    @Getter
    @Setter
    private int code;
    @Getter
    @Setter
    private String msg;

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
