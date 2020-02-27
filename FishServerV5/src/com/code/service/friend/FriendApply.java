package com.code.service.friend;

import java.util.Vector;

/**
 * 好友申请数据
 */
public class FriendApply
{
    //申请记录
    public Vector<String> record = new Vector<>();
    //被动申请数据
    public Vector<String> passive = new Vector<>();

    /**
     * 添加好友申请记录
     *
     * @param friendId 好友编号
     * @return 是否重复添加
     */
    public boolean addRecord(String friendId)
    {
        if (record.contains(friendId))
        {
            return false;
        }
        record.add(friendId);
        return true;
    }

    /**
     * 添加被动好友记录
     *
     * @param friendId 好友编号
     * @return 是否添加申请记录
     */
    public boolean addPassive(String friendId)
    {
        if (passive.contains(friendId))
        {
            return false;
        }
        passive.add(friendId);
        return true;
    }

    /**
     * 判断是否可成为好友
     */
    public boolean existCanBeFriend(String friendId)
    {
        return passive.contains(friendId) && record.contains(friendId);
    }

    /**
     * 移除记录信息
     *
     * @param friendId 好友信息
     */
    public void removeRecord(String friendId)
    {
        passive.removeElement(friendId);
        record.removeElement(friendId);
    }
}
