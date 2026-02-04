package com.xiao.tokenmagnager;

import com.xiao.constans.Constans;
import com.xiao.util.RedisUtils;
import com.xiao.util.StringUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisTokenUtils implements TokenManager {

	@Autowired
	RedisUtils redisUtils;

	@Override
	public String createToken(String member_id) {
		// TODO Auto-generated method stub
		if(StringUtil.isEmpty(member_id)){
			return "";
		}
		//利用tokenutils生成token
		String token= TokenUtils.createJwtToken(member_id);
		//存储到redis并设置过期时间
		boolean flag=redisUtils.set(member_id, token, Constans.TOKEN_EXPIRED_TIME6);
		if(!flag){
			token="";
		}
		return token;
	}

	@Override
	public boolean checkToken(String jwt,String member_id) {
		Claims claims= TokenUtils.parseJWT(jwt);
		if(claims!=null){
			//如果redis存的member_id，token跟他不匹配则返回fasle
			if(jwt.equals(redisUtils.get(member_id))){
				redisUtils.set(member_id, jwt, Constans.TOKEN_EXPIRED_TIME);
				return true;
			}
		}
		return false;
	}

	@Override
	public String getToken(String jwt) {
		// TODO Auto-generated method stub
		String member_id = "";
		try {
			Claims claims= TokenUtils.parseJWT(jwt);
			member_id=claims.getId();
			if(!redisUtils.hasKey(member_id)){
				member_id="";
			}
		}catch (Exception e){

		}
		return member_id;
	}

	@Override
	public void deleteToken(String member_id) {
		// TODO Auto-generated method stub
		redisUtils.del(member_id);
	}


}
