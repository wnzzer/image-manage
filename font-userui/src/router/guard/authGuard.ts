import useUserStore from '@/store/module/user';
import { NavigationGuardNext, RouteLocationNormalized } from 'vue-router';

// 检查用户是否有有效的 token，如果没有则重定向到登录页面
export const requireAuth = (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
  const token = useUserStore().user.token;
  if (token.length < 5) {
    console.log('拦截')
    // 如果没有 token，则重定向到登录页面
    next('/login');
  } else {
    console.log('不拦截')
    // 如果有 token，允许继续导航
    next();
  }
};


// 检查用户是否有有效的 admin权限的token，如果没有则重定向到首页
export const requireAdminAuth = (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
  const level = useUserStore().user.level;
  if (!level || level < 8) {    
    console.log('拦截')
    // 如果没有 token，则重定向到登录页面
    next('/userCenter');
  } else {
    console.log('不拦截,权限验证通过')
    // 如果有 token，允许继续导航
    next();
  }
};

