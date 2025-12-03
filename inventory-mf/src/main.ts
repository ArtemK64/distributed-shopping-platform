import { createApp } from 'vue'
import InventoryApp from './InventoryApp.vue'

const container = document.getElementById('app')
if (container) {
  const app = createApp(InventoryApp)
  app.mount('#app')
}

export default InventoryApp
