package com.fish.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ConfigurationProperties(prefix = "config")
public class BaseConfig {
    private String upload;

    private String domain;

    private String excelSave;

    private String program_SEND_URL;

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getExcelSave() {
        return excelSave;
    }

    public void setExcelSave(String excelSave) {
        this.excelSave = excelSave;
    }

    public String getProgram_SEND_URL() {
        return program_SEND_URL;
    }

    public void setProgram_SEND_URL(String program_SEND_URL) {
        this.program_SEND_URL = program_SEND_URL;
    }
}
