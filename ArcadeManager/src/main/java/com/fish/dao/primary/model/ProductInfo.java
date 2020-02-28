package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProductInfo {
    //产品名称
    private String productName;
    //平台
    private String platform;
    //Appid
    private String appId;
    //参数1
    private String paramOne;
    //参数2
    private String paramTwo;
    //登陆账号
    private String loginId;
    //登陆密码
    private String loginPassword;
    //管理员
    private String admin;
    //管理员信息
    private String adminInfo;
    //结算公司
    private String settleCompany;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getParamOne() {
        return paramOne;
    }

    public void setParamOne(String paramOne) {
        this.paramOne = paramOne;
    }

    public String getParamTwo() {
        return paramTwo;
    }

    public void setParamTwo(String paramTwo) {
        this.paramTwo = paramTwo;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAdminInfo() {
        return adminInfo;
    }

    public void setAdminInfo(String adminInfo) {
        this.adminInfo = adminInfo;
    }

    public String getSettleCompany() {
        return settleCompany;
    }

    public void setSettleCompany(String settleCompany) {
        this.settleCompany = settleCompany;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "productName='" + productName + '\'' +
                ", platform='" + platform + '\'' +
                ", appId='" + appId + '\'' +
                ", paramOne='" + paramOne + '\'' +
                ", paramTwo='" + paramTwo + '\'' +
                ", loginId='" + loginId + '\'' +
                ", loginPassword='" + loginPassword + '\'' +
                ", admin='" + admin + '\'' +
                ", adminInfo='" + adminInfo + '\'' +
                ", settleCompany='" + settleCompany + '\'' +
                '}';
    }
}
