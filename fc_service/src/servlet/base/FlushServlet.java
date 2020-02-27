package servlet.base;

import com.alibaba.fastjson.JSONObject;
import db.CmDbSqlResource;
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

@WebServlet(urlPatterns = "/flush/logic")
public class FlushServlet extends CmServletMain
{
    private Map<String, Class<?>> classMap;


    protected JSONObject handle(CmDbSqlResource sqlResource, HttpServletRequest requestObject, JSONObject content)
    {
        String className = content.getString("name");
        JSONObject result = new JSONObject();
        if (classMap == null)
        {
            result.put("result", "fail");
            result.put("msg", "通知业务逻辑服出现错误，请技术人员进行排查!");
            return result;
        }
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
