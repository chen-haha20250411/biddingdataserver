// package com.xiao.schedule;
//
// import com.alibaba.fastjson.JSON;
// import com.alibaba.fastjson.JSONArray;
// import com.alibaba.fastjson.JSONObject;
// import com.xiao.base.Page;
// import com.xiao.core.jiashicang.JiaShiCangUtils;
// import com.xiao.core.jiashicang.jscCzjCyfczj.service.JscCzjCyfczjService;
// import com.xiao.core.jiashicang.jscFgjLjqye.service.JscFgjLjqyeService;
// import com.xiao.core.jiashicang.jscFgjSjgxjsqy.service.JscFgjSjgxjsqyService;
// import com.xiao.core.jiashicang.jscFgjSsqy.service.JscFgjSsqyService;
// import com.xiao.core.jiashicang.jscFgjZdgxjsqy.service.JscFgjZdgxjsqyService;
// import com.xiao.core.jiashicang.jscJxjDlqyxx.service.JscJxjDlqyxxService;
// import com.xiao.core.jiashicang.jscScjgjQyjbxx.domain.JscScjgjQyjbxx;
// import com.xiao.core.jiashicang.jscScjgjQyjbxx.service.JscScjgjQyjbxxService;
// import com.xiao.core.jiashicang.jscSwjQyssze.domain.JscSwjQyssze;
// import com.xiao.core.jiashicang.jscSwjQyssze.service.JscSwjQysszeService;
// import com.xiao.core.oldcode.basicTalPlacate.service.BasicTalPlacateService;
// import com.xiao.core.oldcode.basiccompany.domain.Basiccompany;
// import com.xiao.core.oldcode.basiccompany.service.BasiccompanyService;
// import com.xiao.core.oldcode.basiccompany5050jh.service.Basiccompany5050jhService;
// import com.xiao.core.oldcode.basiccompanyCq.domain.BasiccompanyCq;
// import com.xiao.core.oldcode.basiccompanyCq.service.BasiccompanyCqService;
// import com.xiao.core.oldcode.basiccompanyCq5050jh.service.BasiccompanyCq5050jhService;
// import com.xiao.core.oldcode.basiccompanyGqjg5050jh.service.BasiccompanyGqjg5050jhService;
// import com.xiao.core.oldcode.basiccompanyJnsbrs.domain.BasiccompanyJnsbrs;
// import com.xiao.core.oldcode.basiccompanyJnsbrs.service.BasiccompanyJnsbrsService;
// import com.xiao.core.oldcode.basiccompanyJnsbrs5050jh.service.BasiccompanyJnsbrs5050jhService;
// import com.xiao.core.oldcode.basiccompanyJs.domain.BasiccompanyJs;
// import com.xiao.core.oldcode.basiccompanyJs.service.BasiccompanyJsService;
// import com.xiao.core.oldcode.basiccompanyJs5050jh.service.BasiccompanyJs5050jhService;
// import com.xiao.core.oldcode.basiccompanyQybq.domain.BasiccompanyQybq;
// import com.xiao.core.oldcode.basiccompanyQybq.service.BasiccompanyQybqService;
// import com.xiao.core.oldcode.basiccompanyQybq5050jh.service.BasiccompanyQybq5050jhService;
// import com.xiao.core.oldcode.basiccompanyRz.domain.BasiccompanyRz;
// import com.xiao.core.oldcode.basiccompanyRz.service.BasiccompanyRzService;
// import com.xiao.core.oldcode.basiccompanyRz5050jh.service.BasiccompanyRz5050jhService;
// import com.xiao.core.oldcode.smsMessage.domain.SmsMessage;
// import com.xiao.core.oldcode.smsMessage.service.SmsMessageService;
// import com.xiao.core.oldcode.statistics.domain.Statistics;
// import com.xiao.core.oldcode.statistics.service.StatisticsService;
// import com.xiao.core.qichacha.QiChaChaUtils;
// import com.xiao.core.qichacha.domain.*;
// import com.xiao.core.sendmms.HttpsMmsSend;
// import com.xiao.core.shebaoworkplat.Shareholder;
// import com.xiao.core.shebaoworkplat.ShebaoUtil;
// import com.xiao.util.RedisUtils;
// import com.xiao.util.StringUtil;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// import javax.annotation.Resource;
// import java.text.SimpleDateFormat;
// import java.util.*;
// import java.util.regex.Pattern;
//
// @Component      //1.主要用于标记配置类，兼备Component的效果。
// public class ScheduleTask {
//     protected Logger log = LoggerFactory.getLogger(this.getClass());
//
//     @Resource
//     JscScjgjQyjbxxService scjgjQyjbxxService;
//     @Resource
//     BasicTalPlacateService basicTalPlacateService;
//     @Resource
//     JscJxjDlqyxxService jscJxjDlqyxxService;
//     @Resource
//     JscFgjZdgxjsqyService jscFgjZdgxjsqyService;
//     @Resource
//     JscFgjSsqyService jscFgjSsqyService;
//     @Resource
//     JscFgjSjgxjsqyService jscFgjSjgxjsqyService;
//     @Resource
//     JscFgjLjqyeService jscFgjLjqyeService;
//     @Resource
//     JscCzjCyfczjService jscCzjCyfczjService;
//     @Resource
//     JscSwjQysszeService jscSwjQysszeService;
//     @Resource
//     BasiccompanyService basiccompanyService;
//     @Resource
//     BasiccompanyJsService basiccompanyJsService;
//     @Resource
//     BasiccompanyCqService basiccompanyCqService;
//     @Resource
//     BasiccompanyRzService basiccompanyRzService;
//     @Resource
//     BasiccompanyQybqService basiccompanyQybqService;
//     @Resource
//     SmsMessageService smsMessageService;
//     @Resource
//     BasiccompanyJnsbrsService basiccompanyJnsbrsService;
//     @Resource
//     StatisticsService statisticsService;
//     @Autowired
//     HttpsMmsSend httpsMmsSend;
//     @Value("${lljUrl}")
// 	private String lljUrl;
//     @Autowired
//     RedisUtils redisUtils;
//     @Resource
//     private Basiccompany5050jhService basiccompany5050jhService;
//     @Resource
//     private BasiccompanyCq5050jhService basiccompanyCq5050jhService;
//     @Resource
//     private BasiccompanyGqjg5050jhService basiccompanyGqjg5050jhService;
//     @Resource
//     private BasiccompanyJnsbrs5050jhService basiccompanyJnsbrs5050jhService;
//     @Resource
//     private BasiccompanyJs5050jhService basiccompanyJs5050jhService;
//     @Resource
//     private BasiccompanyQybq5050jhService basiccompanyQybq5050jhService;
//     @Resource
//     private BasiccompanyRz5050jhService basiccompanyRz5050jhService;
//
//     /*@Scheduled(cron="0 *10 * * * ?")*/
//     //@Scheduled(cron="*/20 * * * * ?")
//     public void queryReceptFile() {
//         try {
//             log.debug("开始查询回执文件");
//             log.debug("结束查询回执文件");
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
//
//     /**
//      * 企业安商稳商预判模型
//      */
//
//     //@Scheduled(cron="0 14 9 * * ?")
//     public void queryForAnalysis() {
//         try {
//             basicTalPlacateService.insertAnalysis();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
//
//     /**
//      * 企业基本信息
//      * TODO 正式需打开
//      * 每天早上8点10分后更新
//      */
//     final String[] strarr = {"互联网小镇", "江北区块", "浦沿街道", "物联网小镇", "白马湖创意城", "西兴街道", "长河街道"};
//
//     //final String[] strarr={"西兴街道"};
//     @Scheduled(cron = "0 10 8 * * ?")
//     public void insertjiashicangQyjbxx() {
//         for (String str : strarr) {
//             boolean flag = scjgjQyjbxxService.insertOrUpdateListBatch(str);
//             if (flag) {
//                 log.info(str + ":" + "企业基本信息城市大脑数据对接更新成功");
//             } else {
//                 log.error(str + ":" + "企业基本信息城市大脑数据对接更新失败");
//             }
//         }
//     }
//
//     public JscScjgjQyjbxx queryJiashicangQyjbxx(String scjgj_qyjbxx_gsqy,String tyshxydm) {
//
//     	if(!StringUtil.isEmpty(scjgj_qyjbxx_gsqy)){
//     		String gsqy= null;
//         	switch (scjgj_qyjbxx_gsqy) {
//     		case "互联网小镇":
//     			gsqy = scjgj_qyjbxx_gsqy;
//     			break;
//     		case  "江北区块":
//     			gsqy = scjgj_qyjbxx_gsqy;
//     			break;
//     		case  "浦沿街道":
//     			gsqy = scjgj_qyjbxx_gsqy;
//     			break;
//     		case  "物联网小镇":
//     			gsqy = scjgj_qyjbxx_gsqy;
//     			break;
//     		case  "白马湖创意城":
//     			gsqy = scjgj_qyjbxx_gsqy;
//     			break;
//     		case  "西兴街道":
//     			gsqy = scjgj_qyjbxx_gsqy;
//     			break;
//     		case  "长河街道":
//     			gsqy = scjgj_qyjbxx_gsqy;
//     			break;
//     		default:
//     			break;
//     		}
//         	if(!StringUtil.isEmpty(gsqy)){
//         		List<JscScjgjQyjbxx> list = JiaShiCangUtils.queryCompanyBasicMsgForReg(gsqy, tyshxydm);
//         		if (list!=null && list.size()>0) {
//         			return list.get(0);
//         		}
//         	}
//     	}
//         for (String str : strarr) {
//         	List<JscScjgjQyjbxx> list = JiaShiCangUtils.queryCompanyBasicMsgForReg(str, tyshxydm);
//             if (list!=null && list.size()>0) {
//                 return list.get(0);
//             }
//         }
// 		return null;
//     }
//
//     @Scheduled(cron = "0 27 8 * * ?")
//     public void insertOtherQyjbxx() {
//         //数据更新完成后需要执行更新企业底库
//         //List<JscScjgjQyjbxx> bascilist = scjgjQyjbxxService.queryCompanyAll();
//         List<Basiccompany> list = new ArrayList<>();
//       //获取企业底库
//         Map<String, Object> params = new HashMap<String, Object>();
//         List<Basiccompany> bascilist = basiccompanyService.queryByMapForSch(params);
//         //for (JscScjgjQyjbxx jscScjgjQyjbxx : bascilist) {
//         for (Basiccompany basic : bascilist) {
//             Basiccompany basecompany = new Basiccompany();
//             basecompany.setLdCompanyId(basic.getLdCompanyId());
// //            if(!StringUtil.isEmpty(basic.getUnified_code())){
//             	//Rxnf更新保存为管辖区域
//             	//JscScjgjQyjbxx jscScjgjQyjbxx = queryJiashicangQyjbxx(basic.getRxnf(), basic.getUnified_code());
// //            	if(jscScjgjQyjbxx!=null){
// //            		basecompany.setLdCompanyName(jscScjgjQyjbxx.getScjgjQyjbxxQymc());
// //            		basic.setLdCompanyName(jscScjgjQyjbxx.getScjgjQyjbxxQymc());
// //            		basecompany.setRxnf(jscScjgjQyjbxx.getScjgjQyjbxxGsqy());
// //            		basecompany.setZczj(jscScjgjQyjbxx.getScjgjQyjbxxZczbs());
// //                    basecompany.setCltime(jscScjgjQyjbxx.getScjgjQyjbxxZcrq());
// //                    basecompany.setCorporate(jscScjgjQyjbxx.getScjgjQyjbxxFr());
// //                    basecompany.setQyAddress(jscScjgjQyjbxx.getScjgjQyjbxxDz());
// //                    basecompany.setScjgj_qyjbxx_jyfw(jscScjgjQyjbxx.getScjgjQyjbxxJyfw());
// //                    basecompany.setScjgj_qyjbxx_qyzt(jscScjgjQyjbxx.getScjgjQyjbxxQyzt());
// //                    basecompany.setScjgj_qyjbxx_zcrq(jscScjgjQyjbxx.getScjgjQyjbxxZcrq());
// //            	}
// //            }
//
//           //查询企业标签
//             params.put("ldCompanyId", basic.getLdCompanyId());
//             int count = basiccompanyQybqService.queryByCount(params);
// 			if(!StringUtil.isEmpty(basic.getLdCompanyName()) &&(count == 0 || StringUtil.isEmpty(basic.getUnified_code()))){
//             //basiccompanyQybqService.deleteByLdCompanyId(jscScjgjQyjbxx.getScjgjQyjbxxId());
// 				List<BasiccompanyQybq> addList = new ArrayList<BasiccompanyQybq>();
// 				EnterpriseInfo enterpriseInfo = QiChaChaUtils.queryCompanyBasicMsg(basic.getLdCompanyName());
// 				if(enterpriseInfo != null){
// 					if(count == 0){
// 						List<TagList> tagList =  enterpriseInfo.getTagList();
// 						for(TagList tag:tagList) {
// 							BasiccompanyQybq basiccompanyQybq = new BasiccompanyQybq();
// 							basiccompanyQybq.setLdCompanyId(basic.getLdCompanyId());
// 							basiccompanyQybq.setQybq(tag.getName());
// 							addList.add(basiccompanyQybq);
// 						}
// 						if(!addList.isEmpty()){
// 							basiccompanyQybqService.insertList(addList);
// 						}
// 					}
// 					if(StringUtil.isEmpty(basic.getUnified_code())){
// 						basecompany.setUnified_code(enterpriseInfo.getCreditCode());
// 						basic.setUnified_code(enterpriseInfo.getCreditCode());
// 						basecompany.setZczj(enterpriseInfo.getRegistCapi());
// 						basecompany.setCltime(enterpriseInfo.getStartDate());
// 						basecompany.setCorporate(enterpriseInfo.getOperName());
// 						basecompany.setQyAddress(enterpriseInfo.getAddress());
// 						basecompany.setScjgj_qyjbxx_jyfw(enterpriseInfo.getScope());
// 						basecompany.setScjgj_qyjbxx_qyzt(enterpriseInfo.getStatus());
// 						basecompany.setScjgj_qyjbxx_zcrq(enterpriseInfo.getStartDate());
// 					}
// 				}
// 			}
// 			if(!StringUtil.isEmpty(basic.getUnified_code())&& !StringUtil.isEmpty(basic.getLdCompanyName())){
//                 //查询参保人数
//                 Shareholder holder = ShebaoUtil.compSocialSecurityNum(basic.getUnified_code(), basic.getLdCompanyName(),redisUtils);
//                 if(holder!=null){
//                     basecompany.setJnsbrs(holder.getCBZS());
//                 }
//                 //basecompany.setUnified_code(jscScjgjQyjbxx.getScjgjQyjbxxTyshxydm());
//             }
//             //从城市大脑获取缴税额插入数据库
//             if(!StringUtil.isEmpty(basic.getUnified_code())){
//                 List<JscSwjQyssze> jsjelist = JiaShiCangUtils.queryCompanyTax(basic.getUnified_code());
//                 List<BasiccompanyJs> jslist = new ArrayList<BasiccompanyJs>();
//                 if (jslist != null && jslist.size() > 0) {
//                     for (JscSwjQyssze jscSwjQyssze : jsjelist) {
//                         BasiccompanyJs cjs = new BasiccompanyJs();
//                         cjs.setLdCompanyId(basic.getLdCompanyId());
//                         cjs.setNsYear(jscSwjQyssze.getRkrq());
//                         cjs.setJsE(jscSwjQyssze.getSsze());
//                         jslist.add(cjs);
//                     }
//                     basiccompanyJsService.deleteByCompanyId(basic.getLdCompanyId());
//                     basiccompanyJsService.insertList(jslist);
//                 }
//             	//查询税收总额
// //            	List<CompanyTax> taxList = ShebaoUtil.companyTaxList(basecompany.getUnified_code());
// //            	if(taxList != null && taxList.size()>0){
// //            		//double taxSum = 0.0;
// //            		List<BasiccompanyJs> jslist = new ArrayList<BasiccompanyJs>();
// //            		for (CompanyTax tax : taxList) {
// //            			if(tax.getZSXMMC().contains("所得")||tax.getZSXMMC().contains("增值")
// //							||tax.getZSXMMC().contains("营业")){
// //	            			BasiccompanyJs cjs = new BasiccompanyJs();
// //	            			cjs.setLdCompanyId(jscScjgjQyjbxx.getScjgjQyjbxxId());
// //	            			cjs.setNsYear(tax.getRKRQ());
// //	            			cjs.setJsE(tax.getSJJE());
// //	            			//taxSum+=Double.parseDouble(tax.getSJJE());
// //	            			jslist.add(cjs);
// //            			}
// //            		}
// //            		basiccompanyJsService.deleteByCompanyId(jscScjgjQyjbxx.getScjgjQyjbxxId());
// //            		basiccompanyJsService.insertList(jslist);
// //            		DoubleSummaryStatistics resultNum =  jslist.stream().mapToDouble((s) -> Double.valueOf(s.getJsE())).summaryStatistics();
// //            		if(resultNum != null){
// //						basecompany.setTax(String.valueOf(resultNum.getSum()));
// //					}
// //
// //            	}
//             }
//             List<BasiccompanyCq> cqlist = new ArrayList<>();
//             int pageindex = 50;
//             //从企查查获取企业软件著作权
//             if(!StringUtil.isEmpty(basic.getLdCompanyName())){
// 	            List<EnterpriseSoftCopyright> softlist = new ArrayList<>();
// 	            Page<EnterpriseSoftCopyright> softPage = QiChaChaUtils.queryCompanySoftCopyright(basic.getLdCompanyName(),  pageindex,1);
// 	            //总页数=（总记录数+每页显示数-1）/每页显示数
// 	            if (softPage.getList() != null) {
// 	                softlist.addAll(softPage.getList());
// 	            }
// 	            int softpagecurrent = (softPage.getTotal() + pageindex - 1) / pageindex;
// 	            if (softpagecurrent > 1) {
// 	                for (int i = 2; i <= softpagecurrent; i++) {
// 	                    Page<EnterpriseSoftCopyright> softPage2 = QiChaChaUtils.queryCompanySoftCopyright(basic.getLdCompanyName(), pageindex,i);
// 	                    if (softPage2.getList() != null) {
// 	                        softlist.addAll(softPage2.getList());
// 	                    }
// 	                }
// 	            }
// 	            for (EnterpriseSoftCopyright nterpriseSoftCopyright : softlist) {
// 	                BasiccompanyCq bcq = new BasiccompanyCq();
// 	                bcq.setLdCompanyId(basic.getLdCompanyId());
// 	                bcq.setCqName(nterpriseSoftCopyright.getName());
// 	                bcq.setGuiShuFang(nterpriseSoftCopyright.getOwner());
// 	                cqlist.add(bcq);
// 	            }
//
// 	            //从企查查获取企业著作权
// 	            List<EnterpriseCopyright> crlist = new ArrayList<>();
// 	            Page<EnterpriseCopyright> crpage = QiChaChaUtils.queryCompanyCopyright(basic.getLdCompanyName(), pageindex,1);
// 	            //总页数=（总记录数+每页显示数-1）/每页显示数
// 	            if (crpage.getList() != null) {
// 	                crlist.addAll(crpage.getList());
// 	            }
// 	            int crpagecurrent = (crpage.getTotal() + pageindex - 1) / pageindex;
// 	            if (crpagecurrent > 1) {
// 	                for (int i = 2; i <= crpagecurrent; i++) {
// 	                    Page<EnterpriseCopyright> crpage2 = QiChaChaUtils.queryCompanyCopyright(basic.getLdCompanyName(),  pageindex,i);
// 	                    if (crpage2.getList() != null) {
// 	                        crlist.addAll(crpage2.getList());
// 	                    }
// 	                }
// 	            }
// 	            for (EnterpriseCopyright enterpriseCopyright : crlist) {
// 	                BasiccompanyCq bcq = new BasiccompanyCq();
// 	                bcq.setLdCompanyId(basic.getLdCompanyId());
// 	                bcq.setCqName(enterpriseCopyright.getName());
// 	                bcq.setGuiShuFang(enterpriseCopyright.getOwner());
// 	                cqlist.add(bcq);
// 	            }
//
// 	            //从企查查获取专利
// 	            List<PatentV4> zllist = new ArrayList<>();
// 	            Page<PatentV4> zlpage = QiChaChaUtils.queryPatentV4(basic.getLdCompanyName(), pageindex,1);
// 	            //总页数=（总记录数+每页显示数-1）/每页显示数
// 	            if (zlpage.getList() != null) {
// 	                zllist.addAll(zlpage.getList());
// 	            }
// 	            int zlpagecurrent = (zlpage.getTotal() + pageindex - 1) / pageindex;
// 	            if (zlpagecurrent > 1) {
// 	                for (int i = 2; i <= zlpagecurrent; i++) {
// 	                    Page<PatentV4> zlpage2 = QiChaChaUtils.queryPatentV4(basic.getLdCompanyName(),  pageindex,i);
// 	                    if (zlpage2.getList() != null) {
// 	                        zllist.addAll(zlpage2.getList());
// 	                    }
// 	                }
// 	            }
// 	            for (PatentV4 patentV4 : zllist) {
// 	                BasiccompanyCq bcq = new BasiccompanyCq();
// 	                bcq.setLdCompanyId(basic.getLdCompanyId());
// 	                bcq.setCqName(patentV4.getTitle());
// 	                bcq.setGuiShuFang(patentV4.getPublicationNumber());
// 	                cqlist.add(bcq);
// 	            }
// 	            if (cqlist != null && cqlist.size() > 0) {
// 	                //删除企业著作和专利
// 	                basiccompanyCqService.deleteByCompanyId(basic.getLdCompanyId());
// 	                basiccompanyCqService.insertList(cqlist);
// 	                //设置今日专利数
// 	                basecompany.setTodayzc(cqlist.size());
// 	            }
//
// 	            //判断有无融资总额
// 	            Basiccompany basiccompany = basiccompanyService.queryById(basecompany.getLdCompanyId());
// 	            if(StringUtil.isEmpty(basiccompany.getRzed())){
// 	            	//从企查查获取融资
// 	            	List<BasiccompanyRz> brzlist = new ArrayList<>();
//
// 	            	List<CompanyFinancing> rzlist = new ArrayList<>();
// 	            	Page<CompanyFinancing> rzpage = QiChaChaUtils.queryCompanyFinancing(basic.getLdCompanyName(),  pageindex,1);
// 	            	//总页数=（总记录数+每页显示数-1）/每页显示数
// 	            	if (rzpage.getList() != null) {
// 	            		rzlist.addAll(rzpage.getList());
// 	            	}
// 	            	int rzpagecurrent = (rzpage.getTotal() + pageindex - 1) / pageindex;
// 	            	if (rzpagecurrent > 1) {
// 	            		for (int i = 2; i <= rzpagecurrent; i++) {
// 	            			Page<CompanyFinancing> rzpage2 = QiChaChaUtils.queryCompanyFinancing(basic.getLdCompanyName(), pageindex,i);
// 	            			if (rzpage2.getList() != null) {
// 	            				rzlist.addAll(rzpage2.getList());
// 	            			}
// 	            		}
// 	            	}
// 	            	double touZiE = 0.00;
// 	            	for (CompanyFinancing companyFinancing : rzlist) {
// 	            		BasiccompanyRz basiccompany_rz = new BasiccompanyRz();
// 	            		basiccompany_rz.setLdCompanyId(basic.getLdCompanyId());
// 	            		basiccompany_rz.setTouZiJiGou(companyFinancing.getInvestment());
// 	            		basiccompany_rz.setTouZiE(companyFinancing.getAmount());
// 	            		basiccompany_rz.setTouZiTime(companyFinancing.getDate());
// 	            		if(!StringUtil.isEmpty(companyFinancing.getAmount())){
// 	            			String REGEX ="[^(0-9).]";
// 	            			if(!StringUtil.isEmpty(Pattern.compile(REGEX).matcher(companyFinancing.getAmount()).replaceAll("").trim())){
// 	            				touZiE+=Double.parseDouble(Pattern.compile(REGEX).matcher(companyFinancing.getAmount()).replaceAll("").trim());
// 	            			}
// 	            		}
// 	            		brzlist.add(basiccompany_rz);
// 	            	}
// 	            	basecompany.setRzed(touZiE+"");
// 	            	//删除企业融资
// 	            	if (brzlist != null && brzlist.size() > 0) {
// 	            		basiccompanyRzService.deleteByCompanyId(basic.getLdCompanyId());
// 	            		basiccompanyRzService.insertList(brzlist);
// 	            	}
// 	            }
//             }
//             //查询税收总额
// //            if(!StringUtil.isEmpty(basecompany.getUnified_code())){
// //            	String tax = ShebaoUtil.companyTax(basecompany.getUnified_code());
// //            	if(!StringUtil.isEmpty(tax)){
// //            		basecompany.setTax(tax);
// //            	}
// //            }
//
//             list.add(basecompany);
//         }
//         boolean bflag = basiccompanyService.updateList(list);
//         if (bflag) {
//             log.info("企业基本信息定时更新成功");
//         } else {
//             log.error("企业基本信息定时更新失败");
//         }
//     }
//
//     /**
//      * 瞪羚企业
//      * * TODO 正式需打开
//      * 每年？？后更新
//      */
//     @Scheduled(cron = "0 01 8 * * ?")
//     public void insertjiashicangDlqy() {
//         boolean flag = jscJxjDlqyxxService.insertOrUpdateListBatch();
//         if (flag) {
//             log.info("瞪羚企业城市大脑数据对接更新成功");
//         } else {
//             log.error("瞪羚企业城市大脑数据对接更新失败");
//         }
//     }
//
//     /**
//      * 重点高新技术企业
//      * * TODO 正式需打开
//      * 每年？？后更新
//      */
//     @Scheduled(cron = "0 02 8 * * ?")
//     public void insertjiashicangZdgxjsqy() {
//         boolean flag = jscFgjZdgxjsqyService.insertOrUpdateListBatch();
//         if (flag) {
//             log.info("重点高新技术企业城市大脑数据对接更新成功");
//         } else {
//             log.error("重点高新技术企业城市大脑数据对接更新失败");
//         }
//     }
//
//     /**
//      * 上市企业
//      * * TODO 正式需打开
//      * 每年？？后更新
//      */
//     @Scheduled(cron = "0 03 8 * * ?")
//     public void insertjiashicangSsqy() {
//         boolean flag = jscFgjSsqyService.insertOrUpdateListBatch();
//         if (flag) {
//             log.info("上市企业城市大脑数据对接更新成功");
//         } else {
//             log.error("上市企业城市大脑数据对接更新失败");
//         }
//     }
//
//     /**
//      * 领军企业信息
//      * * TODO 正式需打开
//      * 每年？？后更新
//      */
//     @Scheduled(cron = "0 04 8 * * ?")
//     public void insertjiashicangLjqyxx() {
//         boolean flag = jscFgjLjqyeService.insertOrUpdateListBatch();
//         if (flag) {
//             log.info("省级高新技术企业城市大脑数据对接更新成功");
//         } else {
//             log.error("省级高新技术企业城市大脑数据对接更新失败");
//         }
//     }
//
//     /**
//      * 省级高新技术企业
//      * * TODO 正式需打开
//      * 每年？？后更新
//      */
//     @Scheduled(cron = "0 05 8 * * ?")
//     public void insertjiashicangSjgxjsqy() {
//         boolean flag = jscFgjSjgxjsqyService.insertOrUpdateListBatch();
//         if (flag) {
//             log.info("省级高新技术企业城市大脑数据对接更新成功");
//         } else {
//             log.error("省级高新技术企业城市大脑数据对接更新失败");
//         }
//     }
//
//     /**
//      * 产业扶持资金
//      * * TODO 正式需打开
//      * 每年？？后更新
//      */
//     @Scheduled(cron = "0 11 8 * * ?")
//     public void insertjiashicangCyfczj() {
//         boolean flag = jscCzjCyfczjService.insertOrUpdateListBatch();
//         if (flag) {
//             log.info("省级高新技术企业城市大脑数据对接更新成功");
//         } else {
//             log.error("省级高新技术企业城市大脑数据对接更新失败");
//         }
//     }
//
//     /**
//      * 自动获取短信是否发送成功状态
//      * * TODO 正式需打开
//      * 每隔3分钟 执行一次
//      */
//     @Scheduled(cron = "5 */3 * * * ?")
//     public void updateSmsMsgIsSend() {
//         String returnmsg = httpsMmsSend.rePortByhttps();
//         log.info(returnmsg);
//         if (!StringUtil.isEmpty(returnmsg) && !"[]".equals(returnmsg)) {
//             Object isobj = JSON.parse(returnmsg);
//             //判断是否是数组
//             if (isobj instanceof JSONArray) {
//                 JSONArray resultsArray = JSONObject.parseArray(returnmsg);
//                 List<SmsMessage> sucList = new ArrayList<>();
//                 for (int i = 0; i < resultsArray.size(); i++) {
//                     JSONObject obj = resultsArray.getJSONObject(i);
//                     String errorCode = obj.getString("errorCode");
//                     String mobile = obj.getString("mobile");
//                     String msgGroup = obj.getString("msgGroup");
//                     String receiveDate = obj.getString("receiveDate");
//                     SmsMessage sms = new SmsMessage();
//                     sms.setAccount(mobile);
//                     sms.setMgsGroup(msgGroup);
//                     sms.setExpireTime(receiveDate);
//                     if (!StringUtil.isEmpty(errorCode) && "DELIVRD".equals(errorCode)) {
//                         sms.setStatus("2");
//                     } else {
//                         sms.setStatus("0");
//                     }
//                     sucList.add(sms);
//                 }
//                 boolean flag = smsMessageService.updateListByPhones(sucList);
//                 if (flag) {
//                     log.info("更新短信数据成功，更新了" + sucList.size() + "条");
//                 } else {
//                     log.error("更新失败了" + sucList.size() + "条", "返回msg是" + returnmsg);
//                 }
//             }
//         }
//         List<SmsMessage> smsMessage = smsMessageService.queryByListForReload();
//         for (SmsMessage message : smsMessage) {
//         	Map<String, Object> map = httpsMmsSend.sendmmsByhttps(message.getAccount(), message.getCode());
//         	Boolean flag = (Boolean) map.get("success");
//         	String mgsGroup = map.get("mgsGroup").toString();
//         	if (flag) {
//         		message.setStatus("1");
//         		message.setMgsGroup(mgsGroup);
//         		smsMessageService.update(message);
//         	}
//         }
//         log.info("自动重发短信数据成功，重发了" + smsMessage.size() + "条");
//     }
//
//     /**
//      * 月末更新企业缴纳社保人数
//      * * TODO 正式需打开
//      * 每年？？后更新
//      */
// <<<<<<< .mine
//    @Scheduled(cron = "0 0 9 28-31 * ?")
// ||||||| .r874
//     //@Scheduled(cron = "0 0 9 28-31 * ?")
// =======
//     @Scheduled(cron = "0 0 1 1 * ?")
// >>>>>>> .r1236
//     public void updateBasiccompanyJnsbrs() {
//     	log.info("月末更新企业缴纳社保人数判断是否最后一天");
//     	final Calendar c = Calendar.getInstance();
//     	//if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
//     		//是最后一天
// 	    	log.info("月末更新企业缴纳社保人数开始");
// 	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
// 	    	c.setTime(new Date());
// 	    	c.add(Calendar.MONTH, -1);
// 	    	String month = sdf.format(c.getTime());
// 	    	System.out.println(month);
// 	    	//List<JscScjgjQyjbxx> bascilist = scjgjQyjbxxService.queryCompanyAll();
// 	    	//获取企业底库
// 	    	Map<String, Object> params = new HashMap<String, Object>();
// 	        List<Basiccompany> bascilist = basiccompanyService.queryByMapForSch(params);
// 	        List<BasiccompanyJnsbrs> list = new ArrayList<BasiccompanyJnsbrs>();
// 	        for (Basiccompany basiccompany : bascilist) {
// 	            if(!StringUtil.isEmpty(basiccompany.getUnified_code())&& !StringUtil.isEmpty(basiccompany.getLdCompanyName())){
// 	            	BasiccompanyJnsbrs basiccompanyJnsbrs = new BasiccompanyJnsbrs();
// 	                //查询参保人数
// 	                Shareholder holder = ShebaoUtil.compSocialSecurityNum(basiccompany.getUnified_code(), basiccompany.getLdCompanyName(),redisUtils);
// 	                if(holder!=null){
// 	                	basiccompanyJnsbrs.setLdCompanyId(basiccompany.getLdCompanyId());
// 	                	basiccompanyJnsbrs.setJnsbrs(holder.getCBZS());
// 	                	basiccompanyJnsbrs.setMonth(month);
// 	                	list.add(basiccompanyJnsbrs);
// 	                }
// 	            }
// 	        }
// 	        if(list != null && list.size()>0){
// 	        	basiccompanyJnsbrsService.insertList(list);
// 	        }
//     	//}
//         log.error("月末更新企业缴纳社保人数结束");
//     }
//
//    /**
//     * 月末统计数量
//     * * TODO 正式需打开
//     * 每年？？后更新
//     */
//    @Scheduled(cron = "0 0 2 1 * ?")
//    //@Scheduled(cron = "0 09 16 * * ?")
//    public void insertStatistics() {
// 	   //log.info("月末统计数量判断是否最后一天");
// 	   //final Calendar c = Calendar.getInstance();
// 	   //if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
// 		   //是最后一天
// 		   log.info("月初统计数量开始");
// 		   Statistics statistics =statisticsService.queryByStatistics();
// 		   try {
// 			   	String dataStr = "pageNo%3D1%26pageSize%3D10";
// 			   	dataStr = dataStr.replace(" ", "");
// 		    	//查询动态预警数量
// 		    	String path = "/portal/api/rcb/open/ai/warning/page";
// 		    	//String res = HttpClientUtil.getHttp(this.lljUrl + path + "?" + dataStr, "", "UTF-8");
// 		    	String res = null;
// 				log.info("查询动态预警数量返回结果："+res);
// 				JSONObject json = JSONObject.parseObject(res);
// 				if (json!=null && json.getInteger("CODE").intValue() == 200){
// 					JSONObject result = json.getJSONObject("RESULT");
// 					String total = result.getString("total");
// 					statistics.setDtyjCount(total);
// 				}else{
// 					statistics.setDtyjCount("0");
// 				}
// 		    	//查询企业获投数量
// 		    	path = "/portal/api/rcb/open/invest/events";
// 		    	//res = HttpClientUtil.getHttp(this.lljUrl + path + "?" + dataStr, "", "UTF-8");
// 				log.info("查询企业获投数量返回结果："+res);
// 				json = JSONObject.parseObject(res);
// 				if (json!=null && json.getInteger("CODE").intValue() == 200){
// 					JSONObject result = json.getJSONObject("RESULT");
// 					String total = result.getString("total");
// 					statistics.setQyhtCount(total);
// 				}else{
// 					statistics.setQyhtCount("0");
// 				}
//
// 		    	//查询专利数量
// 		    	path = "/portal/api/rcb/open/patent/firstAuth";
// 		    	//res = HttpClientUtil.getHttp(this.lljUrl + path + "?" + dataStr, "", "UTF-8");
// 				log.info("查询专利数量返回结果："+res);
// 				json = JSONObject.parseObject(res);
// 				if (json!=null && json.getInteger("CODE").intValue() == 200){
// 					JSONObject result = json.getJSONObject("RESULT");
// 					String total = result.getString("total");
// 					statistics.setZlsqCount(total);
// 				}else{
// 					statistics.setZlsqCount("0");
// 				}
// 				statistics.setOp("insert");
// 			} catch (Exception e) {
// 				e.printStackTrace();
// 			}
// 	    	statisticsService.insert(statistics);
// 		//}
// 		log.error("月初统计数量结束");
// 	}
//
//    @Scheduled(cron = "0 30 2 1 * ?")
//    //@Scheduled(cron = "0 01 14 * * ?")
//    public void insert5050Jh() {
// 	   //是最后一天
// 	   log.info("月初同步企业信息开始");
// 	   basiccompany5050jhService.insert(null);
// 	   basiccompanyCq5050jhService.insert(null);
// 	   basiccompanyGqjg5050jhService.insert(null);
// 	   basiccompanyJnsbrs5050jhService.insert(null);
// 	   basiccompanyJs5050jhService.insert(null);
// 	   basiccompanyQybq5050jhService.insert(null);
// 	   basiccompanyRz5050jhService.insert(null);
// 		log.error("月初同步企业信息结束");
// 	}
// }
