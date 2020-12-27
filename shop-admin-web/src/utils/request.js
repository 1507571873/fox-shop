import axios from 'axios';
import router from "@/router";


const service = axios.create({
    // process.env.NODE_ENV === 'development' 来判断是否开发环境
    // easy-mock服务挂了，暂时不使用了
    // baseURL: 'https://www.easy-mock.com/mock/592501a391470c0ac1fab128',
    timeout: 5000
});

service.interceptors.request.use(
    config => {
        config.headers.common['Authorization-token']=localStorage.getItem("token");
        config.headers.common['Authorization-ip']=localStorage.getItem("ip");
        return config;
    },
    error => {
        console.log(error);
        return Promise.reject(error);
    }
);

service.interceptors.response.use(
    response => {
        let code = response.data.code;
        if (code === 6666){
            alert(response.data.msg);
            router.replace("Login");
        }
        return response.data;
    },
    error => {
        console.log(error);
        return Promise.reject(error);
    }
);

export default service;
