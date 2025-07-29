package com.xiao.core.webmagic.domain;

import java.io.Serializable;

/**
 * 贸易数据
 */
public class BianMaChaDst implements Serializable {

    private static final long serialVersionUID = 1L;
    //年份
    private String years;

    //出口总量 （千克）
    private String totalNumExports;

    //出口总额 （美元）
    private String totalAmountExports;

    //出口均价 （美元）
    private String averageExportPrice;

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getTotalNumExports() {
        return totalNumExports;
    }

    public void setTotalNumExports(String totalNumExports) {
        this.totalNumExports = totalNumExports;
    }

    public String getTotalAmountExports() {
        return totalAmountExports;
    }

    public void setTotalAmountExports(String totalAmountExports) {
        this.totalAmountExports = totalAmountExports;
    }

    public String getAverageExportPrice() {
        return averageExportPrice;
    }

    public void setAverageExportPrice(String averageExportPrice) {
        this.averageExportPrice = averageExportPrice;
    }
}
