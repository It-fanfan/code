package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 系统管理人员-账号
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-17 21:13
 */
public class ManageUser {
    /**
     * 用户ID
     */
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 用户角色列表ID字符串
     */
    private String roleIds;
    /**
     * 用户角色列表名字
     */
    private String roleNames;
    /**
     * 是否允许登录
     */
    private boolean allowedLogin = false;
    /**
     * 数据更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;
    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date lastLoginTime;
    /**
     * 盐值，新增用户时使用
     */
    private String salt;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getNickName() { return nickName; }

    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getRoleIds() { return roleIds; }

    public void setRoleIds(String roleIds) { this.roleIds = roleIds; }

    public String getRoleNames() { return roleNames; }

    public void setRoleNames(String roleNames) { this.roleNames = roleNames; }

    public boolean isAllowedLogin() { return allowedLogin; }

    public void setAllowedLogin(boolean allowedLogin) { this.allowedLogin = allowedLogin; }

    public Date getUpdateTime() { return updateTime; }

    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public Date getLastLoginTime() { return lastLoginTime; }

    public void setLastLoginTime(Date lastLoginTime) { this.lastLoginTime = lastLoginTime; }

    public String getSalt() { return salt; }

    public void setSalt(String salt) { this.salt = salt; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("id = ").append(id);
        sb.append(", userName=").append(userName);
        sb.append(", password=").append(password);
        sb.append(", nickName=").append(nickName);
        sb.append(", roleIds=").append(roleIds);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}