/*
 * Copyright [2020] [ispong]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ispong.oxygen.wechatgo.cache;

import com.ispong.oxygen.wechatgo.pojo.constant.WechatgoConstants;
import com.ispong.oxygen.wechatgo.service.WechatgoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Wechatgo Token Thread
 *
 * @author ispong
 * @since 0.0.1
 */
@Slf4j
public class WechatgoTokenThread implements Runnable {

    private final WechatgoService wechatgoService;

    private final StringRedisTemplate stringRedisTemplate;

    public WechatgoTokenThread(StringRedisTemplate stringRedisTemplate,
                               WechatgoService wechatgoService) {

        this.stringRedisTemplate = stringRedisTemplate;
        this.wechatgoService = wechatgoService;
    }

    @Override
    public void run() {
        log.debug("generate wechatgo token");
//        wechatgoTokenCache.cacheToken(WechatgoConstants.ENV, wechatgoService.getAccessTokenBody().getAccessToken());
        String accessToken = wechatgoService.getAccessTokenBody().getAccessToken();
        stringRedisTemplate.opsForValue().set(WechatgoConstants.WX_OFFICIAL_TOKEN, accessToken);
    }
}
