package com.fh.userOrRole.entity;

import lombok.Data;

import java.util.List;

/**
 * @ClassName UmsAdminRoleBo
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/16 21:34
 * @Version 1.0
 **/
@Data
public class UmsAdminRoleBo extends UmsAdminRoleRelation{

    private Long adminId;

    private List<UmsAdminRoleRelation> umsAdminRoleList;
}
