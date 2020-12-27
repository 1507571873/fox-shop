import request from '../../../utils/request';
//分页查询
export const queryAdminPageData = Data => {
    return request({
        url: "/fox-shop-admin/ums/admin",
        method: 'get',
        params:Data
    });
};
//新增和修改
export const saveOrUpdateData = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/admin",
        method: 'post',
        params:Data
    });
};
//回显
export const queryDataById = (Data) => {
    return request({
        url: "/fox-shop-admin/ums/admin/ById",
        method: 'get',
        params: {
            id:Data
        }
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

