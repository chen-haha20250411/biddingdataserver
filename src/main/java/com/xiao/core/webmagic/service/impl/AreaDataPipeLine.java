package com.xiao.core.webmagic.service.impl;

import com.xiao.core.webmagic.domain.BianMaChaBsc;
import com.xiao.core.webmagic.domain.BianMaChaExp;
import com.xiao.core.webmagic.domain.BianMaChaImp;
import com.xiao.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class AreaDataPipeLine implements Pipeline {
    @Autowired
    RedisUtils redisUtils;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String hashcode =  resultItems.getRequest().getUrl().substring(resultItems.getRequest().getUrl().lastIndexOf("/")).split("\\.")[0].replace("/","");

        String isok=resultItems.get("365area_" + hashcode);
        redisUtils.set("365area_" + hashcode, isok);

        BianMaChaBsc bsc =resultItems.get("365area_bsc_" + hashcode);
        if(bsc!=null){
            redisUtils.set("365area_bsc_" + hashcode, bsc);
        }

        BianMaChaExp exp = resultItems.get("365area_exp_" + hashcode);
        if(exp!=null){
            redisUtils.set("365area_exp_" + hashcode, exp);
        }

        BianMaChaImp imp = resultItems.get("365area_imp_" + hashcode);
        if(imp!=null){
            redisUtils.set("365area_imp_" + hashcode, imp);
        }

    }
}
