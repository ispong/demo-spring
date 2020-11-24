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
package com.isxcode.oxygen.freecode.utils;

import com.isxcode.oxygen.core.exception.OxygenException;
import com.isxcode.oxygen.core.freemarker.FreemarkerUtils;
import com.isxcode.oxygen.freecode.pojo.entity.TableColumnInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

/**
 * freecode工具类
 *
 * @author ispong
 * @since 0.0.1
 */
@Component
@Slf4j
public class FreecodeUtils {

    private static FreemarkerUtils freemarkerUtils;

    public FreecodeUtils(FreemarkerUtils freemarkerUtils) {

        FreecodeUtils.freemarkerUtils = freemarkerUtils;
    }

    /**
     * 生成文件
     *
     * @param fileName     文件名
     * @param templateName 模板名
     * @param freecodeInfo 对象
     * @param modulePath   文件路径
     * @throws IOException 生成文件异常
     * @since 0.0.1
     */
    public static void generateFile(String modulePath, String fileName, String templateName, Object freecodeInfo) throws IOException {

        // 生成文件夹
        modulePath = ResourceUtils.getURL(modulePath).getPath().substring(1);
        if (!Files.exists(Paths.get(modulePath)) && !Files.isDirectory(Paths.get(modulePath))) {
            Files.createDirectory(Paths.get(modulePath));
        }

        // 生成文件
        String filePath = modulePath + "/" + fileName;
        if (!Files.exists(Paths.get(filePath))) {
            try {
                freemarkerUtils.generateToFile(templateName, freecodeInfo, filePath);
            } catch (OxygenException e) {
                log.debug("freecode generate file error->" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 首字母大写
     *
     * @param data 需要转的string
     * @return string
     * @since 0.0.1
     */
    public static String upperFirstCase(String data) {

        return data.substring(0, 1).toUpperCase() + data.substring(1);
    }

    /**
     * 下划线转小驼峰
     *
     * @param lineStr 包含下滑线字符串
     * @return 下划线写法
     * @since 2019-12-24
     */
    public static String lineToHump(String lineStr) {

        StringBuffer humpStrBuff = new StringBuffer();
        lineStr = lineStr.toLowerCase();
        Matcher matcher = compile("_(\\w)").matcher(lineStr);
        while (matcher.find()) {
            matcher.appendReplacement(humpStrBuff, matcher.group(1).toUpperCase());
        }
        return matcher.appendTail(humpStrBuff).toString();
    }

    /**
     * 将数据库类型转换成java的类型
     *
     * @param dataType 数据库类型
     * @return java类型
     * @since 2020-01-09
     */
    public static String parseDataType(String dataType) {

        // 去掉括号，包括括号内的值
        // Example: varchar(200) --> varchar
        Pattern pattern = compile("\\(.*?\\)");
        String dataStr = pattern.matcher(dataType).replaceAll("");

        // 匹配全小写的数据库字段
        switch (dataStr.toLowerCase()) {
            case "int":
            case "integer":
                return "Integer";
            case "bigint":
                return "Long";
            case "datetime":
            case "timestamp":
                return "LocalDateTime";
            case "date":
                return "LocalDate";
            case "double":
                return "Double";
            case "decimal":
                return "BigDecimal";
            default:
                return "String";
        }
    }

    /**
     * 生成需要导入的jar包
     *
     * @param fieldList 数据库字段列表
     * @return 返回需要导入的包
     * @since 2020-01-09
     */
    public static List<String> parseDataPackage(List<TableColumnInfo> fieldList) {

        List<String> packages = new ArrayList<>();
        for (TableColumnInfo metaColumn : fieldList) {

            switch (metaColumn.getType()) {
                case "LocalDateTime":
                    packages.add("java.time.LocalDateTime");
                    break;
                case "LocalDate":
                    packages.add("java.time.LocalDate");
                    break;
                case "BigDecimal":
                    packages.add("java.math.BigDecimal");
                    break;
                case "Long":
                    packages.add("java.lang.Long");
                    break;
                default:
            }
        }
        return packages.stream().distinct().collect(Collectors.toList());
    }

}

