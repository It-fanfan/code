package com.fish.protocols;

import com.alibaba.fastjson.JSONObject;
import javafx.geometry.Pos;

/**
 * 封装的数据提交结果
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-24 21:13
 */
public class PostResult {
    /**
     * Json对象key-是否成功
     */
    private static final String SUCCESS_ED = "successed";
    /**
     * Json对象key-详细信息
     */
    private static final String MSG = "msg";

    /**
     * 数据存储Json对象
     */
    private JSONObject jsonObject = new JSONObject();

    public PostResult() {
        this.jsonObject.put(SUCCESS_ED, true);
        this.jsonObject.put(MSG, "操作成功！");
    }

    public PostResult(boolean successed, String msg) {
        this.jsonObject.put(SUCCESS_ED, successed);
        this.jsonObject.put(MSG, msg);
    }

    public boolean isSuccessed() { return this.jsonObject.getBoolean(SUCCESS_ED); }

    public void setSuccessed(boolean successed) { this.jsonObject.put(SUCCESS_ED, successed); }

    public String getMsg() { return this.jsonObject.getString(MSG); }

    public void setMsg(String msg) { this.jsonObject.put(MSG, msg); }

    @Override
    public String toString() {
        return this.jsonObject.toJSONString();
    }
}
