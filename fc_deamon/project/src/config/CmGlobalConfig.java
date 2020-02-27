package config;

import tool.CmTool;

import javax.servlet.ServletContextEvent;
import java.io.File;
import java.util.HashMap;

/**
 * 工程版本数据信息
 *
 * @author Vinnes
 */
public class CmGlobalConfig
{

    //
    // 定义工程配置文件名称
    //
    public static final String CONFIG_COMMON = "common.cfg";

    //
    // 定义管理员账户
    //
    public static String admin = "";

    //
    // 定义管理员密码
    //
    public static String password = "";

    /**
     * 初始化系统配置文件信息
     */
    public static void initConfig(ServletContextEvent arg)
    {
        String commonPath = arg.getServletContext().getRealPath(File.separator)
                + File.separator + "WEB-INF" + File.separator + CONFIG_COMMON;

        String content = CmTool.readFileString(commonPath);
        HashMap<String, String> configMap = CmTool.parseConfigString(content);

        admin = configMap.get("admin");
        password = configMap.get("password");
        System.out.println("管理员:" + admin + ",password:" + password);
    }

}
