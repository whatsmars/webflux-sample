package org.hongxi.sample.webflux.support;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenhongxi on 2021/4/29.
 */
@Component
public class Crypto {

    /**
     * 模拟解密逻辑：添加一个请求参数 start
     * @param params
     * @return
     */
    public Map<String, Object> decrypt(Map<String, Object> params) {
        Map<String, Object> decrypted = new HashMap<>(params);
        decrypted.put("start", System.currentTimeMillis());
        return decrypted;
    }

    /**
     * 模拟加密逻辑：添加一个响应参数 end
     * @param params
     * @return
     */
    public Map<String, Object> encrypt(Map<String, Object> params) {
        params.remove("end");
        return params;
    }
}
