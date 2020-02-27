package com.fish.dao.primary.model;

import java.util.Date;

public class ArcadeProductInfo {
    private Long id;

    private String productName;

    private String orName;

    private String programType;

    private String gameType;

    private String appId;

    private String orId;

    private String appsec;

    private String addId;

    private String payKey;

    private String merchantNumber;

    private String account;

    private String password;

    private String codeManager;

    private String belongCompany;

    private String clearCompany;

    private Date createTime;

    private Date updateTime;

    private String linkAddress;

    private String formalCollection;

    private String proveVersionNum;

    private String proveContent;

    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getOrName() {
        return orName;
    }

    public void setOrName(String orName) {
        this.orName = orName == null ? null : orName.trim();
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType == null ? null : programType.trim();
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType == null ? null : gameType.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getOrId() {
        return orId;
    }

    public void setOrId(String orId) {
        this.orId = orId == null ? null : orId.trim();
    }

    public String getAppsec() {
        return appsec;
    }

    public void setAppsec(String appsec) {
        this.appsec = appsec == null ? null : appsec.trim();
    }

    public String getAddId() {
        return addId;
    }

    public void setAddId(String addId) {
        this.addId = addId == null ? null : addId.trim();
    }

    public String getPayKey() {
        return payKey;
    }

    public void setPayKey(String payKey) {
        this.payKey = payKey == null ? null : payKey.trim();
    }

    public String getMerchantNumber() {
        return merchantNumber;
    }

    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber == null ? null : merchantNumber.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getCodeManager() {
        return codeManager;
    }

    public void setCodeManager(String codeManager) {
        this.codeManager = codeManager == null ? null : codeManager.trim();
    }

    public String getBelongCompany() {
        return belongCompany;
    }

    public void setBelongCompany(String belongCompany) {
        this.belongCompany = belongCompany == null ? null : belongCompany.trim();
    }

    public String getClearCompany() {
        return clearCompany;
    }

    public void setClearCompany(String clearCompany) {
        this.clearCompany = clearCompany == null ? null : clearCompany.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress == null ? null : linkAddress.trim();
    }

    public String getFormalCollection() {
        return formalCollection;
    }

    public void setFormalCollection(String formalCollection) {
        this.formalCollection = formalCollection == null ? null : formalCollection.trim();
    }

    public String getProveVersionNum() {
        return proveVersionNum;
    }

    public void setProveVersionNum(String proveVersionNum) {
        this.proveVersionNum = proveVersionNum == null ? null : proveVersionNum.trim();
    }

    public String getProveContent() {
        return proveContent;
    }

    public void setProveContent(String proveContent) {
        this.proveContent = proveContent == null ? null : proveContent.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productName=").append(productName);
        sb.append(", orName=").append(orName);
        sb.append(", programType=").append(programType);
        sb.append(", gameType=").append(gameType);
        sb.append(", appId=").append(appId);
        sb.append(", orId=").append(orId);
        sb.append(", appsec=").append(appsec);
        sb.append(", addId=").append(addId);
        sb.append(", payKey=").append(payKey);
        sb.append(", merchantNumber=").append(merchantNumber);
        sb.append(", account=").append(account);
        sb.append(", password=").append(password);
        sb.append(", codeManager=").append(codeManager);
        sb.append(", belongCompany=").append(belongCompany);
        sb.append(", clearCompany=").append(clearCompany);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", linkAddress=").append(linkAddress);
        sb.append(", formalCollection=").append(formalCollection);
        sb.append(", proveVersionNum=").append(proveVersionNum);
        sb.append(", proveContent=").append(proveContent);
        sb.append(", remarks=").append(remarks);
        sb.append("]");
        return sb.toString();
    }
}