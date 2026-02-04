package com.xiao.core.webmagic.domain;

import java.io.Serializable;

/**
 * 申报实例
 */
public class BianMaChaCase  implements Serializable{
    private static final long serialVersionUID = 1L;

    //hscode
    private String hscode;

    //申报名称
    private String declarationName;

    //申报要素
    private String elementsOfDeclaration;

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getDeclarationName() {
        return declarationName;
    }

    public void setDeclarationName(String declarationName) {
        this.declarationName = declarationName;
    }

    public String getElementsOfDeclaration() {
        return elementsOfDeclaration;
    }

    public void setElementsOfDeclaration(String elementsOfDeclaration) {
        this.elementsOfDeclaration = elementsOfDeclaration;
    }
}
