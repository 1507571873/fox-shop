package com.fh.security.filter;

import cn.hutool.json.JSONUtil;
import com.fh.utils.CommonsReturn;
import com.fh.utils.ReturnCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆没有通过的返回结果
 * @ClassName RestfulAccessDeniedHandler
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/20 22:27
 * @Version 1.0
 **/
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(CommonsReturn.error(ReturnCode.NO_PERMISSION)));
        response.getWriter().flush();
    }
}
