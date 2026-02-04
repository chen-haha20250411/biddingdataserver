package com.xiao.core.webmagic.service.impl;

import com.xiao.core.webmagic.domain.*;
import com.xiao.util.Html2Text;
import com.xiao.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BianMaChaPageProcessor implements PageProcessor {
    protected static Logger log = LoggerFactory.getLogger(BianMaChaPageProcessor.class);

    @Autowired
    private BianMaChaDataPipeLine dataPipeLine;

    @Autowired
    RedisUtils redisUtils;

    /**
     * user agent array
     */
    public static final String[] HEADERS = {
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Acoo Browser; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; .NET CLR 3.0.04506)",
            "Mozilla/4.0 (compatible; MSIE 7.0; AOL 9.5; AOLBuild 4337.35; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
            "Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 2.0.50727; Media Center PC 6.0)",
            "Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 1.0.3705; .NET CLR 1.1.4322)",
            "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 5.2; .NET CLR 1.1.4322; .NET CLR 2.0.50727; InfoPath.2; .NET CLR 3.0.04506.30)",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN) AppleWebKit/523.15 (KHTML, like Gecko, Safari/419.3) Arora/0.3 (Change: 287 c9dfb30)",
            "Mozilla/5.0 (X11; U; Linux; en-US) AppleWebKit/527+ (KHTML, like Gecko, Safari/419.3) Arora/0.6",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.2pre) Gecko/20070215 K-Ninja/2.1.1",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/20080705 Firefox/3.0 Kapiko/3.0",
            "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5",
            "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.8) Gecko Fedora/1.9.0.8-1.fc10 Kazehakase/0.5.6",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.20 (KHTML, like Gecko) Chrome/19.0.1036.7 Safari/535.20",
            "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER)",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 LBBROWSER",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; 360SE)",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)",
            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
            "Mozilla/5.0 (iPad; U; CPU OS 4_2_1 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5",
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:2.0b13pre) Gecko/20110307 Firefox/4.0b13pre",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:16.0) Gecko/20100101 Firefox/16.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
            "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10"
    };

    /**
     * get random headers
     *
     * @return
     */
    public static String randomHeader() {
        int random = (int) (Math.random() * HEADERS.length);
        return random == HEADERS.length ? HEADERS[random - 1] : HEADERS[random];
    }


    private Site site = Site
            .me()
            .setCycleRetryTimes(5)
            .setRetryTimes(5)
            .setSleepTime(100)
            .setTimeOut(3 * 60 * 1000)
            .setCharset("UTF-8")
            .setUserAgent(randomHeader())
            .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .addHeader("Accept-Encoding", "gzip, deflate")
            .addHeader("Connection", "keep-alive")
            .addHeader("Referer", "http://hs.bianmachaxun.com/")
            .addHeader("Host", "hs.bianmachaxun.com");

    @Override
    public void process(Page page) {
        String hashcode = page.getUrl().toString().split("\\_")[1].split("\\.")[0];
        List<String> tableallout = page.getHtml().xpath("//div[@class='layui-tab-item']").all();
        if (tableallout != null && tableallout.size() > 0) {
            handlebsc(page, hashcode);
            handleexp(page, hashcode);
            handleimp(page, hashcode);
            handlecase(page, hashcode);
            handlecmpin(page, hashcode);
            handlecmpout(page, hashcode);
            handledstin(page, hashcode);
            handledstout(page, hashcode);
            page.putField("bianmacha_" + hashcode, "1");
        } else {
            page.putField("bianmacha_" + hashcode, "2");
        }
    }

    //处理基本信息
    public void handlebsc(Page page, String hashcode) {
        try {
            log.info("hashcode" + hashcode + "开始处理基本信息");
            //HS编码:
            String td1 = page.getHtml().xpath("//div[@class='layui-tab-item'][1]/table[@class='layui-table']/tbody/tr[1]/td[2]/tidyText()").toString();
            String hcode = td1.split("\\ ")[0];
            //中文描述:
            String td2 = page.getHtml().xpath("//div[@class='layui-tab-item'][1]/table[@class='layui-table']/tbody/tr[2]/td[2]/tidyText()").toString();
            //CIQ代码:
            String td3 = page.getHtml().xpath("//div[@class='layui-tab-item'][1]/table[@class='layui-table']/tbody/tr[3]/td[2]/tidyText()").toString();
            //英文描述:
            String td4 = page.getHtml().xpath("//div[@class='layui-tab-item'][1]/table[@class='layui-table']/tbody/tr[4]/td[2]/tidyText()").toString();
            //申报要素:
            String td5 = page.getHtml().xpath("//div[@class='layui-tab-item'][1]/table[@class='layui-table']/tbody/tr[5]/td[2]/tidyText()").toString();
            //申报要素举例:
            String back = page.getHtml().xpath("//div[@class='layui-tab-item'][1]/table[@class='layui-table']/tbody/tr[6]/td[2]/tidyText()").toString();
            String td6=Html2Text.htmlTotext(back).replace("【查看更多实例】","");
            //单位:
            String td7 = page.getHtml().xpath("//div[@class='layui-tab-item'][1]/table[@class='layui-table']/tbody/tr[7]/td[2]/tidyText()").toString();
            //监管条件:
            String td8 = page.getHtml().xpath("//div[@class='layui-tab-item'][1]/table[@class='layui-table']/tbody/tr[8]/td[2]/tidyText()").toString();
            //检疫条件:
            String td9 = page.getHtml().xpath("//div[@class='layui-tab-item'][1]/table[@class='layui-table']/tbody/tr[9]/td[2]/tidyText()").toString();

            BianMaChaBsc bsc = new BianMaChaBsc();
            bsc.setHscode(hcode);
            bsc.setCndesc(td2);
            bsc.setCiqcode(td3);
            bsc.setEndesc(td4);
            bsc.setDeclelements(td5);
            bsc.setDeclelementsexp(td6);
            bsc.setDescunit(td7);
            bsc.setMonitoringCondit(td8);
            bsc.setQuarantineCondit(td9);
            if (bsc != null) {
                page.putField("bianmacha_bsc_" + hashcode, bsc);
            }
            log.info("hashcode" + hashcode + "结束处理基本信息");
        } catch (Exception ex) {
            log.error("查询失败" + ex.getMessage());
        }
    }

    //处理出口税率
    public void handleexp(Page page, String hashcode) {
        try {
            log.info("hashcode" + hashcode + "开始处理出口税率");
            //出口退税:
            String td1 = page.getHtml().xpath("//div[@class='layui-tab-item'][2]/table[@class='layui-table']/tbody/tr[1]/td[2]/tidyText()").toString();
            //出口关税:
            String td2 = page.getHtml().xpath("//div[@class='layui-tab-item'][2]/table[@class='layui-table']/tbody/tr[2]/td[2]/tidyText()").toString();
            //出口增值税:
            String td3 = page.getHtml().xpath("//div[@class='layui-tab-item'][2]/table[@class='layui-table']/tbody/tr[3]/td[2]/tidyText()").toString();
            //临时税率:
            String td4 = page.getHtml().xpath("//div[@class='layui-tab-item'][2]/table[@class='layui-table']/tbody/tr[4]/td[2]/tidyText()").toString();
            //备注
            String td5 = page.getHtml().xpath("//div[@class='layui-tab-item'][2]/table[@class='layui-table']/tbody/tr[5]/td[2]/tidyText()").toString();

            BianMaChaExp exp = new BianMaChaExp();
            exp.setExportTaxRebate(td1);
            exp.setExportDuties(td2);
            exp.setExportVAT(td3);
            exp.setTemporaryTaxRate(td4);
            exp.setRemark(td5);
            if (exp != null) {
                page.putField("bianmacha_exp_" + hashcode, exp);
            }
            log.info("hashcode" + hashcode + "结束处理出口税率");
        } catch (Exception ex) {
            log.error("查询失败" + ex.getMessage());
        }
    }

    //处理进口税率
    public void handleimp(Page page, String hashcode) {
        try {
            log.info("hashcode" + hashcode + "开始处理进口税率");
            //最惠国进口税率
            String td1 = page.getHtml().xpath("//div[@class='layui-tab-item'][3]/table[@class='layui-table']/tbody/tr[1]/td[2]/tidyText()").toString();
            //普通进口税率:
            String td2 = page.getHtml().xpath("//div[@class='layui-tab-item'][3]/table[@class='layui-table']/tbody/tr[2]/td[2]/tidyText()").toString();
            //暂定进口税率
            String td3 = page.getHtml().xpath("//div[@class='layui-tab-item'][3]/table[@class='layui-table']/tbody/tr[3]/td[2]/tidyText()").toString();
            //进口消费税:
            String td4 = page.getHtml().xpath("//div[@class='layui-tab-item'][3]/table[@class='layui-table']/tbody/tr[4]/td[2]/tidyText()").toString();
            //进口增值税:
            String td5 = page.getHtml().xpath("//div[@class='layui-tab-item'][3]/table[@class='layui-table']/tbody/tr[5]/td[2]/tidyText()").toString();
            //进口其他税:
            String back = page.getHtml().xpath("//div[@class='layui-tab-item'][3]/table[@class='layui-table']/tbody/tr[6]/td[2]/tidyText()").toString();
            String td6=Html2Text.htmlTotext(back);
            //美国进口关税:
            String td7 = page.getHtml().xpath("//div[@class='layui-tab-item'][3]/table[@class='layui-table']/tbody/tr[7]/td[2]/tidyText()").toString();
            //备注
            String td8 = page.getHtml().xpath("//div[@class='layui-tab-item'][3]/table[@class='layui-table']/tbody/tr[8]/td[2]/tidyText()").toString();

            BianMaChaImp imp = new BianMaChaImp();
            imp.setImportTaxRate(td1);
            imp.setGeneralImportTaxRate(td2);
            imp.setProvisionalImportTaxRate(td3);
            imp.setImportConsumptionTax(td4);
            imp.setImportValueAddedTax(td5);
            imp.setOtherImportTaxes(td6);
            imp.setUsaImportTariffs(td7);
            imp.setRemark(td8);
            if (imp != null) {
                page.putField("bianmacha_imp_" + hashcode, imp);
            }
            log.info("hashcode" + hashcode + "结束处理进口税率");
        } catch (Exception ex) {
            log.error("查询失败" + ex.getMessage());
        }
    }

    //申报实例
    public void handlecase(Page page, String hashcode) {
        try {
            log.info("hashcode" + hashcode + "开始处理申报实例");
            List<String> tableall = page.getHtml().xpath("//div[@class='layui-tab-item'][4]/table[@class='layui-table']/tbody/tr").all();
            List<BianMaChaCase> list = new ArrayList<BianMaChaCase>();
            if (tableall.size() < 2) {
                return;
            }
            for (int i = 1; i <= tableall.size(); i++) {
                if (i != 1) {
                    String td1 = page.getHtml().xpath("//div[@class='layui-tab-item'][4]/table[@class='layui-table']/tbody/tr[" + i + "]/td[1]/tidyText()").toString();
                    String td2 = page.getHtml().xpath("//div[@class='layui-tab-item'][4]/table[@class='layui-table']/tbody/tr[" + i + "]/td[2]/tidyText()").toString();
                    String td3 = page.getHtml().xpath("//div[@class='layui-tab-item'][4]/table[@class='layui-table']/tbody/tr[" + i + "]/td[3]/tidyText()").toString();
                    BianMaChaCase bmacase = new BianMaChaCase();
                    bmacase.setHscode(td1);
                    bmacase.setDeclarationName(td2);
                    bmacase.setElementsOfDeclaration(td3);
                    list.add(bmacase);
                }
            }
            if (list != null && list.size() > 0) {
                page.putField("bianmacha_case_" + hashcode, list);
            }
            log.info("hashcode" + hashcode + "结束处理申报实例");
        } catch (Exception ex) {
            log.error("查询失败" + ex.getMessage());
        }
    }

    //处理贸易数据出口
    public void handledstout(Page page, String hashcode) {
        try {
            log.info("hashcode" + hashcode + "开始处理贸易数据-出口");
            List<String> tableallout = page.getHtml().xpath("//div[@class='layui-tab-item'][5]/table[@class='layui-table'][1]/tbody/tr").all();
            List<BianMaChaDst> outlist = new ArrayList<BianMaChaDst>();
            if (tableallout.size() < 2) {
                return;
            }
            for (int i = 1; i <= tableallout.size(); i++) {
                if (i != 1) {
                    String td1 = page.getHtml().xpath("//div[@class='layui-tab-item'][5]/table[@class='layui-table'][1]/tbody/tr[" + i + "]/td[1]/tidyText()").toString();
                    String td2 = page.getHtml().xpath("//div[@class='layui-tab-item'][5]/table[@class='layui-table'][1]/tbody/tr[" + i + "]/td[2]/tidyText()").toString();
                    String td3 = page.getHtml().xpath("//div[@class='layui-tab-item'][5]/table[@class='layui-table'][1]/tbody/tr[" + i + "]/td[3]/tidyText()").toString();
                    String td4 = page.getHtml().xpath("//div[@class='layui-tab-item'][5]/table[@class='layui-table'][1]/tbody/tr[" + i + "]/td[4]/tidyText()").toString();
                    BianMaChaDst outbmadst = new BianMaChaDst();
                    outbmadst.setYears(td1);
                    outbmadst.setTotalNumExports(td2);
                    outbmadst.setTotalAmountExports(td3);
                    outbmadst.setAverageExportPrice(td4);
                    outlist.add(outbmadst);
                }
            }
            if (outlist != null && outlist.size() > 0) {
                page.putField("bianmacha_dst_out_" + hashcode, outlist);
            }
            log.info("hashcode" + hashcode + "结束处理贸易数据-出口");
        } catch (Exception ex) {
            log.error("查询失败" + ex.getMessage());
        }
    }

    //处理贸易数据 进口
    public void handledstin(Page page, String hashcode) {
        try {
            log.info("hashcode" + hashcode + "开始处理贸易数据-进口");
            List<String> tableallin = page.getHtml().xpath("//div[@class='layui-tab-item'][5]/table[@class='layui-table'][2]/tbody/tr").all();
            List<BianMaChaDst> inlist = new ArrayList<BianMaChaDst>();
            if (tableallin.size() < 2) {
                return;
            }
            for (int i = 1; i <= tableallin.size(); i++) {
                if (i != 1) {
                    String td1 = page.getHtml().xpath("//div[@class='layui-tab-item'][5]/table[@class='layui-table'][2]/tbody/tr[" + i + "]/td[1]/tidyText()").toString();
                    String td2 = page.getHtml().xpath("//div[@class='layui-tab-item'][5]/table[@class='layui-table'][2]/tbody/tr[" + i + "]/td[2]/tidyText()").toString();
                    String td3 = page.getHtml().xpath("//div[@class='layui-tab-item'][5]/table[@class='layui-table'][2]/tbody/tr[" + i + "]/td[3]/tidyText()").toString();
                    String td4 = page.getHtml().xpath("//div[@class='layui-tab-item'][5]/table[@class='layui-table'][2]/tbody/tr[" + i + "]/td[4]/tidyText()").toString();
                    BianMaChaDst inbmadst = new BianMaChaDst();
                    inbmadst.setYears(td1);
                    inbmadst.setTotalNumExports(td2);
                    inbmadst.setTotalAmountExports(td3);
                    inbmadst.setAverageExportPrice(td4);
                    inlist.add(inbmadst);
                }
            }
            if (inlist != null && inlist.size() > 0) {
                page.putField("bianmacha_dst_in_" + hashcode, inlist);
            }
            log.info("hashcode" + hashcode + "结束处理贸易数据-进口");
        } catch (Exception ex) {
            log.error("查询失败" + ex.getMessage());
        }
    }

    //主要企业 出口
    public void handlecmpout(Page page, String hashcode) {
        try {
            log.info("hashcode" + hashcode + "开始处理主要企业-进口");
            List<String> tableallout = page.getHtml().xpath("//div[@class='layui-tab-item'][6]/table[@class='layui-table'][1]/tbody/tr").all();
            List<BianMaChaCmp> outlist = new ArrayList<BianMaChaCmp>();
            if (tableallout.size() < 3) {
                return;
            }
            for (int i = 1; i <= tableallout.size(); i++) {
                if (i != 1 && i != tableallout.size()) {
                    String td1 = page.getHtml().xpath("//div[@class='layui-tab-item'][6]/table[@class='layui-table'][1]/tbody/tr[" + i + "]/td[1]/tidyText()").toString();
                    String back = page.getHtml().xpath("//div[@class='layui-tab-item'][6]/table[@class='layui-table'][1]/tbody/tr[" + i + "]/td[2]/tidyText()").toString();
                    String td2=Html2Text.htmlTotext(back);
                    String td3 = page.getHtml().xpath("//div[@class='layui-tab-item'][6]/table[@class='layui-table'][1]/tbody/tr[" + i + "]/td[3]/tidyText()").toString();
                    BianMaChaCmp outbmacmp = new BianMaChaCmp();
                    outbmacmp.setInorout(td1);
                    outbmacmp.setCompanyName(td2);
                    outbmacmp.setAnnualExportScale(td3);
                    outlist.add(outbmacmp);
                }
            }
            if (outlist != null && outlist.size() > 0) {
                page.putField("bianmacha_cmp_out_" + hashcode, outlist);
            }
            log.info("hashcode" + hashcode + "结束处理主要企业-出口");
        } catch (Exception ex) {
            log.error("查询失败" + ex.getMessage());
        }
    }

    //主要企业 进口
    public void handlecmpin(Page page, String hashcode) {
        try {
            log.info("hashcode" + hashcode + "开始处理主要企业-进口");
            List<String> tableallin = page.getHtml().xpath("//div[@class='layui-tab-item'][6]/table[@class='layui-table'][2]/tbody/tr").all();
            List<BianMaChaCmp> inlist = new ArrayList<BianMaChaCmp>();
            if (tableallin.size() < 3) {
                return;
            }
            for (int i = 1; i <= tableallin.size(); i++) {
                if (i != 1 && i != tableallin.size()) {
                    String td1 = page.getHtml().xpath("//div[@class='layui-tab-item'][6]/table[@class='layui-table'][2]/tbody/tr[" + i + "]/td[1]/tidyText()").toString();
                    String back = page.getHtml().xpath("//div[@class='layui-tab-item'][6]/table[@class='layui-table'][2]/tbody/tr[" + i + "]/td[2]/tidyText()").toString();
                    String td2=Html2Text.htmlTotext(back);
                    String td3 = page.getHtml().xpath("//div[@class='layui-tab-item'][6]/table[@class='layui-table'][2]/tbody/tr[" + i + "]/td[3]/tidyText()").toString();
                    BianMaChaCmp inbmacmp = new BianMaChaCmp();
                    inbmacmp.setInorout(td1);
                    inbmacmp.setCompanyName(td2);
                    inbmacmp.setAnnualExportScale(td3);
                    inlist.add(inbmacmp);
                }
            }
            if (inlist != null && inlist.size() > 0) {
                page.putField("bianmacha_cmp_in_" + hashcode, inlist);
            }
            log.info("hashcode" + hashcode + "结束处理主要企业-进口");
        } catch (Exception ex) {
            log.error("查询失败" + ex.getMessage());
        }
    }


    @Override
    public Site getSite() {
        return site;
    }


    public void startbianmacha(String hashcode) {
        //配置IP代理
        // IP代理池
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        try {
            List<Proxy> proxies = buildProxyIP();
            if(proxies.size()>0){
                System.out.println("请求代理IP： " + proxies);
                httpClientDownloader.setProxyProvider(new SimpleProxyProvider(proxies));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Spider.create(new BianMaChaPageProcessor())
                .addUrl("http://hs.bianmachaxun.com/detail_" + hashcode + ".htm")
                .setDownloader(httpClientDownloader)
                .addPipeline(this.dataPipeLine)
                .run();
    }


    /**
     * 不错的免费代理IP站点
     * www.89ip.cn
     *
     * @return
     */
    private List<Proxy> buildProxyIP() throws IOException {
        List<Proxy> proxies = new ArrayList<Proxy>();
        List<Object> list= redisUtils.lGet("ip",0,redisUtils.lGetListSize("ip"));
        for (int i = 0; i < list.size(); i++) {
           String str=(String)list.get(i);
           String[] group = str.split(":");
           int prot = Integer.parseInt(group[1]);
           proxies.add(new Proxy(group[0], prot));
        }
        return proxies;
    }

}
