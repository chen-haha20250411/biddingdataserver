package com.xiao.core.webmagic.domain;

import java.io.Serializable;

/**
 *
 */
public class BianMaChaCmp implements Serializable {
    private static final long serialVersionUID = 1L;
    //出进口
    private String inorout;

    //企业名称
    private String companyName;

    //年出口规模
    private String annualExportScale;

    public String getInorout() {
        return inorout;
    }

    public void setInorout(String inorout) {
        this.inorout = inorout;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAnnualExportScale() {
        return annualExportScale;
    }

    public void setAnnualExportScale(String annualExportScale) {
        this.annualExportScale = annualExportScale;
    }
}
