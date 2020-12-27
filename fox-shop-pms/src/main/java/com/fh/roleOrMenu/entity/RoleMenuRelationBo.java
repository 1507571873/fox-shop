package com.fh.roleOrMenu.entity;

import lombok.Data;

import java.util.List;

/**
 * @ClassName RoleMenuRelationBo
 * @OperatorName 唐嘉萌
 * @Deacription TODO
 * @Author 唐吉诃德
 * @Date 2020/12/17 17:22
 * @Version 1.0
 **/
@Data
public class RoleMenuRelationBo {
    private Integer roleId;
    private List<UmsRoleMenuRelation> roleOrMenuList;
}
