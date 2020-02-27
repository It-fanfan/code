package com.fish.utils;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

public class CcBuyDaily implements Serializable
{


    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comments(name = "id")
    private Integer id;

    @Column(name = "date")
    @Comments(name = "日期")
    private String date;

    @Column(name = "cd")
    @Comments(name = "渠道")
    private String cd;

    @Column(name = "payment")
    @Comments(name = "支出")
    private BigDecimal payment;

    @Column(name = "download_count")
    @Comments(name = "下载量")
    private Integer downloadCount;

    @Column(name = "download_payment")
    @Comments(name = "下载单价")
    private BigDecimal downloadPayment;

    @Column(name = "activation_count")
    @Comments(name = "激活")
    private Integer activationCount;

    @Column(name = "act_download_division")
    @NonExcel
    private Double actDownloadDivision;

    @Column(name = "registration_count")
    @Comments(name = "注册")
    private Integer registrationCount;

    @Column(name = "registration_payment")
    @Comments(name = "注册单价")
    private BigDecimal registrationPayment;

    @Column(name = "reg_act_division")
    @NonExcel
    private Double regActDivision;

    @Column(name = "activation_payment")
    @Comments(name = "激活单价")
    private BigDecimal activationPayment;

    @Transient
    @Comments(name = "下载转化")
    private String actDownloadDivisionText;

    @Transient
    @Comments(name = "注册转化")
    private String regActDivisionText;


    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }


    public String getCd()
    {
        return cd;
    }

    public void setCd(String cd)
    {
        this.cd = cd;
    }


    public BigDecimal getPayment()
    {
        return payment;
    }

    public void setPayment(BigDecimal payment)
    {
        this.payment = payment;
    }


    public Integer getDownloadCount()
    {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount)
    {
        this.downloadCount = downloadCount;
    }


    public BigDecimal getDownloadPayment()
    {
        return downloadPayment;
    }

    public void setDownloadPayment(BigDecimal downloadPayment)
    {
        this.downloadPayment = downloadPayment;
    }


    public Integer getActivationCount()
    {
        return activationCount;
    }

    public void setActivationCount(Integer activationCount)
    {
        this.activationCount = activationCount;
    }

    public Double getActDownloadDivision()
    {
        return actDownloadDivision;
    }

    public void setActDownloadDivision(Double actDownloadDivision)
    {
        this.actDownloadDivision = actDownloadDivision;
    }


    public Integer getRegistrationCount()
    {
        return registrationCount;
    }

    public void setRegistrationCount(Integer registrationCount)
    {
        this.registrationCount = registrationCount;
    }

    public BigDecimal getRegistrationPayment()
    {
        return registrationPayment;
    }

    public void setRegistrationPayment(BigDecimal registrationPayment)
    {
        this.registrationPayment = registrationPayment;
    }


    public Double getRegActDivision()
    {
        return regActDivision;
    }

    public void setRegActDivision(Double regActDivision)
    {
        this.regActDivision = regActDivision;
    }


    public BigDecimal getActivationPayment()
    {
        return activationPayment;
    }

    public void setActivationPayment(BigDecimal activationPayment)
    {
        this.activationPayment = activationPayment;
    }


    public String getActDownloadDivisionText()
    {
        return actDownloadDivisionText;
    }

    public void setActDownloadDivisionText(String actDownloadDivisionText)
    {
        this.actDownloadDivisionText = actDownloadDivisionText;
    }

    public String getRegActDivisionText()
    {
        return regActDivisionText;
    }

    public void setRegActDivisionText(String regActDivisionText)
    {
        this.regActDivisionText = regActDivisionText;
    }
}
