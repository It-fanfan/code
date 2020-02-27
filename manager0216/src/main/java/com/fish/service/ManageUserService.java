package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ManageRoleMapper;
import com.fish.dao.primary.mapper.ManageUserMapper;
import com.fish.dao.primary.model.ManageRole;
import com.fish.dao.primary.model.ManageUser;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.utils.XwhTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统管理人员-账号
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-17 21:13
 */
@Service
public class ManageUserService implements BaseService<ManageUser> {

    @Autowired
    private ManageUserMapper manageUserMapper;

    @Autowired
    private ManageRoleMapper manageRoleMapper;

    public ManageUser login(String userName) { return manageUserMapper.login(userName); }

    @Override
    public void setDefaultSort(GetParameter parameter) { }

    @Override
    public Class<ManageUser> getClassInfo() { return ManageUser.class; }

    @Override
    public boolean removeIf(ManageUser manageUser, JSONObject searchData) {
        return false;
    }

    /**
     * 查询全部用户
     *
     * @param parameter
     * @return
     */
    @Override
    public List<ManageUser> selectAll(GetParameter parameter) {
        // 查询全部用户
        List<ManageUser> manageUserList = this.manageUserMapper.selectAll();
        // 查询全部角色
        List<ManageRole> manageRoleList = this.manageRoleMapper.selectAll();
        Map<Integer, String> manageRoleMap = new HashMap<>(manageRoleList.size());
        for (ManageRole manageRole : manageRoleList) {
            manageRoleMap.put(manageRole.getId(), manageRole.getName());
        }
        for (ManageUser manageUser : manageUserList) {
            // 根据角色拥有的角色ID得到角色名称
            String roleNames = "#";
            if (!StringUtils.isBlank(manageUser.getRoleIds())) {
                for (String roleId : manageUser.getRoleIds().split("#")) {
                    String roleName = manageRoleMap.get(Integer.valueOf(roleId));
                    if (!StringUtils.isBlank(roleName)) {
                        roleNames += roleName + "#";
                    }
                }
            }
            manageUser.setRoleNames(roleNames);
            // 将用户密码置空
            manageUser.setPassword(null);
        }
        return manageUserList;
    }

    /**
     * 新增用户
     *
     * @param manageUser
     * @return
     */
    public PostResult insert(ManageUser manageUser) {
        PostResult postResult = new PostResult();
        // 先调用login方法判断用户名是否存在
        ManageUser existUser = this.manageUserMapper.login(manageUser.getUserName());
        if (existUser == null) {
            if (StringUtils.isBlank(manageUser.getPassword())) {
                manageUser.setPassword(XwhTool.getMD5Encode(manageUser.getUserName() + "123456"));
            } else {
                manageUser.setPassword(XwhTool.getMD5Encode(manageUser.getUserName() + manageUser.getPassword()));
            }
            int id = this.manageUserMapper.insert(manageUser);
            if (id > 0) {
                postResult.setSuccessed(true);
                postResult.setMsg("操作成功！");
            } else {
                postResult.setSuccessed(false);
                postResult.setMsg("操作失败，请联系管理员！");
            }
        } else {
            postResult.setSuccessed(false);
            postResult.setMsg("操作失败，当前用户已存在！");
        }
        return postResult;
    }

    /**
     * 管理员修改用户信息：账号、密码、昵称、权限信息、允许登录状态
     *
     * @param manageUser
     */
    public PostResult update(ManageUser manageUser) {
        PostResult postResult = new PostResult();
        // 先调用login方法判断用户名是否存在
        ManageUser existUser = this.manageUserMapper.login(manageUser.getUserName());
        if (existUser == null || existUser.getId().equals(manageUser.getId())) {
            // 为空则不修改密码
            if (StringUtils.isBlank(manageUser.getPassword())) {
                manageUser.setPassword(null);
            } else {
                manageUser.setPassword(XwhTool.getMD5Encode(manageUser.getUserName() + manageUser.getPassword()));
            }
            int update = this.manageUserMapper.update(manageUser);
            if (update > 0) {
                postResult.setSuccessed(true);
                postResult.setMsg("操作成功！");
            } else {
                postResult.setSuccessed(false);
                postResult.setMsg("操作失败，请联系管理员！");
            }
        } else {
            postResult.setSuccessed(false);
            postResult.setMsg("操作失败，当前用户已存在！");
        }
        return postResult;
    }

