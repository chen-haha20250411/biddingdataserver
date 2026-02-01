package com.xiao.util;

import com.xiao.base.Page;
import com.xiao.base.ResultModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页工具类 - 用于适配 vue-element-admin 分页格式
 */
public class PageUtils {

    /**
     * 将 Page 对象转换为 vue-element-admin 兼容的分页格式
     * vue-element-admin 期望格式: { total: 100, items: [...] }
     */
    public static Map<String, Object> toVuePage(Page<?> page) {
        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("items", page.getList());
        result.put("current", page.getCurrent());
        result.put("pageSize", page.getLimit());
        return result;
    }

    /**
     * 创建成功分页响应
     */
    public static ResultModel pageSuccess(Page<?> page) {
        return ResultModel.success(toVuePage(page));
    }

    /**
     * 创建成功分页响应（自定义消息）
     */
    public static ResultModel pageSuccess(String message, Page<?> page) {
        return ResultModel.success(message, toVuePage(page));
    }

    /**
     * 将列表转换为分页格式（非分页查询）
     */
    public static Map<String, Object> toVueList(Object list) {
        Map<String, Object> result = new HashMap<>();
        if (list instanceof java.util.List) {
            result.put("total", ((java.util.List<?>) list).size());
            result.put("items", list);
        } else {
            result.put("total", 1);
            result.put("items", list);
        }
        return result;
    }

    /**
     * 创建列表成功响应
     */
    public static ResultModel listSuccess(Object list) {
        return ResultModel.success(toVueList(list));
    }
}