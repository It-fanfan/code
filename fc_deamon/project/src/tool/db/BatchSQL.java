package tool.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 批量更新
 *
 * @author Sky
 */
public interface BatchSQL
{
    /**
     * 批量更新數據
     *
     * @return SQL语句设置
     */
    String getSQL();

    /**
     * 更新條數
     *
     * @return 数据长度
     */
    int getLength();

    /**
     * 添加一條批量數據
     *
     * @param prest 数据对象
     * @param index 索引
     */
    void addBatch(PreparedStatement prest, int index) throws SQLException;

}
