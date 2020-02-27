package com.code.protocols.operator.utils;

import java.util.HashMap;
import java.util.Map;

public class ReceiveReward
{
    //日期标志
    public int dayFlag;
    //领取标签
    public Map<UtilsType, Integer> utils = new HashMap<>();

    /**
     * 获取其他参数值
     *
     * @param utilsType 附加类型
     * @return 附加记录值
     */
    public int getUtilsValue(UtilsType utilsType)
    {
        if (utils == null || !utils.containsKey(utilsType))
            return 0;
        return utils.get(utilsType);
    }
}
