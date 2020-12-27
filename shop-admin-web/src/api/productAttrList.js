import request from '../utils/request';
//查询
export const queryProductAttr = Data => {
    return request({
        url: "/fox-shop-admin/pms/attr/attribute",
        method: 'get',
        params:Data
    });
};
//查询
export const queryProductAttrAll = (Data) => {
    return request({
        url: "/fox-shop-admin/pms/attr/attribute/All",
        method: 'get',
        params:Data
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
//新增和修改
export const saveOrUpdateData = (Data) => {
    return request({
        url: "/fox-shop-admin/pms/attr/attribute",
        method: 'post',
        params:Data
    });
};
//回显
export const queryDataById = (Data) => {
    return request({
        url: "/fox-shop-admin/pms/attr/attribute/ById",
        method: 'get',
        params: {
            id:Data
        }
    });
};
