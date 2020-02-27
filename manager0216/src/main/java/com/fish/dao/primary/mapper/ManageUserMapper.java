package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ManageUser;

import java.util.List;

/**
 * 系统管理人员-账号MyBatis接口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-17 21:29
 */
public interface ManageUserMapper {

    /**
     * 通过用户名和密码登录之后查询到的用户信息
     *
     * @param userName
     * @return
     */
    ManageUser login(String userName);

    /**
     * 根据ID查询系统管理人员
     *
     * @param id
     * @return
     */
    ManageUser select(Integer id);

    /**
     * 查询用户列表
     *
     * @return
     */
    List<ManageUser> selectAll();

    /**
     * 新增用户
     *
     * @param user 用户
     * @return
     */
    int insert(ManageUser user);

    /**
     * 管理员修改用户信息：账号、密码、昵称、权限信息、允许登录状态
     *
     * @param user
     * @return
     */
    int update(ManageUser user);

    /**
     * 用户自行修改信息：账号、密码、昵称
     *
     * @param user
     * @return
     */
    int updateByUserSelf(ManageUser user);

    /**
     * 系统管理员删除用户
     *
     * @param deleteIds
     * @return
     */
    int delete(String deleteIds);

    /**
     * 记录最后登录时间
     *
     * @param userName
     * @return
     */
    int recodeLastLoginTime(String userName);

}
