import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/',
            component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
            meta: { title: '自述文件' },
            children: [
                {
                    path: '/dashboard',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
                    meta: { title: '系统首页' }
                },
                {
                    path: '/brand',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/pms/brand/BrandTable.vue'),
                    meta: { title: '品牌管理' }
                },
                {
                    path: '/brandEcharts',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/pms/brand/echarts/BrandEcharts.vue'),
                    meta: { title: '品牌管理' }
                },
                {
                    path: '/productCate',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/pms/category/Category.vue'),
                    meta: { title: '商品分类' }
                },
                {
                    path: '/productAttr',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/pms/productAttr/ProductAttr.vue'),
                    meta: { title: '商品类型' }
                },
                {
                    path: '/productAttrList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/pms/productAttr/productAttrList.vue'),
                    meta: { title: '商品属性列表' }
                },
                {
                    path: '/productAttrList2',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/pms/productAttr/productAttrList2.vue'),
                    meta: { title: '商品参数列表' }
                },
                {
                    path: '/product',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/pms/product/index.vue'),
                    meta: { title: '商品列表' }
                },
                {
                    path: '/addProduct',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/pms/product/ProductAdd.vue'),
                    meta: { title: '商品新增' }
                },
                {
                    path: '/productUpdate',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/pms/product/ProductUpdate.vue'),
                    meta: { title: '商品修改' }
                },
                {
                    path: '/admin',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/ums/admin/index.vue'),
                    meta: { title: '用户管理' }
                },
                {
                    path: '/role',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/ums/role/index.vue'),
                    meta: { title: '角色列表' }
                },
                {
                    path: '/menu',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/ums/menu/index.vue'),
                    meta: { title: '菜单列表' }
                },
                {
                    path: '/resource',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/ums/resource/index.vue'),
                    meta: { title: '资源列表' }
                },
                {
                    path: '/categoryList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/ums/resource/categoryList.vue'),
                    meta: { title: '资源分类' }
                },
                {
                    path: '/allocMenu',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/ums/role/allocMenu.vue'),
                    meta: { title: '分配菜单' }
                },
                {
                    path: '/allocResource',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../views/ums/role/allocResource.vue'),
                    meta: { title: '分配资源' }
                },
                {
                    // 权限页面
                    path: '/permission',
                    component: () => import(/* webpackChunkName: "permission" */ '../components/page/Permission.vue'),
                    meta: { title: '权限测试', permission: true }
                },
                {
                    path: '/404',
                    component: () => import(/* webpackChunkName: "404" */ '../components/page/404.vue'),
                    meta: { title: '404' }
                },
                {
                    path: '/403',
                    component: () => import(/* webpackChunkName: "403" */ '../components/page/403.vue'),
                    meta: { title: '403' }
                },
            ]
        },
        {
            path: '/login',
            component: () => import(/* webpackChunkName: "login" */ '../components/page/Login.vue'),
            meta: { title: '登录' }
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
});