    /**
     * 用户自行修改信息：账号、密码、昵称
     *
     * @param jsonObject
     * @param manageUser
     */
    public PostResult updateByUserSelf(JSONObject jsonObject, ManageUser manageUser) {
        PostResult updateResult = new PostResult();

        String newPassword = jsonObject.getString("newPassword");
        // 1、如果有输入新密码，对用户提交的旧密码进行加密，用以判断旧密码是否正确，注意此时要用旧的用户名
        if (!StringUtils.isBlank(newPassword)) {
            ManageUser existUser = this.manageUserMapper.select(manageUser.getId());
            String md5Password = XwhTool.getMD5Encode(manageUser.getUserName() + jsonObject.getString("oldPassword"));
            if (!existUser.getPassword().equals(md5Password)) {
                updateResult.setSuccessed(false);
                updateResult.setMsg("操作失败，旧密码错误！");
                return updateResult;
            }
        }

        // 2、判断用户名是否存在
        ManageUser existUser = this.manageUserMapper.login(manageUser.getUserName());
        if (existUser == null || existUser.getId().equals(manageUser.getId())) {
            // 3、赋值保存
            manageUser.setNickName(jsonObject.getString("nickName"));
            manageUser.setUserName(jsonObject.getString("userName"));
            if (StringUtils.isBlank(newPassword)) {
                manageUser.setPassword(null);
            } else {
                manageUser.setPassword(XwhTool.getMD5Encode(jsonObject.getString("userName") + newPassword));
            }
            int update = this.manageUserMapper.updateByUserSelf(manageUser);
            if (update <= 0) {
                updateResult.setSuccessed(false);
                updateResult.setMsg("系统错误，请联系管理员！");
            }
        } else {
            updateResult.setSuccessed(false);
            updateResult.setMsg("用户名存在！");
        }
        return updateResult;
    }

    /**
     * 系统管理员删除用户
     *
     * @param deleteIds
     */
    public int delete(String deleteIds) {return this.manageUserMapper.delete(deleteIds);}

    /**
     * 记录用户最后登录时间
     *
     * @param userName
     */
    public void recodeLastLoginTime(String userName) {this.manageUserMapper.recodeLastLoginTime(userName);}

    /**
     * 查询所有角色和已授权角色，按照layui的树状结构data下发
     *
     * @param id
     * @return
     */
    public JSONArray getAuthorizationRole(String id) {
        JSONArray jsonArray = new JSONArray();
        // 当前用户已授权角色
        List<String> authorizedRoleList = new ArrayList<>();
        if (!StringUtils.isBlank(id)) {
            // 如果是管理员请求参数，直接返回空对象
            if ("1".equals(id)) {
                return jsonArray;
            }
            // 其它用户查询当前用户已授权权限
            ManageUser manageUser = this.manageUserMapper.select(Integer.parseInt(id));
            if (manageUser != null && !StringUtils.isBlank(manageUser.getRoleIds())) {
                authorizedRoleList = Arrays.asList(manageUser.getRoleIds().split("#"));
            }
        }

        // 所有角色
        List<ManageRole> allManageRoles = this.manageRoleMapper.selectAll();
        for (ManageRole manageRole : allManageRoles) {
            // 不给普通用户授权为管理员
            if (manageRole.getId() != 1) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", manageRole.getId());
                jsonObject.put("title", manageRole.getName());
                jsonObject.put("checked", authorizedRoleList.contains(String.valueOf(manageRole.getId())));
                jsonArray.add(jsonObject);
            }
        }
        System.out.println(jsonArray);
        return jsonArray;
    }
}

