package com.isxcode.ispring.wechatgo;

import com.isxcode.ispring.wechatgo.model.WeChatAppInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信配置信息
 *
 * @author ispong
 * @version v0.1.0
 * @date 2020-01-14
 */
@Data
@Component
@ConfigurationProperties(prefix = "isxcode.we-chat")
public class WeChatProperties {

    /**
     * 临时储存token
     */
    public static final Map<String, String> WE_CHAT_ACCESS_TOKENS = new HashMap<>();

    /**
     * 所有的配置信息
     */
    private Map<String, WeChatAppInfo> apps;

}
