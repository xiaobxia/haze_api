package com.vxianjin.gringotts.web.filter;


import com.vxianjin.gringotts.util.TimeKey;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Author: chenkai
 * @Date: 2018/8/8 17:22
 * @Description: logfilter
 */
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        TimeKey.clear();
        TimeKey.start();
        filterChain.doFilter(servletRequest,servletResponse);
        TimeKey.clear();
    }

    @Override
    public void destroy() {

    }
}

