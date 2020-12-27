import request from '../utils/request';
//查询
export const queryProductData = Data => {
    return request({
        url: "/fox-shop-admin/pms/product",
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
//回显
export const getProductIdById = (Data) => {
    return request({
        url: "/fox-shop-admin/pms/product/ById",
        method: 'get',
        params: {
            id:Data
        }
    });
};
//新增
export const saveOrUpdateProductData = Data => {
    return request({
        url: "/fox-shop-admin/pms/product",
        method: 'post',
        data:Data
    });
};
