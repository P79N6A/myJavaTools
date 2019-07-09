package com.commons.argValid.xss;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Component
@WebFilter(urlPatterns = "/*", filterName = "xssFilter")
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 单独处理callback
        String callback = request.getParameter("callback");
        if (StringUtils.isNotBlank(callback)) {
            if (!Pattern.matches("^[A-Za-z0-9_]+$", callback)) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setStatus(403);
                return;
            }
        }
        filterChain.doFilter(new XssHttpServletRequestWrapper(request), servletResponse);
    }

    @Override
    public void destroy() {
    }
}
