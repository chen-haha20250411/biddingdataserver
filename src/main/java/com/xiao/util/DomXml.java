package com.xiao.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DomXml {

    public static void main(String[] args){
        Long start = System.currentTimeMillis();
        createXml1();
        createXml2();
        System.out.println("运行时间："+ (System.currentTimeMillis() - start));
    }

    /**
     * 生成xml方法
     */
    public static void createXml1(){
        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document document = db.newDocument();
            // 不显示standalone="no"
            document.setXmlStandalone(true);
            Element ufinterface = document.createElement("ufinterface");
            ufinterface.setAttribute("account", "NKNC");
            ufinterface.setAttribute("billtype","F0");
            ufinterface.setAttribute("businessunitcode","");
            ufinterface.setAttribute("filename","");
            ufinterface.setAttribute("groupcode","CNK");
            ufinterface.setAttribute("isexchange","Y");
            ufinterface.setAttribute("orgcode","NC10");
            ufinterface.setAttribute("receiver","NC10");
            ufinterface.setAttribute("replace","Y");
            ufinterface.setAttribute("roottag","");
            ufinterface.setAttribute("sender","ty");
            // 向bookstore根节点中添加子节点book
            Element bill = document.createElement("bill");
            bill.setAttribute("id","tytest1");
            //向bill节点添加子节点billhead
            Element billhead = document.createElement("billhead");
            //向billhead添加子节点
            //所属集团
            Element pk_group = document.createElement("pk_group");
            pk_group.setTextContent("CNK");
            billhead.appendChild(pk_group);
            //应收财务组织
            Element pk_org = document.createElement("pk_org");
            pk_org.setTextContent("NC10");
            billhead.appendChild(pk_org);
            //结算财务组织
            Element sett_org = document.createElement("sett_org");
            sett_org.setTextContent("NC10");
            billhead.appendChild(sett_org);
            //创建时间
            Element creationtime = document.createElement("creationtime");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            creationtime.setTextContent(df.format(new Date()));
            billhead.appendChild(creationtime);
            //创建人
            Element creator = document.createElement("creator");
            creator.setTextContent("fumn");
            billhead.appendChild(creator);
            //单据类型编码
            Element pk_billtype = document.createElement("pk_billtype");
            pk_billtype.setTextContent("F0");
            billhead.appendChild(pk_billtype);
            //应收类型code
            Element pk_tradetype = document.createElement("pk_tradetype");
            pk_tradetype.setTextContent("D0");
            billhead.appendChild(pk_tradetype);
            //单据大类
            Element billclass = document.createElement("billclass");
            billclass.setTextContent("ys");
            billhead.appendChild(billclass);
            //单据所属系统
            Element syscode = document.createElement("syscode");
            syscode.setTextContent("0");
            billhead.appendChild(syscode);
            //单据日期
            Element billdate = document.createElement("billdate");
            billdate.setTextContent("2019-10-04 10:42:58");
            billhead.appendChild(billdate);
            //单据状态
            Element billstatus = document.createElement("billstatus");
            billstatus.setTextContent("0");
            billhead.appendChild(billstatus);
            //制单人
            Element billmaker = document.createElement("billmaker");
            billmaker.setTextContent("fumm");
            billhead.appendChild(billmaker);
            //年
            Element billyear = document.createElement("billyear");
            billyear.setTextContent("2020");
            billhead.appendChild(billyear);
            //月
            Element billperiod = document.createElement("billperiod");
            billperiod.setTextContent("06");
            billhead.appendChild(billperiod);
            //通宇系统标识
            Element def28 = document.createElement("def28");
            def28.setTextContent("ty");
            billhead.appendChild(def28);
            //通宇系统ID
            Element def29 = document.createElement("def29");
            def29.setTextContent("tytest1");
            billhead.appendChild(def29);
            //
            Element bodys = document.createElement("bodys");
            Element item = document.createElement("item");
            //部门编码
            Element pk_deptid = document.createElement("pk_deptid");
            pk_deptid.setTextContent("NC1002");
            item.appendChild(pk_deptid);
            //业务员
            Element pk_psndoc = document.createElement("pk_psndoc");
            pk_psndoc.setTextContent("");
            item.appendChild(pk_psndoc);
            //客户编码
            Element customer = document.createElement("customer");
            customer.setTextContent("NC10");
            item.appendChild(customer);
            //往来对象
            Element objtype = document.createElement("objtype");
            objtype.setTextContent("0");
            item.appendChild(objtype);
            //方向:1借方 -1贷方
            Element direction = document.createElement("direction");
            direction.setTextContent("1");
            item.appendChild(direction);
            //摘要
            Element scomment = document.createElement("scomment");
            scomment.setTextContent("test");
            item.appendChild(scomment);
            //用友NC65币种编码
            Element pk_currtype = document.createElement("pk_currtype");
            pk_currtype.setTextContent("MOP");
            item.appendChild(pk_currtype);
            //借方数量
            Element quantity_de = document.createElement("quantity_de");
            quantity_de.setTextContent("1");
            item.appendChild(quantity_de);
            //借方原币金额
            Element money_de = document.createElement("money_de");
            money_de.setTextContent("10000.00000000");
            item.appendChild(money_de);
            //原币余额
            Element money_bal = document.createElement("money_bal");
            money_bal.setTextContent("10000.00000000");
            item.appendChild(money_bal);
            //借方原币无税金额
            Element notax_de = document.createElement("notax_de");
            notax_de.setTextContent("10000.00000000");
            item.appendChild(notax_de);
            //单价
            Element price = document.createElement("price");
            price.setTextContent("10000.00000000");
            item.appendChild(price);
            //含税单价
            Element taxprice = document.createElement("taxprice");
            taxprice.setTextContent("10000.00000000");
            item.appendChild(taxprice);
            //发票号
            Element invoiceno = document.createElement("invoiceno");
            invoiceno.setTextContent("fp0001");
            item.appendChild(invoiceno);
            //银行账户编码
            Element recaccount = document.createElement("recaccount");
            recaccount.setTextContent("");
            item.appendChild(recaccount);
            //是否预收预付标志 0/1
            Element prepay = document.createElement("prepay");
            prepay.setTextContent("0");
            item.appendChild(prepay);
            // 将item节点添加到bodys根节点中
            bodys.appendChild(item);
            // 将bodys添加到billhead根节点中
            billhead.appendChild(bodys);
            //将billhead添加到bill根结点
            bill.appendChild(billhead);
            //将bill添加到ufinterface
            ufinterface.appendChild(bill);
            // 将ufinterface节点添加到dom树中
            document.appendChild(ufinterface);
            //获取当前时间戳
            long t0=System.currentTimeMillis();
            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            tff.setAttribute("indent-number", new Integer(2));
            // 创建 Transformer对象
            Transformer tf = tff.newTransformer();

            // 输出内容是否使用换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            // 创建xml文件并写入内容
            tf.transform(new DOMSource(document), new StreamResult(new OutputStreamWriter(new FileOutputStream("应收单"+t0+".xml"))));
            System.out.println("应收单V1.0.xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("应收单V1.0.xml失败");
        }
    }

    public static void createXml2(){
        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document document = db.newDocument();
            // 不显示standalone="no"
            document.setXmlStandalone(true);
            Element ufinterface = document.createElement("ufinterface");
            ufinterface.setAttribute("account", "NKNC");
            ufinterface.setAttribute("billtype","F2");
            ufinterface.setAttribute("businessunitcode","");
            ufinterface.setAttribute("filename","");
            ufinterface.setAttribute("groupcode","CNK");
            ufinterface.setAttribute("isexchange","Y");
            ufinterface.setAttribute("orgcode","NG0401");
            ufinterface.setAttribute("receiver","NG0401");
            ufinterface.setAttribute("replace","Y");
            ufinterface.setAttribute("roottag","");
            ufinterface.setAttribute("sender","ty");
            // 向bookstore根节点中添加子节点book
            Element bill = document.createElement("bill");
            bill.setAttribute("id","tytest1");
            //向bill节点添加子节点billhead
            Element billhead = document.createElement("billhead");
            //向billhead添加子节点
            //所属集团
            Element pk_group = document.createElement("pk_group");
            pk_group.setTextContent("NG0401");
            billhead.appendChild(pk_group);
            //收款财务组织
            Element pk_org = document.createElement("pk_org");
            pk_org.setTextContent("NG0401");
            billhead.appendChild(pk_org);
            //结算财务组织
            Element sett_org = document.createElement("sett_org");
            sett_org.setTextContent("NG0401");
            billhead.appendChild(sett_org);
            //创建时间
            Element creationtime = document.createElement("creationtime");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            creationtime.setTextContent(df.format(new Date()));
            billhead.appendChild(creationtime);
            //创建人
            Element creator = document.createElement("creator");
            creator.setTextContent("fumn");
            billhead.appendChild(creator);
            //单据类型编码
            Element pk_billtype = document.createElement("pk_billtype");
            pk_billtype.setTextContent("F2");
            billhead.appendChild(pk_billtype);
            //应收类型code
            Element pk_tradetype = document.createElement("pk_tradetype");
            pk_tradetype.setTextContent("D2");
            billhead.appendChild(pk_tradetype);
            //单据大类
            Element billclass = document.createElement("billclass");
            billclass.setTextContent("sk");
            billhead.appendChild(billclass);
            //单据日期
            Element billdate = document.createElement("billdate");
            billdate.setTextContent("2019-10-04 10:42:58");
            billhead.appendChild(billdate);
            //单据所属系统
            Element syscode = document.createElement("syscode");
            syscode.setTextContent("0");
            billhead.appendChild(syscode);
            //单据来源
            Element src_syscode = document.createElement("src_syscode");
            src_syscode.setTextContent("0");
            billhead.appendChild(src_syscode);
            //单据状态
            Element billstatus = document.createElement("billstatus");
            billstatus.setTextContent("0");
            billhead.appendChild(billstatus);
            //制单人
            Element billmaker = document.createElement("billmaker");
            billmaker.setTextContent("fumm");
            billhead.appendChild(billmaker);
            //业务流程
            Element pk_busitype = document.createElement("pk_busitype");
            pk_busitype.setTextContent("");
            billhead.appendChild(pk_busitype);
            //年
            Element billyear = document.createElement("billyear");
            billyear.setTextContent("2020");
            billhead.appendChild(billyear);
            //月
            Element billperiod = document.createElement("billperiod");
            billperiod.setTextContent("06");
            billhead.appendChild(billperiod);
            //银行账户
            Element recaccount = document.createElement("recaccount");
            recaccount.setTextContent("88779988");
            billhead.appendChild(recaccount);
            //通宇系统标识
            Element def28 = document.createElement("def28");
            def28.setTextContent("ty");
            billhead.appendChild(def28);
            //通宇系统ID
            Element def29 = document.createElement("def29");
            def29.setTextContent("tytest1");
            billhead.appendChild(def29);
            //
            Element bodys = document.createElement("bodys");
            Element item = document.createElement("item");
            //客户
            Element customer = document.createElement("customer");
            customer.setTextContent("07891");
            item.appendChild(customer);
            //票据方向
            Element checkdirection = document.createElement("checkdirection");
            checkdirection.setTextContent("ar");
            item.appendChild(checkdirection);
            //往来对象
            Element objtype = document.createElement("objtype");
            objtype.setTextContent("0");
            item.appendChild(objtype);
            //方向:1借方 -1贷方
            Element direction = document.createElement("direction");
            direction.setTextContent("-1");
            item.appendChild(direction);
            //摘要
            Element scomment = document.createElement("scomment");
            scomment.setTextContent("作业单号;车辆类型;托架号....作业单号;车辆类型;托架号....");
            item.appendChild(scomment);
            //用友NC65币种编码
            Element pk_currtype = document.createElement("pk_currtype");
            pk_currtype.setTextContent("MOP");
            item.appendChild(pk_currtype);
            //部门
            Element pk_deptid = document.createElement("pk_deptid");
            pk_deptid.setTextContent("NG040101");
            item.appendChild(pk_deptid);
            //贷方原币金额,
            Element money_cr = document.createElement("money_cr");
            money_cr.setTextContent("10.00000000");
            item.appendChild(money_cr);
            //原币余额
            Element money_bal = document.createElement("money_bal");
            money_bal.setTextContent("10000.00000000");
            item.appendChild(money_bal);
            //借方原币无税金额
            Element notax_de = document.createElement("notax_de");
            notax_de.setTextContent("10000.00000000");
            item.appendChild(notax_de);
            //发票号
            Element invoiceno = document.createElement("invoiceno");
            invoiceno.setTextContent("fp0001");
            item.appendChild(invoiceno);
            // 将item节点添加到bodys根节点中
            bodys.appendChild(item);
            // 将bodys添加到billhead根节点中
            billhead.appendChild(bodys);
            //将billhead添加到bill根结点
            bill.appendChild(billhead);
            //将bill添加到ufinterface
            ufinterface.appendChild(bill);
            // 将ufinterface节点添加到dom树中
            document.appendChild(ufinterface);
            //获取当前时间戳
            long t1=System.currentTimeMillis();
            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            tff.setAttribute("indent-number", new Integer(2));
            // 创建 Transformer对象
            Transformer tf = tff.newTransformer();

            // 输出内容是否使用换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            // 创建xml文件并写入内容
            tf.transform(new DOMSource(document), new StreamResult(new OutputStreamWriter(new FileOutputStream("收款单_"+t1+".xml"))));
            System.out.println("收款单V1.0.xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("收款单V1.0.xml失败");
        }

    }

}
