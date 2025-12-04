<template>
  <div class="layout-wrapper">
    <header class="top-nav">
      <div class="container">
        <div class="left-block">
          <a href="/" class="site-title" @click.prevent="navigateTo('/inventory')">Торговая платформа</a>
          <nav class="main-nav">
            <a href="#" @click.prevent="navigateTo('/inventory')" :class="{ active: currentRoute === '/inventory' }">Товары</a>
            <a href="#" @click.prevent="navigateTo('/orders')" :class="{ active: currentRoute === '/orders' }">Заказать</a>
            <a href="#" @click.prevent="navigateTo('/notifications')" :class="{ active: currentRoute === '/notifications' }">Уведомления</a>
            <a href="#" @click.prevent="navigateTo('/about')" :class="{ active: currentRoute === '/about' }">О проекте</a>
          </nav>
          <button class="burger-button" @click="toggleMobileMenu" aria-label="Меню">
            <Icon :icon="menuIcon" />
          </button>
        </div>

        <button class="theme-toggle" @click="toggleTheme">
          <Icon :icon="theme === 'light' ? moonIcon : sunIcon" />
        </button>
      </div>

      <div :class="{ 'mobile-nav-wrapper': true, open: mobileMenuOpen }">
        <nav v-if="mobileMenuOpen" class="mobile-nav">
          <a href="#" @click.prevent="navigateTo('/inventory')" :class="{ active: currentRoute === '/inventory' }">Товары</a>
          <a href="#" @click.prevent="navigateTo('/orders')" :class="{ active: currentRoute === '/orders' }">Заказать</a>
          <a href="#" @click.prevent="navigateTo('/notifications')" :class="{ active: currentRoute === '/notifications' }">Уведомления</a>
          <a href="#" @click.prevent="navigateTo('/about')" :class="{ active: currentRoute === '/about' }">О проекте</a>
        </nav>
      </div>
    </header>

    <main class="content">
      <component :is="currentComponent" />
    </main>

    <footer class="site-footer">
      <p>Учебный проект в портфолио • 2025</p>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, shallowRef } from 'vue'
import { Icon } from '@iconify/vue'
import AboutPage from './components/AboutPage.vue'

import { defineAsyncComponent } from 'vue'

const InventoryApp = defineAsyncComponent(() => import('inventory-mf/InventoryApp'))
const OrdersApp = defineAsyncComponent(() => import('orders-mf/OrdersApp'))
const NotificationsApp = defineAsyncComponent(() => import('notifications-mf/NotificationsApp'))

const theme = ref<'light' | 'dark'>('light')
const mobileMenuOpen = ref(false)
const currentComponent = shallowRef<any>(null)
const currentRoute = ref(window.location.hash.replace('#', '') || '/inventory')

const sunIcon = 'mdi:weather-sunny'
const moonIcon = 'mdi:weather-night'
const menuIcon = 'mdi:menu'

onMounted(() => {
  const saved = localStorage.getItem('theme')
  theme.value = saved === 'dark' ? 'dark' : 'light'
  applyTheme(theme.value)
  
  if (!window.location.hash || window.location.hash === '#/' || window.location.hash === '#') {
    window.location.hash = '/inventory'
    currentRoute.value = '/inventory'
  }
  
  updateRouteFromHash()
  window.addEventListener('hashchange', updateRouteFromHash)
})

function toggleTheme() {
  theme.value = theme.value === 'light' ? 'dark' : 'light'
  applyTheme(theme.value)
}

function applyTheme(value: string) {
  document.documentElement.setAttribute('data-theme', value)
  localStorage.setItem('theme', value)
}

function toggleMobileMenu() {
  mobileMenuOpen.value = !mobileMenuOpen.value
}

function updateRouteFromHash() {
  const hash = window.location.hash.replace('#', '')
  currentRoute.value = hash ? hash : '/inventory'
  loadComponentForRoute(currentRoute.value)
}

async function loadComponentForRoute(route: string) {
  console.log('Loading component for route:', route)
  currentComponent.value = null
  
  if (route === '/inventory') {
    try {
      const module = await import('inventory-mf/InventoryApp')
      currentComponent.value = module.default
    } catch (error) {
      console.error('Failed to load inventory app:', error)
    }
  } else if (route === '/orders') {
    try {
      const module = await import('orders-mf/OrdersApp')
      currentComponent.value = module.default
    } catch (error) {
      console.error('Failed to load orders app:', error)
    }
  } else if (route === '/notifications') {
    try {
      const module = await import('notifications-mf/NotificationsApp')
      currentComponent.value = module.default
    } catch (error) {
      console.error('Failed to load notifications app:', error)
    }
  } else if (route === '/about') {
    currentComponent.value = AboutPage
  }
}

function navigateTo(path: string) {
  console.log('Navigating to:', path)
  window.location.hash = path
  mobileMenuOpen.value = false
}
</script>

<style scoped>
:global(body) {
  margin: 0;
  padding: 0;
  overflow-x: hidden;
}

.layout-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  max-width: 100vw;
  overflow-x: hidden;
}

.top-nav {
  background-color: var(--color-background);
  border-bottom: 1px solid var(--color-border);
  padding: 1rem;
  position: sticky;
  top: 0;
  z-index: 10;
}

.container {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.left-block {
  display: flex;
  align-items: center;
  gap: 2rem;
  flex-wrap: wrap;
  flex: 1;
}

.site-title {
  font-family: 'Poppins', sans-serif;
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--color-heading);
  margin: 0;
  text-decoration: none;
  cursor: pointer;
}

.main-nav {
  display: flex;
  gap: 1rem;
}

.main-nav a {
  text-decoration: none;
  color: var(--color-text);
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.2s;
  cursor: pointer;
}

.main-nav a:hover,
.main-nav a:active {
  background-color: var(--color-background-mute);
}

.main-nav a.active {
  background-color: var(--color-background-mute);
}

.theme-toggle {
  background: none;
  border: none;
  cursor: pointer;
  color: var(--color-text);
  font-size: 1.5rem;
}

.burger-button {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  color: var(--color-text);
  font-size: 1.5rem;
}

.mobile-nav-wrapper {
  height: 0;
  overflow: hidden;
  transition: height 0.3s ease;
}

.mobile-nav-wrapper.open {
  height: auto;
  margin-bottom: 1rem;
}

.mobile-nav {
  display: none;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 0;
}

.mobile-nav a {
  text-decoration: none;
  color: var(--color-text);
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.2s;
  cursor: pointer;
}

.mobile-nav a:hover,
.mobile-nav a:active {
  background-color: var(--color-background-mute);
}

.mobile-nav a.active {
  background-color: var(--color-background-mute);
}

@media (max-width: 992px) {
  .burger-button {
    display: block;
    align-self: center;
    margin-top: 0.5rem;
  }

  .container {
    flex-direction: column;
    align-items: center;
    text-align: center;
    margin-top: 1rem;
  }

  .left-block {
    flex-direction: column;
    align-items: center;
    gap: 0.5rem;
  }

  .main-nav {
    display: none;
  }

  .mobile-nav {
    display: flex;
  }

  .theme-toggle {
    order: -1;
    margin-bottom: 1rem;
  }
}

.content {
  flex: 1;
  width: 90%;
  max-width: 960px;
  margin: 1rem auto 0;
  background-color: var(--color-background);
}

.site-footer {
  text-align: center;
  padding: 0.5rem;
  font-size: 0.9rem;
  color: var(--color-text-light-2);
  background-color: var(--color-background-soft);
  border-top: 1px solid var(--color-border);
  margin-top: auto;
}
</style>
