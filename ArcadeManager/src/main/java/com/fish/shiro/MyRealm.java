package com.fish.shiro;

import com.fish.dao.primary.model.User;
import com.fish.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

public class MyRealm extends AuthorizingRealm
{
    private final static Logger logger = LoggerFactory.getLogger(MyRealm.class);
    @Autowired
    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
    {
        logger.debug("##################执行Shiro权限认证##################");
        User user = (User) principalCollection.getPrimaryPrincipal();
        if (user != null)
        {
            System.out.println("我是user : "+user.toString());
            Set<String> roles = new HashSet<>();
            Set<String> permissions = new HashSet<>();
            roles.add("user");
            permissions.add("user:query");
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
            info.setStringPermissions(permissions);
            System.out.println("我是info : "+info.toString());
            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException
    {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        logger.info("用户验证执行 : " + token.getUsername());

        User user = userService.selectByUserName(token.getUsername());
        if (user == null)
        {
            logger.error("用户 { " + token.getUsername() + " } 不存在 ");
            throw new AccountException("账户不存在");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }


    @PostConstruct
    public void initCredentialsMatcher()
    {
        //该句作用是重写shiro的密码验证，让shiro用我自己的验证
        setCredentialsMatcher(new CredentialsMatcher());

    }
}
