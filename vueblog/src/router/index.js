import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Registry from '@/components/Registry'
import Home from '@/components/Home'
import ArticleList from '@/components/ArticleList'
// import CateMana from '@/components/CateMana'
// import DataCharts from '@/components/DataCharts'
import PostArticle from '@/components/PostArticle'
// import UserMana from '@/components/UserMana'
import BlogDetail from '@/components/BlogDetail'
import Topic from '@/components/Topic'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: '登录',
      hidden: true,
      component: Login
    }, {
      path: "/registry",
      name: '注册',
      hidden: true,
      component: Registry
    }, {
      path: '/home',
      name: '',
      component: Home,
      hidden: true
    }, {
      path: '/art',
      component: Home,
      name: '问题列表',
      children: [
        {
          path: '/articleList',
          name: '问题列表',
          component: ArticleList,
          meta: {
            // 设置页面缓存
            // keepAlive: true
          }
        }
      ]
    }, {
      path: '/top',
      component: Home,
      name: '',
      children: [
        {
          path: '/topic',
          name: '话题列表',
          component: Topic
        }
      ]
    },
    {
      path: '/art',
      component: Home,
      hidden: true,
      name: '问题列表',
      children: [
        {
          path: '/blogDetail',
          name: '博客详情',
          component: BlogDetail,
          hidden: true,
          meta: {
            keepAlive: false
          }
        }
      ]
    },
    {
      path: '/art',
      component: Home,
      hidden: true,
      name: '问题列表',
      children: [
        {
          path: '/blogDetail',
          name: '博客详情',
          component: BlogDetail,
          hidden: true,
          meta: {
            keepAlive: false
          }
        }
      ]
    },
    {
      path: '/editBlog',
      name: '编辑博客',
      component: PostArticle,
      hidden: true,
      meta: {
        keepAlive: false
      }
    }
    // , {
    //   path: '/home',
    //   component: Home,
    //   name: '用户管理',
    //   children: [
    //     {
    //       path: '/user',
    //       iconCls: 'fa fa-user-o',
    //       name: '用户管理',
    //       component: UserMana
    //     }
    //   ]
    // }, {
    //   path: '/home',
    //   component: Home,
    //   name: '栏目管理',
    //   children: [
    //     {
    //       path: '/cateMana',
    //       iconCls: 'fa fa-reorder',
    //       name: '栏目管理',
    //       component: CateMana
    //     }
    //   ]
    // }, {
    //   path: '/home',
    //   component: Home,
    //   name: '数据统计',
    //   iconCls: 'fa fa-bar-chart',
    //   children: [
    //     {
    //       path: '/charts',
    //       iconCls: 'fa fa-bar-chart',
    //       name: '数据统计',
    //       component: DataCharts
    //     }
    //   ]
    // }
  ]
})
