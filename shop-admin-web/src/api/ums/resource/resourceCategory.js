import request from '../../../utils/request';
//分页查询
export const queryPageData = Data => {
    return request({
        url: "/fox-shop-admin/ums/resourceCategory",
        method: 'get',
        params:Data
    });
};
//新增和修改
export const saveOrUpdateData = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/resourceCategory",
        method: 'post',
        params:Data
    });
};
//回显
export const queryDataById = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/resourceCategory/ById",
        method: 'get',
        params: {
            id:Data
        }
    });
};

//回显
export const deleteById = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/resourceCategory",
        method: 'delete',
        params: {
            id:Data
        }
    });
};



//用户页面附角色 查询所有
export const queryResourceCategoryAll = () => {
    return request({
        url: "/fox-shop-admin/ums/resourceCategory/All",
        method: 'get',
    });
};




