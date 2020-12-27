import request from '../../../utils/request';
//分页查询
export const queryRolePageData = Data => {
    return request({
        url: "/fox-shop-admin/ums/role",
        method: 'get',
        params:Data
    });
};
//__________________________________________________________________________________________________
//给用户赋予角色保存
export const saveOrUpdateData = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/userOrRole",
        method: 'post',
        data:Data
    });
};
//给用户赋予角色回显
export const queryUserOrRoleById = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/userOrRole/ById",
        method: 'get',
        params: {
            adminId:Data
        }
    });
};
//__________________________________________________________________________________________________
//回显
export const queryDataById = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/role/ById",
        method: 'get',
        params: {
            id:Data
        }
    });
};

//用户页面附角色 查询所有
export const queryRolePageAll = () => {
    return request({
        url: "/fox-shop-admin/ums/role/All",
        method: 'get',
    });
};

//用户页面附角色 查询所有
export const queryRoleDelete = (id) => {
    return request({
        url: "/fox-shop-admin/ums/role",
        method: 'delete',
        params: {
            id:id
        }
    });
};

//用户页面附角色 保存
export const saveUserOrRole = (data) => {
    return request({
        url: "/fox-shop-admin/ums/userOrRole",
        method: 'post',
        data:data
    });
};

