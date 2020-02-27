package com.code.cache;

/**
 * 玩家初始查询数据，与UserCache类成员一一匹配，自动注入，无需再查
 *
 * @warnning 必须与成员变量一一匹配
 */
public enum UserRedisInitField
{
    userId,//编号
    shell,//贝壳
    pearl,//珍珠
    pearlTotal,//珍珠总值
    shellTotal,//总贝壳
    loginCode//登陆态
}
