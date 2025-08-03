import { createRouter, createWebHistory } from 'vue-router'

import ProductsView from '@/views/ProductsView.vue'
import OrdersView from '@/views/OrdersView.vue'
import NotificationsView from '@/views/NotificationsView.vue'
import AboutView from '@/views/AboutView.vue'

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'products', component: ProductsView },
    { path: '/order', name: 'orders', component: OrdersView },
    { path: '/notifications', name: 'notifications', component: NotificationsView },
    { path: '/about', name: 'about', component: AboutView }
  ]
})
