package com.fish.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.ManageRole;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ManageRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 角色管理Controller
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-18 20:16
 */
@Controller
@RequestMapping(value = "/manage")
public class ManageRoleController {

    @Autowired
    ManageRoleService manageRoleService;

    /**
     * 查询所有角色
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/manageRole")
    public GetResult getManageRole(GetParameter parameter) {
        return this.manageRoleService.findAll(parameter);
    }

    /**
     * 新增角色，由于要自行处理权限的页面字符串，所以用JSONObject接收数据
     *
     * @param manageRole
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/manageRole/new")
    public PostResult newManageUser(@RequestBody ManageRole manageRole) {
        PostResult result = new PostResult();
        int id = this.manageRoleService.insert(manageRole);
        if (id > 0) {
            result.setMsg("操作成功");
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 修改角色，由于要自行处理权限的页面字符串，所以用JSONObject接收数据
     *
     * @param manageRole
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/manageRole/edit")
    public PostResult updateByAdmin(@RequestBody ManageRole manageRole) {
        PostResult result = new PostResult();
        if (manageRole.getId() == 1) {
            result.setMsg("操作成功，但是【超级管理员】角色不允许修改！");
        } else {
            int update = this.manageRoleService.update(manageRole);
            if (update > 0) {
                result.setMsg("操作成功");
            } else {
                result.setSuccessed(false);
                result.setMsg("操作失败，请联系管理员");
            }
        }
        return result;
    }

    /**
     * 删除角色，提交的是“,”分隔的ID
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/manageRole/delete")
    public PostResult delete(@RequestBody JSONObject jsonObject) {
        PostResult result = new PostResult();
        int update = this.manageRoleService.delete(jsonObject.getString("deleteIds"));
        if (update > 0) {

            result.setMsg("操作成功");
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 获取配置菜单，将菜单处理成符合layui树形组件的模式，参考地址：<br/>
     * https://www.layui.com/doc/modules/tree.html
     *
     * @param id 角色ID
     * @return 角色授权菜单Json
     */
    @ResponseBody
    @GetMapping(value = "/manageRole/getAuthorizationMenu")
    public JSONArray getAuthorizationMenu(String id) { return this.manageRoleService.getAuthorizationMenu(id); }

}
