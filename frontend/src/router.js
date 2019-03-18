import Vue from 'vue'
import Router from 'vue-router'
import Home from './components/MainFrame.vue'
import User from './views/User.vue'
import NotFound from './views/NotFound.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
      {
          path: '/',
          component: Home,
      },
      {
          path: '/user',
          component: User,
      },
      {
          path: '*',
          component: NotFound
      }
  ]
})

