<template>
  <div class="mt-4">
    <h2 class="results-title mb-3">Список доступных товаров</h2>

    <form class="search-form" @submit.prevent="searchProducts">
      <div class="form-group search-group">
        <input
          id="productName"
          v-model="nameFilter"
          type="text"
          placeholder="Введите наименование"
          required
          maxlength="150"
        />
      </div>
      <button type="submit">Поиск</button>
    </form>

    <div class="table-wrapper">
      <table class="results-table">
        <thead>
          <tr>
            <th>Название</th>
            <th class="hide-on-mobile">Категория</th>
            <th class="hide-on-mobile">Производитель</th>
            <th class="hide-on-mobile">Остаток</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="item in filteredProducts" :key="item.id">
            <tr>
              <td>
                <div class="mobile-row-header">
                  {{ item.name }}
                  <button
                    class="expand-toggle"
                    @click="toggleExpanded(item.id)"
                  >
                    {{ expandedRows[item.id] ? 'Скрыть' : 'Показать' }}
                  </button>
                </div>
              </td>
              <td class="hide-on-mobile">{{ item.category }}</td>
              <td class="hide-on-mobile">{{ item.manufacturer }}</td>
              <td class="hide-on-mobile">{{ item.quantity }}</td>
            </tr>

            <tr v-if="expandedRows[item.id]" class="mobile-expanded-row">
              <td colspan="4">
                <div><strong>Категория:</strong> {{ item.category }}</div>
                <div><strong>Производитель:</strong> {{ item.manufacturer }}</div>
                <div><strong>Остаток:</strong> {{ item.quantity }}</div>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { fetchProducts } from '@/api/api'
import type { ProductInfoDto } from '@/types'

const products = ref<ProductInfoDto[]>([])
const nameFilter = ref('')
const expandedRows = ref<Record<string | number, boolean>>({})
const isMobile = ref(window.innerWidth < 768)

onMounted(async () => {
  const fetched = await fetchProducts()
  if (Array.isArray(fetched)) {
    products.value = fetched
    fetched.forEach(p => {
      expandedRows.value[p.id] = false
    })
  } else {
    console.error('fetchProducts не вернул массив', fetched)
    products.value = []
  }
})

window.addEventListener('resize', () => {
  const nowMobile = window.innerWidth < 768
  if (isMobile.value && !nowMobile) {
    expandedRows.value = {}
  }
  isMobile.value = nowMobile
})

function toggleExpanded(id: string | number) {
  expandedRows.value[id] = !expandedRows.value[id]
}

const filteredProducts = computed(() => {
  const name = nameFilter.value.trim().toLowerCase()
  return name
    ? products.value.filter(p => p.name.toLowerCase().includes(name))
    : products.value
})

function searchProducts() {}
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
  margin: 0 auto 2rem;
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
  margin-bottom: 2rem;
  border-collapse: collapse;
  background-color: var(--color-background-soft);
  color: var(--color-text);
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

:root[data-theme='dark'] .expand-toggle {
  color: white;
  border-color: white;
}

:root[data-theme='dark'] .expand-toggle:hover {
  background-color: var(--color-background-mute);
  color: white;
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
