package servlet.config;

import com.alibaba.fastjson.JSONObject;
import config.ReadConfig;
import db.CmDbSqlResource;
import db.PeDbWeight;
import servlet.CmServletMain;
import tool.ClassUtils;
import tool.Log4j;

import javax.persistence.Entity;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

<<<<<<< HEAD
/**
 * @author xuwei
 */
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
@WebServlet(urlPatterns = "/flush/logic")
public class FlushServlet extends CmServletMain
{
    private Map<String, Class<?>> classMap;

<<<<<<< HEAD
    @Override
=======
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        String className = content.getString("name");
        JSONObject result = new JSONObject();
<<<<<<< HEAD
        if (classMap == null || className == null)
        {
            result.put("result", "fail");
            result.put("msg", "通知业务逻辑服出现错误，请技术人员进行排查!");
            return result;
        }
        String configStr = "config";
        if (className.equals(configStr))
=======
        if ("config".equals(className))
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        {
            //进行刷新配置文件，并且进行刷新权限文件
            ReadConfig.init();
            PeDbWeight.init();
            result.put("result", "success");
            result.put("msg", JSONObject.toJSONString(PeDbWeight.instance()));
            return result;
        }
<<<<<<< HEAD
=======
        if (classMap == null)
        {
            result.put("result", "fail");
            result.put("msg", "通知业务逻辑服出现错误，请技术人员进行排查!");
            return result;
        }
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
        Class<?> element = classMap.get(className);
        if (element == null)
        {
            result.put("result", "fail");
            result.put("msg", "该业务不支持当前业务刷新，请确定后再处理!");
            return result;
        }
        try
        {
            Method method = element.getDeclaredMethod("init");
            if (method != null)
            {
                method.invoke(null);
            }
        } catch (Exception e)
        {
            result.put("error", Log4j.getExceptionInfo(e));
        }

        result.put("result", "success");
        return result;
    }

    /**
     * 进行加载业务逻辑表
     */
    private void setLogicClass()
    {
        String packageName = "db";
        System.out.println("进行扫描包" + packageName);
        Set<Class<?>> classes = ClassUtils.getClasses(packageName);
        classMap = new ConcurrentHashMap<>();
        classes.forEach(action ->
        {
            if (action.isAnnotationPresent(Entity.class))
            {
                Entity entity = action.getAnnotation(Entity.class);
                String name = entity.name();
                classMap.put(name, action);
            }
        });
        System.out.println("扫描共有" + classMap.size() + "个实体类~");
    }

    @Override
    public void init() throws ServletException
    {
        super.init();
        setLogicClass();
    }
}
