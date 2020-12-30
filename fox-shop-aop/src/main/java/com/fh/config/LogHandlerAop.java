package com.fh.config;

import com.alibaba.fastjson.JSON;

import com.fh.aop.service.ITSysLogsService;
import com.fh.logs.dao.LogsDao;
import com.fh.logs.model.Logs;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

/**
 * @author 唐吉诃德
 */
@Aspect
@Component
@Order(3)
public class LogHandlerAop {

    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private ITSysLogsService logService;

    @Resource
    private LogsDao logsDao;

    /**
     * 环绕通知
     */
    @Around("execution(* com.fh.*.controller.*.*(..)) && @annotation(logsAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, com.fh.config.LogsAnnotation logsAnnotation) throws Throwable {
        Object obj=null;
        String ip=httpServletRequest.getHeader("Authorization-ip");
        //存入mysql
        //TSysLogs logs = new TSysLogs();
        //存入mongoDB
        Logs logs = new Logs();
        //获取当前访问的用户信息
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String args = JSON.toJSONString(joinPoint.getArgs());
        logs.setUserip(ip);
        System.out.println(ip);
        logs.setClassname(className);
        logs.setMethodname(methodName);
        logs.setArgs(args);
        Date date = new Date();

        logs.setCreatetime(date);
        logs.setDescribeinfo(logsAnnotation.value());
        long startTime = System.currentTimeMillis();
        try {
            logs.setType(1);
            obj=joinPoint.proceed();
        } catch (Throwable throwable) {
            logs.setType(2);
            throwable.printStackTrace();
            logs.setErrorinfo(exception(throwable));
            throw throwable;
        }finally {
            logs.setRuntime(System.currentTimeMillis()-startTime);
            //logService.save(logs);
            logsDao.insert(logs);
        }
        return obj;
    }
    /**
     * 将异常信息转化成字符串
     *
     * @param t-
     * @return
     * @throws IOException
     */
    private static String exception(Throwable t) throws IOException {
        if (t == null){
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            t.printStackTrace(new PrintStream(baos));
        } finally {
            baos.close();
        }
        return baos.toString();
    }
}