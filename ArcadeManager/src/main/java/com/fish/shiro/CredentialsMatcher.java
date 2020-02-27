package com.fish.shiro;

import com.fish.utils.XwhTool;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CredentialsMatcher extends SimpleCredentialsMatcher
{
    private static Logger logger = LoggerFactory.getLogger(CredentialsMatcher.class);

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
    {
        UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
        Object tokenCredentials = XwhTool.getMD5Encode(authcToken.getUsername() + String.valueOf(authcToken.getPassword()));
        Object accountCredentials = getCredentials(info);
        logger.info("密码进行校验:" + tokenCredentials + ",account=" + accountCredentials);
        return accountCredentials.equals(tokenCredentials);
    }
}
