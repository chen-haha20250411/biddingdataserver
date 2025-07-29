package com.xiao.core.biddingInfo.domain;

import com.xiao.base.BaseDomain;
public class BiddingInfo extends BaseDomain{
	
	private static final long serialVersionUID = 1L;

    //@ApiModelProperty(value = "")
   	private String id;
    //@ApiModelProperty(value = "")
   	private String projectNumber;
    //@ApiModelProperty(value = "")
   	private String projectName;
    //@ApiModelProperty(value = "")
   	private String publishDate;
    //@ApiModelProperty(value = "")
   	private String content;
    //@ApiModelProperty(value = "")
   	private String projectId;
    //@ApiModelProperty(value = "")
   	private String totalContent;
    //@ApiModelProperty(value = "")
   	private String dataSource;
    //@ApiModelProperty(value = "")
   	private String htmlUrl;

    public String getId(){ 
    	return id;
    }
    	
    public void setId(String id){
		this.id=id;
	}

    public String getProjectNumber(){ 
    	return projectNumber;
    }
    	
    public void setProjectNumber(String projectNumber){
		this.projectNumber=projectNumber;
	}

    public String getProjectName(){ 
    	return projectName;
    }
    	
    public void setProjectName(String projectName){
		this.projectName=projectName;
	}

    public String getPublishDate(){ 
    	return publishDate;
    }
    	
    public void setPublishDate(String publishDate){
		this.publishDate=publishDate;
	}

    public String getContent(){ 
    	return content;
    }
    	
    public void setContent(String content){
		this.content=content;
	}

    public String getProjectId(){ 
    	return projectId;
    }
    	
    public void setProjectId(String projectId){
		this.projectId=projectId;
	}

    public String getTotalContent(){ 
    	return totalContent;
    }
    	
    public void setTotalContent(String totalContent){
		this.totalContent=totalContent;
	}

    public String getDataSource(){ 
    	return dataSource;
    }
    	
    public void setDataSource(String dataSource){
		this.dataSource=dataSource;
	}

    public String getHtmlUrl(){ 
    	return htmlUrl;
    }
    	
    public void setHtmlUrl(String htmlUrl){
		this.htmlUrl=htmlUrl;
	}

}