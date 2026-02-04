package com.xiao.core.webmagic.domain;

import java.io.Serializable;

/**
 * 出口税率
 */
public class BianMaChaExp implements Serializable {
    private static final long serialVersionUID = 1L;

    //出口退税:
    private String exportTaxRebate;

    //出口关税:
    private String exportDuties;

    //出口增值税
    private String exportVAT;

    //临时税率:
    private String temporaryTaxRate;

    //备注
    private String remark;

    public String getExportTaxRebate() {
        return exportTaxRebate;
    }

    public void setExportTaxRebate(String exportTaxRebate) {
        this.exportTaxRebate = exportTaxRebate;
    }

    public String getExportDuties() {
        return exportDuties;
    }

    public void setExportDuties(String exportDuties) {
        this.exportDuties = exportDuties;
    }

    public String getExportVAT() {
        return exportVAT;
    }

    public void setExportVAT(String exportVAT) {
        this.exportVAT = exportVAT;
    }

    public String getTemporaryTaxRate() {
        return temporaryTaxRate;
    }

    public void setTemporaryTaxRate(String temporaryTaxRate) {
        this.temporaryTaxRate = temporaryTaxRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
