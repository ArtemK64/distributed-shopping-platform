<script setup lang="ts">
import { RouterView, RouterLink } from 'vue-router'
import { ref, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import sunIcon from '@iconify-icons/mdi/weather-sunny'
import moonIcon from '@iconify-icons/mdi/weather-night'
import menuIcon from '@iconify-icons/mdi/menu'

const theme = ref<'light' | 'dark'>('light')
const mobileMenuOpen = ref(false)

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

<template>
  <div class="layout-wrapper">
    <header class="top-nav">
      <div class="container">
        <div class="left-block">
          <RouterLink to="/" class="site-title">Торговая платформа</RouterLink>
          <nav class="main-nav">
            <RouterLink to="/">Товары</RouterLink>
            <RouterLink to="/order">Заказать</RouterLink>
            <RouterLink to="/notifications">Уведомления</RouterLink>
            <RouterLink to="/about">О проекте</RouterLink>
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
          <RouterLink to="/" @click="mobileMenuOpen = false">Товары</RouterLink>
          <RouterLink to="/order" @click="mobileMenuOpen = false">Заказать</RouterLink>
          <RouterLink to="/notifications" @click="mobileMenuOpen = false">Уведомления</RouterLink>
          <RouterLink to="/about" @click="mobileMenuOpen = false">О проекте</RouterLink>
        </nav>
      </div>
    </header>

    <main class="content">
      <RouterView />
    </main>

    <footer class="site-footer">
      <p>Учебный проект в портфолио • 2025</p>
    </footer>
  </div>
</template>

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
}

.main-nav a:hover,
.main-nav a.router-link-exact-active {
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
}

.mobile-nav a:hover,
.mobile-nav a.router-link-exact-active {
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
