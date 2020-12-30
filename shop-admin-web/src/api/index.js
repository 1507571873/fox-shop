import request from '../utils/request';
//分页查询
export const queryData = (Data,Path) => {
    return request({
        url: Path,
        method: 'get',
        params:Data
    });
};
//查询
export const queryDataAll = (Data) => {
    return request({
        url: "/fox-shop-admin/pms/brand/All",
        method: 'get',
    });
};
//删除
export const deleteData = (Data,Path) => {
    return request({
        url: Path,
        method: 'delete',
        params: {
            id:Data
        }
    });
};
//新增
export const saveOrUpdateData = (Data,Path) => {
    return request({
        url: Path,
        method: 'post',
        params:Data
    });
};
//回显
export const queryDataById = (Data,Path) => {
    return request({
        url: Path,
        method: 'get',
        params: {
            id:Data
        }
    });
};
//只查询品牌名称
export const queryBrandName = () => {
    return request({
        url: "/fox-shop-admin/pms/brand/BrandName",
        method: 'get',
    });
};
//退出登录
export const loginOut = () => {
    return request({
        url: "/fox-shop-admin/LoginController/LoginOut",
        method: 'get',
    });
};