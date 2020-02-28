package com.fish.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.ManageUser;
import com.fish.service.ManageRoleService;
import com.fish.service.ManageUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

/**
 * 系统管理员登录、登出、菜单查询<br>
 * 用户信息修改授权等请跳转ManageUserController
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-18 22:19
 */
@Controller
@RequestMapping(value = "/userLog")
public class ManageUserLogController {

    @Autowired
    ManageUserService manageUserService;

    @Autowired
    ManageRoleService manageRoleService;

    @ResponseBody
    @PostMapping("/login")
    public JSONObject userLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        JSONObject loginJson = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setTimeout(7200000);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            if (subject.isAuthenticated()) {
                loginJson.put("code", 0);
                loginJson.put("msg", "登录成功");
                loginJson.put("username", username);
                this.manageUserService.recodeLastLoginTime(username);
            } else {
                token.clear();
                loginJson.put("code", 1);
                loginJson.put("msg", "登录失败");
            }
        } catch (UnknownAccountException uae) {
            loginJson.put("code", 2);
            loginJson.put("msg", "未知账户");
        } catch (IncorrectCredentialsException ice) {
            loginJson.put("code", 3);
            loginJson.put("msg", "密码不正确");
        } catch (LockedAccountException lae) {
            loginJson.put("code", 4);
            loginJson.put("msg", "账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            loginJson.put("code", 5);
            loginJson.put("msg", "用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            loginJson.put("code", 6);
            loginJson.put("msg", "用户名或密码不正确");
        }
        return loginJson;
    }

    @ResponseBody
    @GetMapping("/getUserInfo")
    public JSONObject getUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        ManageUser manageUser = (ManageUser) subject.getPrincipal();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", manageUser.getUserName());
        jsonObject.put("nickName", manageUser.getNickName());
        return jsonObject;
    }

    /**
     * 获取已授权菜单
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/getAuthorizedMenu")
    public JSONArray getAuthorizedMenu() {
        // 获取用户及用户权限信息
        Set<String> authorizedPages = null;
        Subject subject = SecurityUtils.getSubject();
        ManageUser manageUser = (ManageUser) subject.getPrincipal();
        if (manageUser == null || StringUtils.isBlank(manageUser.getRoleIds())) {
            return new JSONArray();
        }
        return this.manageRoleService.getAuthorizedMenu(manageUser.getRoleIds());
    }

    @ResponseBody
    @GetMapping("/logout")
    public String select() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "成功登出";
    }

}
