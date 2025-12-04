import { createApp } from 'vue'
import InventoryApp from './InventoryApp.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: InventoryApp
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const container = document.getElementById('app')
if (container) {
  const app = createApp(InventoryApp)
  app.use(router)
  app.mount('#app')
}

export default InventoryApp
