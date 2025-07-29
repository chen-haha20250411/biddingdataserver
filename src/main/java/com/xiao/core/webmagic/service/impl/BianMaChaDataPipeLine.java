package com.xiao.core.webmagic.service.impl;

import com.xiao.core.webmagic.domain.*;
import com.xiao.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class BianMaChaDataPipeLine implements Pipeline {
    @Autowired
    RedisUtils redisUtils;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String hashcode =  resultItems.getRequest().getUrl().split("\\_")[1].split("\\.")[0];

        String isok=resultItems.get("bianmacha_" + hashcode);
        redisUtils.set("bianmacha_" + hashcode, isok);

        BianMaChaBsc bsc =resultItems.get("bianmacha_bsc_" + hashcode);
        if(bsc!=null){
            redisUtils.set("bianmacha_bsc_" + hashcode, bsc);
        }

        BianMaChaExp exp = resultItems.get("bianmacha_exp_" + hashcode);
        if(exp!=null){
            redisUtils.set("bianmacha_exp_" + hashcode, exp);
        }

        BianMaChaImp imp = resultItems.get("bianmacha_imp_" + hashcode);
        if(imp!=null){
            redisUtils.set("bianmacha_imp_" + hashcode, imp);
        }

        List<BianMaChaCase> list =resultItems.get("bianmacha_case_" + hashcode);
        if(list!=null&&list.size()>0){
            redisUtils.set("bianmacha_case_" + hashcode, list);
        }

        List<BianMaChaDst> outlist = resultItems.get("bianmacha_dst_out_" + hashcode);
        if(outlist!=null&&outlist.size()>0){
            redisUtils.set("bianmacha_dst_out_" + hashcode, outlist);
        }

        List<BianMaChaDst> inlist = resultItems.get("bianmacha_dst_in_" + hashcode);
        if(inlist!=null&&inlist.size()>0){
            redisUtils.set("bianmacha_dst_in_" + hashcode, inlist);
        }

        List<BianMaChaCmp> cmpoutlist =resultItems.get("bianmacha_cmp_out_" + hashcode);
        if(cmpoutlist!=null&&cmpoutlist.size()>0){
            redisUtils.set("bianmacha_cmp_out_" + hashcode, cmpoutlist);
        }

        List<BianMaChaCmp> cmpinlist =resultItems.get("bianmacha_cmp_in_" + hashcode);
        if(cmpinlist!=null&&cmpinlist.size()>0){
            redisUtils.set("bianmacha_cmp_in_" + hashcode, cmpinlist);
        }
    }
}
