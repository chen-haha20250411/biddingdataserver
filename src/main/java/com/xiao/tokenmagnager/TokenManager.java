package com.xiao.tokenmagnager;

public interface TokenManager {
	/**
     * 创建一个token关联上指定用户
     * @param userId 指定用户的id
     * @return 生成的token
     */
    public String createToken(String member_id);

    /**
     * 检查token是否有效
     * @param model token
     * @return 是否有效
     */
    public boolean checkToken(String jwt, String member_id);

    /**
     * 从字符串中解析token
     * @param authentication 加密后的字符串
     * @return
     */
    public String getToken(String authentication);

    /**
     * 清除token
     * @param userId 登录用户的id
     */
    public void deleteToken(String member_id);

}
