package com.xiao.core.webmagic.controller;

import com.xiao.base.BaseController;
import com.xiao.base.ResultModel;
import com.xiao.core.webmagic.domain.*;
import com.xiao.core.webmagic.service.impl.BianMaChaPageProcessor;
import com.xiao.logannotation.LoginRequired;
import com.xiao.util.IPUtil;
import com.xiao.util.RedisUtils;
import com.xiao.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/admin/bianmacha")
public class BianMaChaController extends BaseController {
    @Resource
    BianMaChaPageProcessor bianMaChaPageProcessor;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 到主页面
     *
     * @return
     */
    @LoginRequired(remark = "编码查爬取操作")
    @RequestMapping(value = "/toList")
    public ResultModel toList(String hashcode) {
        if (!StringUtil.isEmpty(hashcode) && hashcode.length() == 10) {
            if(!redisUtils.hasKey("bianmacha_"+hashcode)){
                bianMaChaPageProcessor.startbianmacha(hashcode);
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
            if(redisUtils.hasKey("bianmacha_"+hashcode)) {
                String isok= (String) redisUtils.get("bianmacha_"+hashcode);
                if("1".equals(isok)){
                    BianMaChaBsc bsc = (BianMaChaBsc) redisUtils.get("bianmacha_bsc_"+hashcode);
                    BianMaChaExp exp = (BianMaChaExp) redisUtils.get("bianmacha_exp_"+hashcode);
                    BianMaChaImp imp = (BianMaChaImp) redisUtils.get("bianmacha_imp_"+hashcode);
                    List<BianMaChaCase> list = (List<BianMaChaCase>) redisUtils.get("bianmacha_case_"+hashcode);
                    List<BianMaChaDst> outlist = (List<BianMaChaDst>) redisUtils.get("bianmacha_dst_out_"+hashcode);
                    List<BianMaChaDst> inlist= (List<BianMaChaDst>) redisUtils.get("bianmacha_dst_in_"+hashcode);
                    List<BianMaChaCmp> cmpoutlist = (List<BianMaChaCmp>) redisUtils.get("bianmacha_cmp_out_"+hashcode);
                    List<BianMaChaCmp> cmpinlist = (List<BianMaChaCmp>) redisUtils.get("bianmacha_cmp_in_"+hashcode);
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
            redisUtils.del("bianmacha_"+hashcode);
            redisUtils.del("bianmacha_bsc_"+hashcode);
            redisUtils.del("bianmacha_exp_"+hashcode);
            redisUtils.del("bianmacha_imp_"+hashcode);
            redisUtils.del("bianmacha_case_"+hashcode);
            redisUtils.del("bianmacha_dst_out_"+hashcode);
            redisUtils.del("bianmacha_dst_in_"+hashcode);
            redisUtils.del("bianmacha_cmp_out_"+hashcode);
            redisUtils.del("bianmacha_cmp_in_"+hashcode);
            return sendSuccessMessage("处理成功");
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
    @RequestMapping(value = "/getIpnice")
    public ResultModel getIpnice(String hashcode) throws Exception {
        for (int j =47; j <= 60; j++) {//取3页数据
            log.info("正在获取第" + j + "页数据");
            Document parse = Jsoup.parse(new URL("http://www.89ip.cn/index_" + j + ".html"), 5000);
            Elements trs = parse.select("table").select("tbody").select("tr");
            for (int i = 0; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td");
                String ip = tds.get(0).text().trim();
                String strport = tds.get(1).text().trim();
                int prot = Integer.parseInt(strport);
                if (!IPUtil.ifUseless(ip, prot)) {
                    String all=ip+":"+prot;
                    redisUtils.lSet("ip",all);
                }
            }
        }
        return sendSuccessMessage("处理成功");
    }

}
