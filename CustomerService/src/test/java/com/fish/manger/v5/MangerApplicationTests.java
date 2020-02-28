package com.fish.manger.v5;

import com.fish.dao.primary.mapper.*;
import com.fish.dao.primary.model.ManageAccount;
import com.fish.manger.v5.generator.LayuiHtml;
import com.fish.utils.XwhTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan({"com.fish.manger.v5.mapper", "com.fish.dao.primary.mapper"})
public class MangerApplicationTests
{
    @Autowired
    LayuiHtml layUi;
    @Autowired
    ManageAccountMapper userMapper;

    static Logger LOGGER = LoggerFactory.getLogger(MangerApplicationTests.class);


    private void createLayUi()
    {
        layUi.generator();
    }

    /**
     * 设置随机密码
     *
     * @return 密码
     */
    private String setRandomPassword()
    {
        int len = ThreadLocalRandom.current().nextInt(6, 9);
        StringBuilder builder = new StringBuilder();
        char a = '0', b = '9', c = 'a', d = 'z', e = 'A', f = 'Z';
        char[] s = new char[]{'@', '&', '-', '.'};
        int size = 4;
        int hit = ThreadLocalRandom.current().nextInt(3);
        for (int i = 0; i < len; i++)
        {
            switch (hit)
            {
                case 0:
                {
                    int val = b - a;
                    int random = ThreadLocalRandom.current().nextInt(val);
                    builder.append((char) (random + a));
                }
                break;
                case 1:
                {
                    int val = d - c;
                    int random = ThreadLocalRandom.current().nextInt(val);
                    builder.append((char) (random + c));
                }
                break;
                case 2:
                {
                    int val = f - e;
                    int random = ThreadLocalRandom.current().nextInt(val);
                    builder.append((char) (random + e));
                }
                break;
                case 3:
                {
                    size = 3;
                    builder.append(s[ThreadLocalRandom.current().nextInt(s.length)]);
                }
            }
            hit = ThreadLocalRandom.current().nextInt(size);
        }
        return builder.toString();
    }

    private void createAccount()
    {
        ManageAccount account = new ManageAccount();
        account.setUsername("chufan@password.com");
        String password = setRandomPassword();
        String value = XwhTool.getMD5Encode(account.getUsername() + password);
        account.setPassword(value);
        account.setNickname("褚帆");
        account.setUpdatetime(new Date());
        account.setRole("user");
        ManageAccount temp = userMapper.selectByPrimaryKey(account.getUsername());
        if (temp == null)
        {
            userMapper.insert(account);
            LOGGER.info("新建账号:" + account.getUsername() + ",密码:" + password);
        }
        //        user.setPassword("");
        //        Object tokenCredentials = XwhTool.getMD5Encode(authcToken.getUsername() + String.valueOf(authcToken.getPassword()));
    }


    @Test
    public void contextLoads() throws IOException {

        createAccount();
        //createLayUi();
    }

}
