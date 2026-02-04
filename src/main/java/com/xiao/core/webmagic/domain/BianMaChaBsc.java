package com.xiao.core.webmagic.domain;

import java.io.Serializable;

/**
 * 基本信息
 */
public class BianMaChaBsc implements Serializable{
    private static final long serialVersionUID = 1L;

    //HS编码:
    private String hscode;

    //中文描述:
    private String cndesc;

    //CIQ代码:
    private String ciqcode;

    //英文描述:
    private String endesc;

    //申报要素:
    private String declelements;

    //申报要素举例
    private String declelementsexp;

    //单位:
    private String descunit;

    //监管条件
    private String monitoringCondit;

    //监管条件
    private String quarantineCondit;



    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getCndesc() {
        return cndesc;
    }

    public void setCndesc(String cndesc) {
        this.cndesc = cndesc;
    }

    public String getCiqcode() {
        return ciqcode;
    }

    public void setCiqcode(String ciqcode) {
        this.ciqcode = ciqcode;
    }

    public String getEndesc() {
        return endesc;
    }

    public void setEndesc(String endesc) {
        this.endesc = endesc;
    }

    public String getDeclelements() {
        return declelements;
    }

    public void setDeclelements(String declelements) {
        this.declelements = declelements;
    }

    public String getDeclelementsexp() {
        return declelementsexp;
    }

    public void setDeclelementsexp(String declelementsexp) {
        this.declelementsexp = declelementsexp;
    }

    public String getDescunit() {
        return descunit;
    }

    public void setDescunit(String descunit) {
        this.descunit = descunit;
    }

    public String getMonitoringCondit() {
        return monitoringCondit;
    }

    public void setMonitoringCondit(String monitoringCondit) {
        this.monitoringCondit = monitoringCondit;
    }

    public String getQuarantineCondit() {
        return quarantineCondit;
    }

    public void setQuarantineCondit(String quarantineCondit) {
        this.quarantineCondit = quarantineCondit;
    }
}
