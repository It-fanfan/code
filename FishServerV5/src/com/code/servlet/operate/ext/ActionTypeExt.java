package com.code.servlet.operate.ext;

import com.code.protocols.AbstractResponse;

import java.util.Vector;

public class ActionTypeExt
{
    public enum ActionType
    {
        shell,//贝壳
        hook, //鱼钩
        book,//图鉴
        fish,//鱼
        basin,//水域
    }

    public static class RequestImpl
    {
        public long userid;
        //更新类型
        public Vector<OperateEntity> entities;
    }

    public static class ResponseImpl extends AbstractResponse
    {

    }

    public static class OperateEntity
    {
        //更新类型
        public ActionType type;
        //对象数据
        public Object value;

        public OperateEntity()
        {

        }

        public OperateEntity(ActionType type, Object value)
        {
            this.type = type;
            this.value = value;
        }
    }

    public static class BookEntity
    {
        public Vector<Integer> books;
    }

    public static class FishEntity
    {
        public Vector<AddFish> fishes;
    }

    public static class AddFish
    {
        public int num;
        public int fitd;
        public int level;

        @Override
        public String toString()
        {
            return "AddFish{" + "num=" + num + ", fitd=" + fitd + ", level=" + level + '}';
        }
    }

}
