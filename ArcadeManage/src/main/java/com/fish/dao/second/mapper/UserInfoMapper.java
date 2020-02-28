package com.fish.dao.second.mapper;

import com.fish.dao.second.model.UserAllInfo;
import com.fish.dao.second.model.UserInfo;
import com.fish.dao.second.model.UserValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper
{
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    UserInfo selectByDdUid(String uid);

    List<UserInfo> selectUserInfo(String uids);

    List<UserInfo> selectByDdName(String ddName);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    List<UserAllInfo> selectAll();

    List<UserAllInfo> selectByRegister(@Param("start") String start, @Param("end") String end);

    List<UserAllInfo> selectByLogin(@Param("start") String start, @Param("end") String end);

    List<UserAllInfo> selectByTime(@Param("registerstart") String registerstart, @Param("registerend") String registerend,@Param("loginstart") String loginstart, @Param("loginend") String loginend);
}