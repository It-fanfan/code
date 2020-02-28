package db;

import tool.Log4j;

import javax.persistence.Entity;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xuwei
 */
@Entity(name = "config_confirm")
public class PeDbConfigConfirm extends PeDbObject {
    /**
     * 主键
     */
    public String ddId;
    /**
     * 状态
     */
    public boolean ddStatus;
    /**
     * 确认值
     */
    public String ddYes;
    /**
     * 否认值
     */
    public String ddNo;

    private static Map<String, PeDbConfigConfirm> cacheMap = new ConcurrentHashMap<>();

    /**
     * 获取缓存数据
     *
     * @param key 主键信息
     * @return 配置参数
     */
    public static PeDbConfigConfirm getConfirm(String key) {
        return cacheMap.get(key);
    }

    private static void sync() {
        cacheMap.clear();
        Vector<PeDbObject> data = getData();
        data.forEach(peDbObject -> {
            PeDbConfigConfirm confirm = (PeDbConfigConfirm) peDbObject;
            cacheMap.put(confirm.ddId, confirm);
        });
    }


    /**
     * 获取游戏列表
     */
    public static Vector<PeDbObject> getData() {
        CmDbSqlResource sqlResource = CmDbSqlResource.instance();
        Vector<PeDbObject> objects = new Vector<>();

        try {
            objects = PeDbConfigConfirm.queryObject(sqlResource, PeDbConfigConfirm.class, "");
        } catch (Exception e) {
            LOG.error(Log4j.getExceptionInfo(e));
        }
        return objects;
    }

    /**
     * 初始化数据对象
     */
    public static void init() {
        sync();
    }

    /**
     * 构造一个 PeDbUser
     */
    public PeDbConfigConfirm() {
        super();
    }
}
