package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.ManageRole;
import com.fish.dao.primary.model.ManageUser;

import java.util.List;

/**
 * 系统管理人员-角色MyBatis接口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-17 21:57
 */
public interface ManageRoleMapper {

    /**
     * 通过ID查询角色信息
     *
     * @param id
     * @return
     */
    ManageRole select(int id);

    /**
     * 根据多个ID查询角色列表
     *
     * @param roleIds 用逗号间隔的角色ID字符串
     * @return 角色列表
     */
    List<ManageRole> selectRolesByIds(String roleIds);

    /**
     * 查询全部角色
     *
     * @return
     */
    List<ManageRole> selectAll();

    /**
     * 新增角色信息
     *
     * @param manageRole
     * @return
     */
    int insert(ManageRole manageRole);

    /**
     * 修改角色信息
     *
     * @param manageRole
     * @return
     */
    int update(ManageRole manageRole);

    /**
     * 删除角色
     *
     * @param deleteIds
     * @return
     */
    int delete(String deleteIds);

}
