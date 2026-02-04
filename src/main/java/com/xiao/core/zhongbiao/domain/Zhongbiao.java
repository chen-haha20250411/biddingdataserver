package com.xiao.core.zhongbiao.domain;

import com.xiao.base.BaseDomain;
public class Zhongbiao extends BaseDomain{
	
	private static final long serialVersionUID = 1L;
	private String id;
    //@ApiModelProperty(value = "中标日期")
   	private String publishDate;
    //@ApiModelProperty(value = "项目的唯一编号")
   	private String projectNo;
    //@ApiModelProperty(value = "招标的客户名称")
   	private String customer;
    //@ApiModelProperty(value = "招标的类型")
   	private String noticeType;
    //@ApiModelProperty(value = "招标项目的标题")
   	private String title;
    //@ApiModelProperty(value = "中标货品的名称，可能较长，使用 TEXT 类型")
   	private String productLabels;
    //@ApiModelProperty(value = "中标合计金额，保留两位小数")
   	private String winnerAmount;
    //@ApiModelProperty(value = "相关备注信息，可能较长，使用 TEXT 类型")
   	private String remark;
    //@ApiModelProperty(value = "中标的供应商名称")
   	private String winnerPrincipal;

    private String html_content;


	public String getHtml_content() {
		return html_content;
	}

	public void setHtml_content(String html_content) {
		this.html_content = html_content;
	}

	private String html_url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPublishDate(){
    	return publishDate;
    }
    	
    public void setPublishDate(String publishDate){
		this.publishDate=publishDate;
	}

    public String getProjectNo(){ 
    	return projectNo;
    }
    	
    public void setProjectNo(String projectNo){
		this.projectNo=projectNo;
	}

    public String getCustomer(){ 
    	return customer;
    }
    	
    public void setCustomer(String customer){
		this.customer=customer;
	}

    public String getNoticeType(){ 
    	return noticeType;
    }
    	
    public void setNoticeType(String noticeType){
		this.noticeType=noticeType;
	}

    public String getTitle(){ 
    	return title;
    }
    	
    public void setTitle(String title){
		this.title=title;
	}

    public String getProductLabels(){ 
    	return productLabels;
    }
    	
    public void setProductLabels(String productLabels){
		this.productLabels=productLabels;
	}

    public String getWinnerAmount(){ 
    	return winnerAmount;
    }
    	
    public void setWinnerAmount(String winnerAmount){
		this.winnerAmount=winnerAmount;
	}

    public String getRemark(){ 
    	return remark;
    }
    	
    public void setRemark(String remark){
		this.remark=remark;
	}

    public String getWinnerPrincipal(){ 
    	return winnerPrincipal;
    }
    	
    public void setWinnerPrincipal(String winnerPrincipal){
		this.winnerPrincipal=winnerPrincipal;
	}

	public String getHtml_url(){
		return html_url;
	}

	public void setHtml_url(String html_url){
		this.html_url=html_url;
	}


}