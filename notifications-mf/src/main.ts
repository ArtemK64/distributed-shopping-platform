import { createApp } from 'vue'
import NotificationsApp from './NotificationsApp.vue'

const container = document.getElementById('app')
if (container) {
  const app = createApp(NotificationsApp)
  app.mount('#app')
}

export default NotificationsApp
