package com.xiao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 简单限流工具
 * 基于 IP + 时间窗口
 */
@Component
public class RateLimiter {
    private static final Logger log = LoggerFactory.getLogger(RateLimiter.class);

    private static final int WINDOW_SECONDS = 60;
    private static final int MAX_REQUESTS_PER_WINDOW = 10;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 检查请求是否超过限制
     *
     * @param request HTTP 请求
     * @param action 操作标识（用于 Redis key）
     * @return true = 超过限制，false = 未超过
     */
    public boolean isLimited(HttpServletRequest request, String action) {
        String ip = getClientIp(request);
        String key = "rate_limit:" + action + ":" + ip;

        try {
            Long currentCount = redisUtils.incr(key, 1);

            if (currentCount == 1) {
                // 首次请求，设置过期时间
                redisUtils.set(key, 1, WINDOW_SECONDS);
            }

            if (currentCount > MAX_REQUESTS_PER_WINDOW) {
                log.warn("Rate limit exceeded - IP: {}, action: {}, count: {}", ip, action, currentCount);
                return true;
            }

            return false;
        } catch (Exception e) {
            log.error("Rate limiter error", e);
            // 失败时允许通过，避免影响正常业务
            return false;
        }
    }

    /**
     * 获取客户端真实 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
