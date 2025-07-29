package com.xiao.core.webmagic.controller;

import com.xiao.base.BaseController;
import com.xiao.base.ResultModel;
import com.xiao.core.webmagic.domain.*;
import com.xiao.core.webmagic.service.impl.AreaPageProcessor;
import com.xiao.logannotation.LoginRequired;
import com.xiao.util.RedisUtils;
import com.xiao.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/areahscode")
public class AreahscodeController extends BaseController {
    @Resource
    AreaPageProcessor areaPageProcessor;

    @Autowired
    RedisUtils redisUtils;
    /**
     * 到主页面
     *
     * @return
     */
    @LoginRequired(remark = "编码查爬取操作")
    @RequestMapping(value = "/toList",method={RequestMethod.POST,RequestMethod.GET})
    public ResultModel toList(String hashcode) {
        if (!StringUtil.isEmpty(hashcode) && hashcode.length() == 10) {
            if(!redisUtils.hasKey("365area_"+hashcode)){
                areaPageProcessor.start(hashcode);
            }
            return sendSuccessMessage("处理成功");
        } else {
            return sendFailureMessage("hashcode不能为空,或者不为10位hashcode");
        }
    }


    /**
     * 数据展现
     *
     * @return
     */
    @LoginRequired(remark = "编码查查询redis操作")
    @RequestMapping(value = "/getData")
    public ResultModel getData(String hashcode) {
        if (!StringUtil.isEmpty(hashcode) && hashcode.length() == 10) {
            if(redisUtils.hasKey("365area_"+hashcode)) {
                String isok= (String) redisUtils.get("365area_"+hashcode);
                if("1".equals(isok)){
                    BianMaChaBsc bsc = (BianMaChaBsc) redisUtils.get("365area_bsc_"+hashcode);
                    BianMaChaExp exp = (BianMaChaExp) redisUtils.get("365area_exp_"+hashcode);
                    BianMaChaImp imp = (BianMaChaImp) redisUtils.get("365area_imp_"+hashcode);
                    List<BianMaChaCase> list =new ArrayList<>();
                    List<BianMaChaDst> outlist =new ArrayList<>();
                    List<BianMaChaDst> inlist=new ArrayList<>();
                    List<BianMaChaCmp> cmpoutlist =new ArrayList<>();
                    List<BianMaChaCmp> cmpinlist =new ArrayList<>();
                    ResultModel rm=new ResultModel();
                    rm.setMsg("处理成功");
                    rm.setSuccess(true);
                    rm.putData("bsc",bsc);
                    rm.putData("exp",exp);
                    rm.putData("imp",imp);
                    rm.putData("caselist",list);
                    rm.putData("dstoutlist",outlist);
                    rm.putData("dstinlist",inlist);
                    rm.putData("cmpoutlist",cmpoutlist);
                    rm.putData("cmpinlist",cmpinlist);
                    return rm;
                }
            }
            ResultModel rm=new ResultModel();
            rm.setMsg("处理成功");
            rm.setErrcode("2");
            rm.setSuccess(true);
            return rm;
        } else {
            return sendFailureMessage("hashcode不能为空,或者不为10位hashcode");
        }
    }


    /**
     * 删除redis数据
     *
     * @return
     */
    @LoginRequired(remark = "编码查查询redis操作")
    @RequestMapping(value = "/deletedata")
    public ResultModel deletedata(String hashcode) {
        if (!StringUtil.isEmpty(hashcode) && hashcode.length() == 10) {
            redisUtils.del("365area_"+hashcode);
            redisUtils.del("365area_bsc_"+hashcode);
            redisUtils.del("365area_exp_"+hashcode);
            redisUtils.del("365area_imp_"+hashcode);
            return sendSuccessMessage("处理成功");
        } else {
            return sendFailureMessage("hashcode不能为空,或者不为10位hashcode");
        }
    }


}
