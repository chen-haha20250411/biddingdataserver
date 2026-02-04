package com.xiao.core.webmagic.service.impl;

import com.xiao.core.webmagic.domain.BianMaChaBsc;
import com.xiao.core.webmagic.domain.BianMaChaExp;
import com.xiao.core.webmagic.domain.BianMaChaImp;
import com.xiao.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Component
public class AreaPageProcessor implements PageProcessor {
    protected static Logger log = LoggerFactory.getLogger(BianMaChaPageProcessor.class);

    @Autowired
    private AreaDataPipeLine dataPipeLine;

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
            .setSleepTime(500)
            .setTimeOut(3 * 60 * 1000)
            .setCharset("UTF-8")
            .setUserAgent(randomHeader())
            .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .addHeader("Accept-Encoding", "gzip, deflate")
            .addHeader("Connection", "keep-alive");

    @Override
    public Site getSite() {
        return site;
    }


    @Override
    public void process(Page page) {
        String hashcode = page.getUrl().toString().substring(page.getUrl().toString().lastIndexOf("/")).split("\\.")[0].replace("/","");
        List<Selectable> tableallout = page.getHtml().xpath("//div[@class='cbox']").nodes();
        if (tableallout != null && tableallout.size() > 0) {
            handlebsc(page,tableallout,hashcode);
            handleexpimp(page,tableallout, hashcode);
            page.putField("365area_" + hashcode, "1");
        } else {
            page.putField("365area_" + hashcode, "2");
        }
    }
    //基本信息
    public void handlebsc(Page page, List<Selectable> selectables, String hashcode){
        try{
            log.info("hashcode" + hashcode + "开始处理基本信息");
            //HS编码:
            String td1 = selectables.get(0).xpath("//table/tbody/tr[1]/td[2]/tidyText()").toString();
            //商品名称
            String td2 = selectables.get(0).xpath("//table/tbody/tr[2]/td[2]/tidyText()").toString();
            //单位
            String td4 = selectables.get(1).xpath("//table/tbody/tr[1]/td[2]/tidyText()").toString();
            //CIQ
            StringBuffer ciqsb=new StringBuffer("");
            List<String> tableciq = selectables.get(6).xpath("//table/tbody/tr").all();
            for (int i = 1; i <= tableciq.size(); i++) {
                String ciqtd1 = selectables.get(6).xpath("//table/tbody/tr[" + i + "]/td[1]/tidyText()").toString();
                String ciqtd2 = selectables.get(6).xpath("//table/tbody/tr[" + i + "]/td[2]/tidyText()").toString();
                ciqsb.append(ciqtd1+":"+ciqtd2+",");
            }
            StringBuffer td3=new StringBuffer("");
            //申报要素
            List<String> tableall =  selectables.get(2).xpath("//table/tbody/tr").all();
            if (tableall.size() < 2) {
                return;
            }
            for (int i = 1; i <= tableall.size(); i++) {
                String ytd1 = selectables.get(2).xpath("//table/tbody/tr[" + i + "]/td[1]/tidyText()").toString();
                String ytd2 = selectables.get(2).xpath("//table/tbody/tr[" + i + "]/td[2]/tidyText()").toString();
                String nytd2 = ytd2.split("\\ ")[0];
                td3.append(ytd1+":"+nytd2+",");
            }
            //监管条件
            String td5 = selectables.get(3).xpath("//table/tbody/tr[1]/td[1]/tidyText()").toString() +" "+ selectables.get(3).xpath("//table/tbody/tr[1]/td[2]/tidyText()").toString();
            //检验检疫类别
            String td6 = selectables.get(4).xpath("//table/tbody/tr[1]/td[1]/tidyText()").toString() +" "+ selectables.get(4).xpath("//table/tbody/tr[1]/td[2]/tidyText()").toString();
            BianMaChaBsc bsc = new BianMaChaBsc();
            bsc.setHscode(td1);
            bsc.setCndesc(td2);
            bsc.setDeclelements(td3.toString());
            bsc.setDescunit(td4);
            bsc.setCiqcode(ciqsb.toString());
            bsc.setMonitoringCondit(td5);
            bsc.setQuarantineCondit(td6);
            if (bsc != null) {
                page.putField("365area_bsc_" + hashcode, bsc);
            }
        } catch (Exception ex) {
            log.error("基本信息查询失败" + ex.getMessage());
        }
    }

    //税率
    public void handleexpimp(Page page, List<Selectable> selectables, String hashcode){
        try{
            log.info("hashcode" + hashcode + "开始处理税单信息");
            String td1 = selectables.get(1).xpath("//table/tbody/tr[2]/td[2]/tidyText()").toString();
            String td2 = selectables.get(1).xpath("//table/tbody/tr[3]/td[2]/tidyText()").toString();
            String td3 = selectables.get(1).xpath("//table/tbody/tr[4]/td[2]/tidyText()").toString();

            BianMaChaExp exp = new BianMaChaExp();
            exp.setExportTaxRebate(td2);
            exp.setExportDuties(td1);
            exp.setTemporaryTaxRate(td3);
            if (exp != null) {
                page.putField("365area_exp_" + hashcode, exp);
            }

            String td5 = selectables.get(1).xpath("//table/tbody/tr[5]/td[2]/tidyText()").toString();
            String td6 = selectables.get(1).xpath("//table/tbody/tr[6]/td[2]/tidyText()").toString();
            String td7 = selectables.get(1).xpath("//table/tbody/tr[7]/td[2]/tidyText()").toString();
            String td8 = selectables.get(1).xpath("//table/tbody/tr[8]/td[2]/tidyText()").toString();
            String td9 = selectables.get(1).xpath("//table/tbody/tr[9]/td[2]/tidyText()").toString();

            BianMaChaImp imp = new BianMaChaImp();
            imp.setImportTaxRate(td6);
            imp.setGeneralImportTaxRate(td8);
            imp.setProvisionalImportTaxRate(td7);
            imp.setImportConsumptionTax(td9);
            imp.setImportValueAddedTax(td5);
            if (imp != null) {
                page.putField("365area_imp_" + hashcode, imp);
            }

        } catch (Exception ex) {
            log.error("税率查询失败" + ex.getMessage());
        }
    }


    public void start(String hashcode) {
        Spider.create(new AreaPageProcessor())
                .addUrl("https://www.hsbianma.com/Code/" + hashcode + ".html")
                .addPipeline(this.dataPipeLine)
                .run();
    }

    public static void main(String[] args) {
        AreaPageProcessor areaPageProcessor=new AreaPageProcessor();
        areaPageProcessor.start("3304990039");
    }
}
