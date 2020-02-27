package com.fish.shiro;

import com.fish.dao.primary.model.ManageUser;
import com.fish.service.ManageUserService;
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

/**
 * 系统管理人员-权限管理
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-18 16:13
 */
public class ManageUserRealm extends AuthorizingRealm {

    private final static Logger logger = LoggerFactory.getLogger(ManageUserRealm.class);

    @Autowired
    ManageUserService manageUserService;

    /**
     * TODO 设置授权信息，后续再优化
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.debug("##################执行Shiro权限认证##################");
        ManageUser manageAccount = (ManageUser) principalCollection.getPrimaryPrincipal();
        if (manageAccount != null) {
            Set<String> roles = new HashSet<>();
            Set<String> permissions = new HashSet<>();
            roles.add(manageAccount.getRoleIds());
            permissions.add("user:query");
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
            info.setStringPermissions(permissions);
            return info;
        }
        return null;
    }

    /**
     * 管理登录认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        ManageUser manageUser = manageUserService.login(token.getUsername());
        logger.info("用户验证执行 : " + token.getUsername(), " -> " + manageUser);
        if (manageUser == null) {
            logger.error("用户 { " + token.getUsername() + " } 不存在 ");
            throw new AccountException("账户不存在");
        }
        return new SimpleAuthenticationInfo(manageUser, manageUser.getPassword(), getName());
    }


    @PostConstruct
    public void initCredentialsMatcher() {
        //该句作用是重写shiro的密码验证，让shiro用我自己的验证
        setCredentialsMatcher(new CredentialsMatcher());
    }

}
