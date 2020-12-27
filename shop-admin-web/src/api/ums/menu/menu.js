import request from '../../../utils/request';
//分页查询
export const queryMenuPageData = Data => {
    return request({
        url: "/fox-shop-admin/ums/menu",
        method: 'get',
        params:Data
    });
};
//分页查询
export const queryMenuLevelData = Data => {
    return request({
        url: "/fox-shop-admin/ums/menu/level",
        method: 'get',
        params:{level:Data}
    });
};
//新增和修改
export const saveOrUpdateData = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/menu",
        method: 'post',
        params:Data
    });
};
//回显
export const queryDataById = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/menu/ById",
        method: 'get',
        params: {
            id:Data
        }
    });
};

//用户页面附角色 查询所有
export const queryMenuTree = () => {
    return request({
        url: "/fox-shop-admin/ums/menu/tree",
        method: 'get',
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

//用户页面附角色 保存
export const queryMenuByName = () => {
    return request({
        url: "/fox-shop-admin/ums/menu/TreeByName",
        method: 'get',
    });
};
