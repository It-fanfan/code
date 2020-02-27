package com.fish.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.ManageUser;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ManageUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 系统管理人员信息修改，授权<br>
 * 系统管理员登录、登出、菜单查询跳转 ManageUserLogController
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-17 21:13
 */
@Controller
@RequestMapping(value = "/manage")
public class ManageUserController {

    @Autowired
    ManageUserService manageUserService;

    /**
     * 查询所有管理员用户
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/manageUser")
    public GetResult getManageUser(GetParameter parameter) {
        return this.manageUserService.findAll(parameter);
    }

    /**
     * 新增管理员用户
     *
     * @param manageUser
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/manageUser/new")
    public PostResult newManageUser(@RequestBody ManageUser manageUser) {
        return this.manageUserService.insert(manageUser);
    }

    /**
     * 管理员修改用户信息：账号、密码、昵称、权限信息、允许登录状态
     *
     * @param manageUser
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/manageUser/edit")
    public PostResult edit(@RequestBody ManageUser manageUser) {
        return this.manageUserService.update(manageUser);
    }

    /**
     * 用户自行修改信息：账号、密码、昵称
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/manageUser/updateByUser")
    public PostResult updateByUser(@RequestBody JSONObject jsonObject) {
        Subject subject = SecurityUtils.getSubject();
        ManageUser manageUser = (ManageUser) subject.getPrincipal();
        return this.manageUserService.updateByUserSelf(jsonObject, manageUser);
    }

    /**
     * 系统管理员删除用户
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/manageUser/delete")
    public PostResult delete(@RequestBody JSONObject jsonObject) {
        PostResult result = new PostResult();
        int update = this.manageUserService.delete(jsonObject.getString("deleteIds"));
        if (update > 0) {

            result.setMsg("操作成功");
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }


    /**
     * 查询所有角色和已授权角色，按照layui的树状结构data下发
     *
     * @param id 用户ID
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/manageUser/getAuthorizationRole")
    public JSONArray getAuthorizationRole(String id) { return this.manageUserService.getAuthorizationRole(id); }
}
