package com.code.service.angling;

import com.code.dao.entity.fish.config.Systemstatusinfo;
import com.utils.XwhTool;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

class FishNickNameService
{
    /**
     * 昵称命名空间
     */
    private static final NickNameSpace NAME_SPACE = getNicknameSpace();

    /**
     * 昵称命名
     *
     * @return 命名空間
     */
    private static NickNameSpace getNicknameSpace()
    {
        Systemstatusinfo info = Systemstatusinfo.getSystemstatusinfo("name_space");
        try
        {
            if (info != null)
            {
                String json = info.getSystem_value();
                return XwhTool.parseConfigString(json, NickNameSpace.class);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new NickNameSpace();
    }

    /**
     * 進行通過性別設置名稱
     *
     * @param sex 性别
     * @return 返回值
     */
    static String createNickName(int sex)
    {
        Vector<String> names = null;
        switch (sex)
        {
            case 0:// 女性
            {
                names = NAME_SPACE.female;
            }
            break;
            case 1:// 男性
            {
                names = NAME_SPACE.male;
            }
            break;
            default:
                break;
        }
        if (names != null)
        {
            String a = "";
            // 獲取形容詞
            Vector<String> a_s = NAME_SPACE.a_;
            if (a_s != null && !a_s.isEmpty())
            {
                int size = a_s.size();
                if (1 == size)
                {
                    a = a_s.firstElement();
                } else
                {
                    int index = ThreadLocalRandom.current().nextInt(size);
                    a = a_s.elementAt(index);
                }
            }
            StringBuilder builder = new StringBuilder();
            int size = names.size();
            int index = ThreadLocalRandom.current().nextInt(size);
            builder.append(names.elementAt(index));
            if (builder.length() == 1)
            {
                builder.insert(0, a);
            } else if (builder.length() == 2)
            {
                if (ThreadLocalRandom.current().nextBoolean())
                {
                    builder.insert(0, a);
                }
            }
            return builder.toString();
        }
        return "--";
    }

    /**
     * 昵称空间命名
     *
     * @author Sky
     */
    public static class NickNameSpace
    {
        // 形容词
        public Vector<String> a_;
        // 雄性
        public Vector<String> male;
        // 雌性
        public Vector<String> female;
    }
}
