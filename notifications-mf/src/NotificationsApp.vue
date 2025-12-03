<template>
  <div class="mt-4">
    <h2 class="results-title mb-3">Найти отправленные уведомления</h2>

    <form class="search-form" @submit.prevent="searchNotifications">
      <div class="form-group search-group">
        <input
          id="email"
          v-model="emailFilter"
          type="email"
          placeholder="Введите email"
          required
          maxlength="255"
        />
      </div>
      <button type="submit">Поиск</button>
    </form>

    <div class="table-wrapper">
      <table class="results-table">
        <thead>
          <tr>
            <th>Кому</th>
            <th class="hide-on-mobile">Тема</th>
            <th class="hide-on-mobile">Дата</th>
            <th class="hide-on-mobile">Статус</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(n, index) in filteredNotifications" :key="n.id">
            <tr>
              <td>
                <div class="mobile-row-header">
                  {{ n.recipientAddress }}
                  <button
                    class="expand-toggle"
                    @click="toggleNotificationExpanded(index)"
                  >
                    {{ notificationExpanded[index] ? 'Скрыть' : 'Показать' }}
                  </button>
                </div>
              </td>
              <td class="hide-on-mobile">{{ n.subject }}</td>
              <td class="hide-on-mobile">{{ new Date(n.sentAt).toLocaleString() }}</td>
              <td class="hide-on-mobile">{{ n.status }}</td>
            </tr>

            <tr v-if="notificationExpanded[index]" class="mobile-expanded-row">
              <td colspan="4">
                <div><strong>Тема:</strong> {{ n.subject }}</div>
                <div><strong>Дата:</strong> {{ new Date(n.sentAt).toLocaleString() }}</div>
                <div><strong>Статус:</strong> {{ n.status }}</div>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { fetchNotificationsByEmail } from '@/api/api'
import type { NotificationDto } from '@/types'

const emailFilter = ref('')
const notifications = ref<NotificationDto[]>([])
const notificationExpanded = ref<boolean[]>([])
const isMobile = ref(window.innerWidth < 768)

onMounted(() => {
  window.addEventListener('resize', () => {
    const nowMobile = window.innerWidth < 768
    if (isMobile.value && !nowMobile) {
      notificationExpanded.value = Array(notifications.value.length).fill(false)
    }
    isMobile.value = nowMobile
  })
})

async function searchNotifications() {
  const email = emailFilter.value.trim()
  if (!email) return

  const data = await fetchNotificationsByEmail(email)
  notifications.value = data
  notificationExpanded.value = Array(data.length).fill(false)
}

function toggleNotificationExpanded(index: number) {
  notificationExpanded.value[index] = !notificationExpanded.value[index]
}

const filteredNotifications = computed(() => {
  const email = emailFilter.value.trim().toLowerCase()
  return email
    ? notifications.value.filter(n =>
        n.recipientAddress.toLowerCase().includes(email)
      )
    : notifications.value
})
</script>

<style scoped>
.results-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--color-heading);
  text-align: center;
  margin-bottom: 2rem;
}

.search-form {
  max-width: 800px;
  margin: 0 auto 2rem auto;
  display: flex;
  align-items: flex-end;
  gap: 1rem;
  flex-wrap: wrap;
}

.search-group {
  flex: 1;
}

.search-form input {
  padding: 0.5rem 1.75rem 0.5rem 0.75rem;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  font-size: 14px;
  background-color: var(--color-input-background);
  color: var(--color-input-text);
  width: 100%;
  box-sizing: border-box;
}

.search-form button {
  padding: 0.6rem 2rem;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  font-size: 14px;
  white-space: nowrap;
}

.search-form button:hover {
  background-color: #369b6c;
}

.table-wrapper {
  width: 100%;
  display: flex;
  justify-content: center;
  overflow-x: auto;
}

.results-table {
  width: 100%;
  max-width: 800px;
  border-collapse: collapse;
  background-color: var(--color-background-soft);
  color: var(--color-text);
  margin-bottom: 2rem;
}

.results-table th,
.results-table td {
  padding: 0.75rem 1rem;
  border: 1px solid var(--color-border);
  text-align: left;
}

.results-table th {
  background-color: var(--color-background-mute);
  color: var(--color-heading);
}

.mobile-row-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.expand-toggle {
  display: none;
}

@media (max-width: 768px) {
  .search-form {
    flex-direction: column;
    align-items: stretch;
  }

  .search-form button {
    width: 100%;
  }

  .hide-on-mobile {
    display: none;
  }

  .expand-toggle {
    display: inline-block;
    padding: 0.25rem 0.75rem;
    border: 1px solid var(--color-border);
    background: transparent;
    border-radius: 4px;
    font-size: 13px;
    cursor: pointer;
    color: var(--color-text);
  }

  .expand-toggle:hover {
    background-color: var(--color-background-mute);
  }

  .mobile-expanded-row td {
    background-color: var(--color-background-mute);
    padding: 1rem;
    font-size: 14px;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
}
</style>
