package com.fish.protocols;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class GetResult<T> {
    private int count;
    private List<T> data;
    private Object footer;
    private int code;
    private String msg;
    //折线图统计
    private JSONObject lineData;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Object getFooter() {
        return footer;
    }

    public void setFooter(Object footer) {
        this.footer = footer;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getLineData() {
        return lineData;
    }

    public void setLineData(JSONObject lineData) {
        this.lineData = lineData;
    }
}
