package com.fh.aop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fh.aop.entity.TSysLogs;
import com.fh.aop.mapper.TSysLogsMapper;
import com.fh.aop.service.ITSysLogsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 唐嘉萌
 * @since 2020-12-09
 */
@Service
public class TSysLogsServiceImpl extends ServiceImpl<TSysLogsMapper, TSysLogs> implements ITSysLogsService {

}
