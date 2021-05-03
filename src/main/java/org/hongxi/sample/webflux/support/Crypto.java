package org.hongxi.sample.webflux.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenhongxi on 2021/4/29.
 */
public class Crypto {

    /**
     * 模拟解密逻辑：添加一个请求参数 start
     * @param params
     * @return
     */
    public static Map<String, Object> decrypt(Map<String, Object> params) {
        Map<String, Object> decrypted = new HashMap<>(params);
        decrypted.put("start", System.currentTimeMillis());
        return decrypted;
    }

    /**
     * 模拟加密逻辑：添加一个响应参数 end
     * @param data
     * @return
     */
    public static byte[] encrypt(byte[] data) {
        Map<String, Object> result = JacksonUtils.deserialize(data, Map.class);
        result.put("end", System.currentTimeMillis());
        return JacksonUtils.serialize(result);
    }
}
