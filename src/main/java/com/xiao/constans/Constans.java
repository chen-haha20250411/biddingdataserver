package com.xiao.constans;

import java.util.HashMap;
import java.util.Map;

/**
* 说明：常量类
* @author  赵增丰
* @version  1.0
* 2014-8-21 上午10:40:01
*/ 
public class Constans {
	
	/** 按钮 查询*/
	public static final String BTNEQUERY = "query";
	/**增加 */
	public static final String BTNADD = "add";
	/**修改 */
	public static final String BTNEDIT = "edit";
	/**删除 */
	public static final String BTNDEL = "del";
	/**审核 */
	public static final String BTNCHECK = "check";
	/**授权 */
	public static final String BTNSQ = "sq";
	/**审核 */
	public static final String BTNAUDIT = "audit";
	/**设置 */
	public static final String BTNSET = "set";
	/**发送、发布 */
	public static final String BTNSNED = "send";
	/**上架*/
	public static final String BTNSJ="sj";
	/**下架*/
	public static final String BTNXJ="xj";
	/**导出 */
	public static final String BTNEXPORT = "export";
	/**导入*/
	public static final String BTNIMPORT= "import";
	/**发货*/
	public static final String BTNDELIVERY ="delivery";
	/**退款*/
	public static final String BTNREFUND ="refund";
	/**收货 */
	public static final String BTNSH = "sh";
	/**上传Excel 文件*/ 
	public static final String BTNSCEXCELWJ = "scexcelwj";
	
	public static final String BTNVIEW = "view";
	
	public static final String BTNMARK="mark";
	
	public static final String BTNRENEW="renew";
	
	//====================分页数据=================================
	
	/**分页 默认页大小*/ 
	public static final int PAGESIZE = 10;
	/**默认第一页*/ 
	public static final int PAGE = 1;
	/**前端产品分页*/
	public static final int PAGESIZEPRODUCT = 8;
	
	//=============================订单状态=======================//
/*	public static final int ORDER_PENDING = 11;          		//待支付
	public static final int ORDER_ACCEPTED = 20;		 		//待发货
	public static final int ORDER_DECLAREING = 21;    			//待申报
	public static final int ORDER_DECLAR_FAILED  = 22;			//申报失败
	public static final int ORDER_DECLAR_SUCCESS = 30;			//申报成功/已发货
	public static final int ORDER_REFUNDING = 23;				//退款中
	public static final int ORDER_HAS_REFUNDED = 24;			//已退款
	public static final int ORDER_RETURNSING = 33;				//退货中
	public static final int ORDER_HAS_RETURNED = 34;			//已退货
	public static final int ORDER_FINISHED = 40;				//已完成
	public static final int ORDER_CANCELED = 0;					//已取消
*/		
	//================支付方式===================//	
	public static final int OFFLINEPAY = 1;						//余额支付
	public static final int UNIONPAY = 2;						//直联
	// public static final int ALIPAY = 3;							//支付宝
	public static final int INTERALIPAY = 4;					//国际支付宝
	//public static final int WXPAY = 5;							//微信
	/**余额支付*/
	public static final String BALANCE = "0";					// 余额支付
	/**支付宝支付*/
	public static final String ALIPAY = "1";					// 支付宝支付
	/** 通联*/
	public static final String ALLINPAY = "2";                  // 通联
	/**微信*/
	public static final String WXPAY = "3";						//微信
	
	
	//===================token使用================//
	public final static String CURRENT_USER = "operatorfortoken";
	
	public final static String ACCESS_TOKEN = "accessToken";

	public final static String WEB_CURRENT_USER = "userfortoken";

	public final static String WEB_ACCESS_TOKEN = "webaccessToken";
	
	public final static Long TOKEN_EXPIRED_TIME = 2592000L;
	public final static Long TOKEN_EXPIRED_TIME6 = 216000L;
	
	
	//===================订单状态================//
	public static final String ORDERSTATUS_00 = "00"; // 00:周期购未激活订单
	public static final String ORDERSTATUS_0 = "0"; // 0:待支付
	public static final String ORDERSTATUS_1 = "1"; // 1:待发货
	public static final String ORDERSTATUS_2 = "2"; // 2:已发货
	public static final String ORDERSTATUS_5 = "5"; // 5:已收货
	public static final String ORDERSTATUS_99 = "99"; // 99:失败
	
	//===================运费=================//
	/**运费无*/
	public static final String FREIGHT00 = "00";
	/**运费有*/
	public static final String FREIGHT01 = "01";
	
	//==================添加购物车==================//
	/**不可以*/
	public static final String SHOPPING_CART00 = "00";
	/**可以*/
	public static final String SHOPPING_CART01 = "01";
	
	//==================商品业务类型==================//
	/**完税商品*/
	public static final String GOODS_TYPE_1 = "1";
	/**保税商品*/
	public static final String GOODS_TYPE_2 = "2";
	
	//==================税率区分==================//
	/**默认税率*/
	public static final String TAX_TYPE_0 = "0";
	/**自定义税率*/
	public static final String TAX_TYPE_1 = "1";
	
	//==================税率==================//
	/**保税税率*/
	public static final String TAX_2 = "11.9";
	/**完税税率*/
	public static final String TAX_1 = "0";
	
	//==================发送邮件==================//
	/**SMTP服务器的名字*/
	public static final String HOST = "smtp.163.com";
	/**发送人的邮箱*/
	public static final String SENDER = "doit9927@163.com";
	/**发送人的昵称*/
	public static final String EMAIL_NAME = "杭州人才卫生网";
	/**邮件主题*/
	public static final String SUBJECT = "面试邀请通知!";
	/**邮件内容*/
	public static final String MESSAGE = "恭喜您获得谢谢参与一枚!";
	/**收件人的邮箱*/
	public static final String RECEIVER = "157211979@qq.com";
	/**收件人的名称*/
	public static final String RECEIVER_NAME = "张三";
	/**账号*/
	public static final String USERNAME = "doit9927@163.com";
	/**密码*/
	public static final String PASSWORD = "doit9927";
	/**收件人的邮箱和名称*/
	public static final Map<String,String> map = new HashMap<String,String>(){
		{
			put("157211979@qq.com", "张三");
		}
	};
	
	/**分组——全局*/
	public static String GROUP_ID_0 = "0";
	
	public static int TX_METHOD_TIMEOUT=5;
	
}
