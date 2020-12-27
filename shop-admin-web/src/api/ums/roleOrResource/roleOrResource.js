import request from '../../../utils/request';
//查询
export const queryRoleOrResourceData = Data => {
    return request({
        url: "/fox-shop-admin/ums/roleOrResource/ById",
        method: 'get',
        params: {
            roleId:Data
        }
    });
};
//保存
export const saveData = Data => {
    return request({
        url: "/fox-shop-admin/ums/roleOrResource",
        method: 'post',
        data: Data
    });
};

