package com.fh.logs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName Logs
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/29 14:57
 * @Version 1.0
 **/
@Data
@Document("fox_logs")
public class Logs {
    /**
     * 主键id
     */
    private String id;

    /**
     * 执行时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("createTime")
    private Date createtime;

    /**
     * 类型 1，正常  2，错误
     */
    private Integer type;

    /**
     * 用户ip地址
     */
    @TableField("userIp")
    private String userip;

    /**
     * 记录的方法名
     */
    @TableField("methodName")
    private String methodname;

    /**
     * 记录的类名
     */
    @TableField("className")
    private String classname;

    /**
     * 记录的参数名
     */
    private String args;

    /**
     * 性能统计
     */
    @TableField("runTime")
    private Long runtime;

    /**
     * 描述
     */
    @TableField("describeInfo")
    private String describeinfo;

    /**
     * 错误描述
     */
    @TableField("errorInfo")
    private String errorinfo;
}
