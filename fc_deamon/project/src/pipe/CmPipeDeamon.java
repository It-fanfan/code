<<<<<<< HEAD
=======
/**
 *
 */
>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed
package pipe;

import config.ReadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.RoundSafeguard;
import tool.ClassUtils;
import tool.Log4j;

import javax.persistence.Entity;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author feng
 */
public class CmPipeDeamon extends Thread
{
    private static final Logger LOG = LoggerFactory.getLogger(CmPipeDeamon.class);

<<<<<<< HEAD
    /**
     * 定义 CmPipeDeamon 对象
     */
    private static CmPipeDeamon pipeDeamon = null;
=======
    //
    // 定义 CmPipeDeamon 对象
    //
    public static CmPipeDeamon pipeDeamon = null;

    //
    // 定义是否正在运行
    //
    public boolean isRunning = true;

>>>>>>> 8e4fccbbfce1955a84f9ef20f6bf84773b680aed

    /**
     * 初始化的方法
     *
     * @param scheduler 线程池
     */
    public static void init(ScheduledExecutorService scheduler)
    {
        try
        {
            initDatabase();
            int port = Integer.valueOf(ReadConfig.get("service-port"));
            scheduler.execute(new CmPipeServiceDemon(port));
            scheduler.scheduleWithFixedDelay(new RoundSafeguard(scheduler), 0, 5, TimeUnit.SECONDS);
        } catch (Exception e)
        {
            LOG.error(Log4j.getExceptionInfo(e));
        }

        pipeDeamon = new CmPipeDeamon();
        pipeDeamon.start();
    }

    /**
     * 进行加载业务逻辑表
     */
    private static void initDatabase()
    {
        String packageName = "db";
        System.out.println("进行扫描包" + packageName);
        Set<Class<?>> classes = ClassUtils.getClasses(packageName);
        classes.forEach(action ->
        {
            if (action.isAnnotationPresent(Entity.class))
            {
                try
                {
                    Method method = action.getDeclaredMethod("init");
                    if (method != null)
                    {
                        method.invoke(null);
                    }
                } catch (Exception e)
                {
                    LOG.error(Log4j.getExceptionInfo(e));
                }
            }
        });
        System.out.println("進行初始实体类~");
    }
}
