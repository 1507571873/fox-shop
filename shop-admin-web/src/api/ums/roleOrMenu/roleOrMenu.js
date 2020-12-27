import request from '../../../utils/request';
//查询
export const queryRoleOrMenuData = Data => {
    return request({
        url: "/fox-shop-admin/ums/roleOrMenu",
        method: 'get',
        params: {
            roleId:Data
        }
    });
};
//保存
export const saveData = Data => {
    return request({
        url: "/fox-shop-admin/ums/roleOrMenu",
        method: 'post',
        data: Data
    });
};

