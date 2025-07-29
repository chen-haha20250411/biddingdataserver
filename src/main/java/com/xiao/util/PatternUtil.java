package com.xiao.util;

import java.util.regex.Pattern;

public class PatternUtil {
    //判断excel是否为xls还是xlsx
    public static Pattern EXCEL_PATTERN = Pattern.compile("[.]xls|xlsx$");
    //数字
    public static Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    //对字符转义
    public static Pattern ESCAPESTRING__PATTERN = Pattern.compile("^\\t|\\r|\\n");
}
