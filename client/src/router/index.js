import Vue from 'vue'
import Router from 'vue-router'
import Help from '@/pages/help/index'
import Portfolios from '@/pages/portfolio/index'
import Portfolio from '@/pages/portfolio/show'
import NewPortfolio from '@/pages/portfolio/new'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'portfolios',
      component: Portfolios
    },
    {
      path: '/portfolio/:id',
      name: 'portfolio',
      component: Portfolio
    },
    {
      path: '/portfolio/new',
      name: 'new-portfolio',
      component: NewPortfolio
    },
    {
      path: '/help',
      name: 'help',
      component: Help
    }
  ]
})
