package com.fish.shiro;

import com.fish.utils.XwhTool;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 密码校验
 *
 * @author CC
 * @date 2020-02-17 21:13
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    private static Logger logger = LoggerFactory.getLogger(CredentialsMatcher.class);

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken authoToken = (UsernamePasswordToken) token;
        Object tokenCredentials = XwhTool.getMD5Encode(
                authoToken.getUsername() + String.valueOf(authoToken.getPassword()));
        Object accountCredentials = getCredentials(info);
        logger.info("密码进行校验:" + tokenCredentials + ",account=" + accountCredentials);
        return accountCredentials.equals(tokenCredentials);
    }

}
