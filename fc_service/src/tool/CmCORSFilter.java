package tool;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName = "cors")
public class CmCORSFilter implements Filter
{

    /**
     * 初始化的方法
     */
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    /**
     * 过滤的方法
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 销毁的方法
     */
    public void destroy()
    {
    }
}
