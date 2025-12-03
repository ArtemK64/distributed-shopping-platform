import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/inventory'
  },
  {
    path: '/inventory',
    name: 'Inventory',
    component: () => import('inventory-mf/InventoryApp')
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('orders-mf/OrdersApp')
  },
  {
    path: '/notifications',
    name: 'Notifications',
    component: () => import('notifications-mf/NotificationsApp')
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('./views/About.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
