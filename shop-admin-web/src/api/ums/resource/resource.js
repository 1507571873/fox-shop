import request from '../../../utils/request';
//分页查询
export const queryResourcePageData = Data => {
    return request({
        url: "/fox-shop-admin/ums/resource",
        method: 'get',
        params:Data
    });
};
//新增和修改
export const saveOrUpdateData = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/resource",
        method: 'post',
        params:Data
    });
};
//回显
export const queryDataById = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/resource/ById",
        method: 'get',
        params: {
            id:Data
        }
    });
};
//删除
export const deleteById = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/resource",
        method: 'delete',
        params: {
            id:Data
        }
    });
};
//查询全部
export const queryResourceDataAll = () => {
    return request({
        url: "/fox-shop-admin/ums/resource/All",
        method: 'get',
    });
};
