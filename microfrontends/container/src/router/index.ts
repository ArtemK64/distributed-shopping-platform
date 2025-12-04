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
      name: 'inventory',
      component: InventoryApp
    },
    {
      path: '/order',
      name: 'order',
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
