package com.fish.service;

import com.fish.dao.second.mapper.UserAllInfoMapper;
import com.fish.dao.second.mapper.UserValueMapper;
import com.fish.dao.second.model.UserAllInfo;
import com.fish.dao.second.model.UserValue;
import com.fish.protocols.GetParameter;
import com.fish.utils.HexToStringUtil;
import com.fish.utils.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserValueService implements BaseService<UserAllInfo> {

    @Autowired
    UserValueMapper uerValueMapper;

    @Autowired
    UserAllInfoMapper userAllInfoMapper;

    @Override
    public List<UserAllInfo> selectAll(GetParameter parameter) {
        List<UserValue> userValues = uerValueMapper.selectAll();
        List<UserAllInfo> userAllInfos = new ArrayList();
        UserAllInfo userAllInfo =new UserAllInfo();

        for (UserValue userValue : userValues) {
            String dduid = userValue.getDduid();
            String tableName = "user" + generateTableCodeByKey(dduid);
//            System.out.println("tableName : " + tableName);
//            System.out.println("dduid : " + dduid);
            try {
                 userAllInfo = userAllInfoMapper.selectByUid(dduid, tableName);
                userAllInfo.setDdAwardMoney(userValue.getDdawardmoney());
                userAllInfo.setDdAwardCoin(userValue.getDdawardcoin());
                userAllInfo.setDdCoinCount(userValue.getDdcoincount());
                userAllInfo.setDdMoney(userValue.getDdmoney());
                userAllInfo.setDdTotalPayMoney(userValue.getDdtotalpaymoney());
                userAllInfo.setDdname128u(HexToStringUtil.getStringFromHex(userAllInfo.getDdname128u()));
                userAllInfos.add(userAllInfo);
            } catch (Exception e)
            {
                LOGGER.error(Log4j.getExceptionInfo(e));
            }
        }
        return userAllInfos;
    }

    //新增展示所有产品信息
    public int insert(UserAllInfo record) {
        record.setDdname128u(HexToStringUtil.getStringToHex(record.getDdname128u()));
        return userAllInfoMapper.insert(record);
    }

    //更新产品信息
    public int updateByPrimaryKeySelective(UserAllInfo record) {
        record.setDdname128u(HexToStringUtil.getStringToHex(record.getDdname128u()));
        return userAllInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void setDefaultSort(GetParameter parameter) {

    }

    @Override
    public Class<UserAllInfo> getClassInfo() {
        return null;
    }

    @Override
    public boolean removeIf(UserAllInfo userAllInfo, Map<String, String> searchData) {
        return false;
    }

    /**
     * 根据 id 生成分表编号的方法
     */
    public static String generateTableCodeByKey(String key) {
        if (key.length() < 16) {
            return "0";
        }

        int index = key.length() - 4;
        int c0 = (int) key.charAt(index);
        int c1 = (int) key.charAt(index + 1);
        int c2 = (int) key.charAt(index + 2);
        int c3 = (int) key.charAt(index + 3);
        int value = (c0 % 10) * 1000 + (c1 % 10) * 100 + (c2 % 10) * 10 + (c3 % 10);
        if (value < 0) {
            value = -value;
        }

        return String.valueOf(value);
    }

}
