package config;

import javax.servlet.ServletContextEvent;
import java.io.File;

/**
 * 服务器配置文件初始化接口
 *
 * @author feng
 */
public class CmProjectConfig
{

    //
    // 定义项目根目录
    //
    public static String projectPath = null;

    /**
     * 初始化系统配置文件信息
     *
     * @param arg
     */
    public static void initConfig(ServletContextEvent arg)
    {
        try
        {
            //
            // 获取服务器根目录
            //
            projectPath = arg.getServletContext().getRealPath(File.separator) + File.separator;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取配置路径
     *
     * @param path 路径
     * @return 绝对路径
     */
    public static String getWxP12FilePath(String path)
    {
        return projectPath + path;
    }

    /**
     * 私有化构造方法
     */
    private CmProjectConfig()
    {
    }
}
