import { createRouter, createWebHistory } from 'vue-router'
import { defineAsyncComponent } from 'vue'
import AboutPage from '../components/AboutPage.vue'

const InventoryApp = defineAsyncComponent(() => import('inventory-mf/InventoryApp'))
const OrdersApp = defineAsyncComponent(() => import('orders-mf/OrdersApp'))
const NotificationsApp = defineAsyncComponent(() => import('notifications-mf/NotificationsApp'))

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/inventory'
    },
    {
      path: '/inventory',
      name: 'inventory',
      component: InventoryApp
    },
    {
      path: '/orders',
      name: 'orders',
      component: OrdersApp
    },
    {
      path: '/notifications',
      name: 'notifications',
      component: NotificationsApp
    },
    {
      path: '/about',
      name: 'about',
      component: AboutPage
    }
  ]
})

export default router
