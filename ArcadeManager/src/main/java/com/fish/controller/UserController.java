package com.fish.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/user")
public class UserController
{
    @ResponseBody
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password)
    {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        System.out.println("token : "+token);
        try
        {
            subject.login(token);
        } catch (UnknownAccountException uae)
        {
            return "未知账户";
        } catch (IncorrectCredentialsException ice)
        {
            return "密码不正确";
        } catch (LockedAccountException lae)
        {
            return "账户已锁定";
        } catch (ExcessiveAttemptsException eae)
        {
            return "用户名或密码错误次数过多";
        } catch (AuthenticationException ae)
        {
            return "用户名或密码不正确！";
        }
        if (subject.isAuthenticated())
        {
            return "登录成功";
        } else
        {
            token.clear();
            return "登录失败";
        }
    }

    @ResponseBody
    @GetMapping("/logout")
    public String select()
    {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "成功登出";
    }
}
