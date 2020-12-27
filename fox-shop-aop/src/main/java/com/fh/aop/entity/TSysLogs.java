package com.fh.aop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 日志记录
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
