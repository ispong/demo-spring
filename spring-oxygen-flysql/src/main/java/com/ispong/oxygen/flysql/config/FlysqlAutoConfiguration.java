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
package com.ispong.oxygen.flysql.config;

import com.ispong.oxygen.flysql.core.Flysql;
import com.ispong.oxygen.flysql.pojo.constant.FlysqlConstants;
import com.ispong.oxygen.flysql.pojo.properties.FlysqlDataSourceProperties;
import com.ispong.oxygen.flysql.success.SuccessControllerAdvice;
import com.ispong.oxygen.flysql.success.SuccessResponseAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动配置
 *
 * @author ispong
 * @since 0.0.1
 */
@Slf4j
@EnableConfigurationProperties(FlysqlDataSourceProperties.class)
public class FlysqlAutoConfiguration {

    @Bean
    @ConditionalOnClass(FlysqlAutoConfiguration.class)
    private SuccessResponseAdvice initSuccessAdvice() {

        return new SuccessResponseAdvice();
    }

    @Bean
    @ConditionalOnClass(FlysqlAutoConfiguration.class)
    private SuccessControllerAdvice initSuccessControllerAdvice() {

        return new SuccessControllerAdvice();
    }

    @Bean
    @ConditionalOnClass(FlysqlAutoConfiguration.class)
    private void initBanner() {
        log.debug("welcome to use oxygen-flysql");
        log.debug("                                           ______                 __");
        log.debug("  ____  _  ____  ______ ____  ____        / __/ /_  ___________ _/ /");
        log.debug(" / __ \\| |/_/ / / / __ `/ _ \\/ __ \\______/ /_/ / / / / ___/ __ `/ / ");
        log.debug("/ /_/ />  </ /_/ / /_/ /  __/ / / /_____/ __/ / /_/ (__  ) /_/ / /  ");
        log.debug("\\____/_/|_|\\__, /\\__, /\\___/_/ /_/     /_/ /_/\\__, /____/\\__, /_/   ");
        log.debug("          /____//____/                       /____/        /_/      ");
    }

    /**
     * 初始化多数据源
     *
     * @param flysqlDataSourceProperties 数据源yaml配置
     * @param jdbcTemplate               原生jdbcTemplate
     * @since 0.0.1
     */
    @Bean
    @ConditionalOnClass(FlysqlAutoConfiguration.class)
    private Flysql initFlySqlFactory(FlysqlDataSourceProperties flysqlDataSourceProperties, JdbcTemplate jdbcTemplate) {

        Map<String, JdbcTemplate> jdbcTemplateMap;
        Map<String, DataSourceProperties> dataSourcePropertiesMap = flysqlDataSourceProperties.getDatasource();
        if (dataSourcePropertiesMap == null) {
            jdbcTemplateMap = new HashMap<>(1);
        } else {
            jdbcTemplateMap = new HashMap<>(dataSourcePropertiesMap.size() + 1);
            dataSourcePropertiesMap.forEach((k, v) -> jdbcTemplateMap.put(k, new JdbcTemplate(v.initializeDataSourceBuilder().build())));
        }

        jdbcTemplateMap.put(FlysqlConstants.PRIMARY_DATASOURCE_NAME, jdbcTemplate);
        return new Flysql(jdbcTemplateMap);
    }

}
