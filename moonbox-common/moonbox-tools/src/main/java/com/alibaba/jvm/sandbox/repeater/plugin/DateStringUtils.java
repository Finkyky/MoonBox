package com.alibaba.jvm.sandbox.repeater.plugin;

import java.time.LocalDateTime;

public class DateStringUtils {

    private static String numPrefixPattern = "^[0-9]{6,17}$";

    public static boolean isDateString(String str) {
        if (str == null) {
            return false;
        }
        if (str.startsWith("202") && str.contains("T")) {  //2022-04-19T11:46:52.864
            try {
                LocalDateTime.parse(str);
                return true;
            } catch (Exception e) {
            }
        }
        str = str.replaceAll(":|/|-|年|月|日| |", "");
        if (!str.matches(numPrefixPattern)) {
            return false;
        }
        Integer year = Integer.valueOf(str.substring(0, 4));
        if (year < 2020 || year > 2025) {
            return false;
        }
        Integer month = Integer.valueOf(str.substring(4, 6));
        if (month < 1 || month > 12) {
            return false;
        }
        if (str.length() >= 8) {
            Integer day = Integer.valueOf(str.substring(6, 8));
            if (day < 1 || day > 31) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDateLong(Long data) {
        return data != null && isDateString(data.toString());
    }


    /**
     * 匹配包含2022/12/02 09:34:36时间串;
     */
    private static String DATETIME_REGEX1 ="202[2-9](\\-|\\/|.)[0-1]{1}[0-9]{1}(\\-|\\/|.)[0-3]{1}[0-9]{1} [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}";
    /**
     * 匹配包含20221202093436这种类型时间串;
     */
    private static String DATETIME_REGEX2 ="202[2-9][0-1]{1}[0-9]{1}[0-3]{1}[0-9]{1}[0-2]{1}[0-9]{1}[0-5]{1}[0-9]{1}[0-5]{1}[0-9]{1}";

    /**
     * 解析来自requestHeaders的date
     * [Accept: application/json, User-Agent: volc-sdk-java/v1.0.10, Content-Type: application/json, Host: open.volcengineapi.com, X-Date: 20220420T031448Z, X-Content-Sha256: 9c53a29b036fd2c20af26f63f0f01a3b3ba4fa9e68570d2fd915befa8e52ea74, Authorization: HMAC-SHA256 Credential=AKLTZDhmOGVmMzA5N2M3NDU1M2JmMjVhNzZiNWFmODQ4NTU/20220420/cn-north-1/CreativeManagementPlatform/request, SignedHeaders=content-type;host;x-content-sha256;x-date, Signature=31b01e5964485672b86ee0b80b4b64c7ce4d51a9e3314e48bcf97df7f7442ec7]
     */
    private static String DATETIME_REGEX3 ="202[2-9][0-1]{1}[0-9]{1}[0-3]{1}[0-9]{1}T[0-2]{1}[0-9]{1}[0-5]{1}[0-9]{1}[0-5]{1}[0-9]{1}Z";

    /**
     * 将字符串里面时间给过滤掉....
     * @param data 要过滤字符串
     * @return 过滤结果
     */
    public static String replaceDateTimeToTips(String data) {
        return data.replaceAll(DATETIME_REGEX1,"时间自动匹配").replaceAll(DATETIME_REGEX2,"时间自动匹配").replaceAll(DATETIME_REGEX3,"时间自动匹配");
    }
}
