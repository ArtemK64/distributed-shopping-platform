import { createApp } from 'vue'
import { createPinia } from 'pinia'
import OrdersApp from './OrdersApp.vue'

const container = document.getElementById('app')
if (container) {
  const app = createApp(OrdersApp)
  const pinia = createPinia()
  app.use(pinia)
  app.mount('#app')
}

export default OrdersApp
