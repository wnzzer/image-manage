import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import index from "@/views/Index.vue";
import UserCenter from "@/views/UserCenter.vue"
import Home from "@/views/Home.vue"
import Picture from "@/views/Picture.vue"
import Document from "@/views/Document.vue"
import {requireAuth,requireAdminAuth} from './guard/authGuard';
import Setting from "@/views/Setting.vue"
import Users from "@/views/Users.vue"
import Servers from "@/views/Servers.vue"
import User from "@/views/User.vue"

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        redirect: '/login'
    },

    {
        name: '/login',
        path: '/login',
        component: index
    },
    {
        path: '/userCenter',
        redirect: '/userCenter/home',
        component: UserCenter,
        beforeEnter: requireAuth,
        children: [
            {
                path: '/userCenter/home',
                component: Home
            },
            {
                path: '/userCenter/user',
                component: User
            },
            {
                path: '/userCenter/picture',
                component: Picture
            },
            {
                path: '/userCenter/document',
                component: Document
            }
        ]
    },

    {
        path: '/setting',
        component: UserCenter,
        beforeEnter : requireAdminAuth,
        redirect: '/setting/users',
        children: [
            {
                path: '/setting/users',
                component: Users
            },
            {
                path: '/setting/servers',
                component: Servers
            },
            {
                path: '/setting/setting',
                component: Setting
            }
        ]
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;