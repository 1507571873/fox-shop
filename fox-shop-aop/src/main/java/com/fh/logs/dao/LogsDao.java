package com.fh.logs.dao;

import com.fh.logs.model.Logs;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @InterfaceName LogsDao
 * @Author 唐嘉萌
 * @Date 2020/12/29 14:56
 * @Version 1.0
 **/
public interface LogsDao extends MongoRepository<Logs,String> {
}
