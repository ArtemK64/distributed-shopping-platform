import { createApp } from 'vue'
import NotificationsApp from './NotificationsApp.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: NotificationsApp
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const container = document.getElementById('app')
if (container) {
  const app = createApp(NotificationsApp)
  app.use(router)
  app.mount('#app')
}

export default NotificationsApp
