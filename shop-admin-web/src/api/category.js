import request from '../utils/request';
//分页查询
export const queryProductAttr = Data => {
    return request({
        url: "/fox-shop-admin/pms/attr/category",
        method: 'get',
        params:Data
    });
};
//查询
export const queryCategoryAll =()=> {
    return request({
        url: "/fox-shop-admin/pms/category/All",
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
//新增和修改
export const saveOrUpdateData = (Data) => {
    return request({
        url: "/fox-shop-admin/pms/attr/category",
        method: 'post',
        params:Data
    });
};
//回显
export const queryDataById = (Data) => {
    return request({
        url: "/fox-shop-admin/pms/attr/category/ById",
        method: 'get',
        params: {
            id:Data
        }
    });
};
