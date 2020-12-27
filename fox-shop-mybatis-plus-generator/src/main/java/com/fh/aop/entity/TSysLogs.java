package com.fh.aop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-09
 */
@Data
@TableName("t_sys_logs")
public class TSysLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 执行时间
     */
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
