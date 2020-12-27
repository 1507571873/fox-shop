import request from '../utils/request';
//查询
export const queryMemberLevelData = Data => {
    return request({
        url: "/fox-shop-admin/pms/member/"+Data,
        method: 'get',
    });
};


