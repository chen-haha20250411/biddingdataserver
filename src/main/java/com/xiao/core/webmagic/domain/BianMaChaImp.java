package com.xiao.core.webmagic.domain;

import java.io.Serializable;

/**
 * 进口税率
 */
public class BianMaChaImp implements Serializable {
    private static final long serialVersionUID = 1L;

    //最惠国进口税率
    private String importTaxRate;

    //普通进口税率:
    private String generalImportTaxRate;

    //暂定进口税率:
    private String provisionalImportTaxRate;

    //进口消费税:
    private String importConsumptionTax;

    //进口增值税:
    private String importValueAddedTax;

    //进口其他税:
    private String otherImportTaxes;

    //美国进口关税:
    private String usaImportTariffs;

    //备注
    private String remark;

    public String getImportTaxRate() {
        return importTaxRate;
    }

    public void setImportTaxRate(String importTaxRate) {
        this.importTaxRate = importTaxRate;
    }

    public String getGeneralImportTaxRate() {
        return generalImportTaxRate;
    }

    public void setGeneralImportTaxRate(String generalImportTaxRate) {
        this.generalImportTaxRate = generalImportTaxRate;
    }

    public String getProvisionalImportTaxRate() {
        return provisionalImportTaxRate;
    }

    public void setProvisionalImportTaxRate(String provisionalImportTaxRate) {
        this.provisionalImportTaxRate = provisionalImportTaxRate;
    }

    public String getImportConsumptionTax() {
        return importConsumptionTax;
    }

    public void setImportConsumptionTax(String importConsumptionTax) {
        this.importConsumptionTax = importConsumptionTax;
    }

    public String getImportValueAddedTax() {
        return importValueAddedTax;
    }

    public void setImportValueAddedTax(String importValueAddedTax) {
        this.importValueAddedTax = importValueAddedTax;
    }

    public String getOtherImportTaxes() {
        return otherImportTaxes;
    }

    public void setOtherImportTaxes(String otherImportTaxes) {
        this.otherImportTaxes = otherImportTaxes;
    }

    public String getUsaImportTariffs() {
        return usaImportTariffs;
    }

    public void setUsaImportTariffs(String usaImportTariffs) {
        this.usaImportTariffs = usaImportTariffs;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
