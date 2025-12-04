import { createApp } from 'vue'
import { createPinia } from 'pinia'
import OrdersApp from './OrdersApp.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: OrdersApp
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const container = document.getElementById('app')
if (container) {
  const app = createApp(OrdersApp)
  const pinia = createPinia()
  app.use(pinia)
  app.use(router)
  app.mount('#app')
}

export default OrdersApp
