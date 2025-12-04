<template>
  <div class="layout-wrapper">
    <header class="top-nav">
      <div class="container">
        <div class="left-block">
          <router-link to="/inventory" class="site-title">Торговая платформа</router-link>
          <nav class="main-nav">
            <router-link to="/inventory" :class="{ active: $route.path === '/inventory' }">Товары</router-link>
            <router-link to="/orders" :class="{ active: $route.path === '/orders' }">Заказать</router-link>
            <router-link to="/notifications" :class="{ active: $route.path === '/notifications' }">Уведомления</router-link>
            <router-link to="/about" :class="{ active: $route.path === '/about' }">О проекте</router-link>
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
          <router-link to="/inventory" @click="toggleMobileMenu" :class="{ active: $route.path === '/inventory' }">Товары</router-link>
          <router-link to="/orders" @click="toggleMobileMenu" :class="{ active: $route.path === '/orders' }">Заказать</router-link>
          <router-link to="/notifications" @click="toggleMobileMenu" :class="{ active: $route.path === '/notifications' }">Уведомления</router-link>
          <router-link to="/about" @click="toggleMobileMenu" :class="{ active: $route.path === '/about' }">О проекте</router-link>
        </nav>
      </div>
    </header>

    <main class="content">
      <router-view v-slot="{ Component }">
        <component :is="Component" />
      </router-view>
    </main>

    <footer class="site-footer">
      <p>Учебный проект в портфолио • 2025</p>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Icon } from '@iconify/vue'

const theme = ref<'light' | 'dark'>('light')
const mobileMenuOpen = ref(false)

const sunIcon = 'mdi:weather-sunny'
const moonIcon = 'mdi:weather-night'
const menuIcon = 'mdi:menu'

onMounted(() => {
  const saved = localStorage.getItem('theme')
  theme.value = saved === 'dark' ? 'dark' : 'light'
  applyTheme(theme.value)
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
