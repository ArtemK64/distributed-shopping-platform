<template>
  <form class="form-card" @submit.prevent="submitForm">
    <h2>Оформление заказа</h2>

    <h3>Данные клиента</h3>
    <div class="form-grid">
      <div
        class="form-group"
        v-for="(field, key) in customerFields"
        :key="key"
      >
        <label :for="key">{{ field.label }}</label>
        <input
          v-model="form.customer[key as CustomerFieldKey]"
          v-bind="field"
          :id="key"
        />
      </div>
    </div>

    <h3>Адрес доставки</h3>
    <div class="form-grid">
      <div
        class="form-group"
        v-for="(field, key) in addressFields"
        :key="key"
      >
        <label :for="key">{{ field.label }}</label>
        <input
          v-model="form.shippingAddress[key as AddressFieldKey]"
          v-bind="field"
          :id="key"
        />
      </div>
    </div>

    <h3 class="products-title">Товары</h3>
    <div class="product-grid">
      <div
        class="product-block"
        v-for="(item, index) in form.items"
        :key="index"
      >
        <div class="product-line">
          <div class="form-group small-group">
            <label :for="'productName-' + index">Наименование</label>
            <select
              v-model="item.productName"
              :id="'productName-' + index"
              required
            >
              <option disabled value="">-- выберите --</option>
              <option
                v-for="product in getAvailableProducts(index)"
                :key="product"
                :value="product"
              >
                {{ product }}
              </option>
            </select>
          </div>

          <div class="form-group small-group">
            <label :for="'quantity-' + index">Количество</label>
            <input
              type="number"
              min="1"
              v-model.number="item.quantity"
              :id="'quantity-' + index"
              required
            />
          </div>

          <div
            class="buttons-inline"
            :class="{
              single: form.items.length === 1 || getAvailableProducts(index).length === 1
            }"
          >
            <button
              v-if="canAddMoreProducts && index === form.items.length - 1"
              type="button"
              class="outlined-button"
              @click="addItem"
            >
              Добавить
            </button>
            <button
              v-if="form.items.length > 1"
              type="button"
              class="outlined-button"
              @click="removeItem(index)"
            >
              Удалить
            </button>
          </div>
        </div>

        <div class="price-line" v-if="itemPrices[index] > 0">
          <span class="price-label">Стоимость:</span>
          <span class="price-value">{{ itemPrices[index].toFixed(2) }} ₽</span>
        </div>
      </div>
    </div>

    <div class="total-price" v-if="totalPrice > 0">
      <strong>Итого:</strong> {{ totalPrice.toFixed(2) }} ₽
    </div>

    <button type="submit">Отправить</button>
  </form>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { submitOrder, fetchProducts } from '@/api/api'
import type { CreateOrderRequest, ProductInfoDto } from '@/types'

type FieldConfig = {
  label: string
  type: string
  required: boolean
  maxlength?: number
  min?: number
  max?: number
}

type CustomerFieldKey = keyof CreateOrderRequest['customer']
type AddressFieldKey = keyof CreateOrderRequest['shippingAddress']

const allProducts = ref<ProductInfoDto[]>([])

const form = reactive<CreateOrderRequest>({
  customer: {
    firstName: '',
    lastName: '',
    middleName: '',
    age: 18,
    email: '',
    phone: ''
  },
  shippingAddress: {
    country: '',
    city: '',
    street: '',
    house: '',
    postalCode: ''
  },
  items: [
    {
      productName: '',
      quantity: 1
    }
  ]
})

const customerFields: Record<CustomerFieldKey, FieldConfig> = {
  firstName: { label: 'Имя', type: 'text', required: true, maxlength: 50 },
  lastName: { label: 'Фамилия', type: 'text', required: true, maxlength: 50 },
  middleName: { label: 'Отчество', type: 'text', required: false, maxlength: 50 },
  age: { label: 'Возраст', type: 'number', required: true, min: 18, max: 120 },
  email: { label: 'Электронная почта', type: 'email', required: true, maxlength: 255 },
  phone: { label: 'Телефон', type: 'text', required: true, maxlength: 16 }
}

const addressFields: Record<AddressFieldKey, FieldConfig> = {
  country: { label: 'Страна', type: 'text', required: true, maxlength: 100 },
  city: { label: 'Город', type: 'text', required: true, maxlength: 100 },
  street: { label: 'Улица', type: 'text', required: true, maxlength: 100 },
  house: { label: 'Дом', type: 'text', required: true, maxlength: 20 },
  postalCode: { label: 'Индекс', type: 'text', required: true, maxlength: 10 }
}

const canAddMoreProducts = computed(() =>
  form.items.length < allProducts.value.length
)

function getAvailableProducts(currentIndex: number): string[] {
  const selected = form.items
    .map((item, i) => (i === currentIndex ? null : item.productName))
    .filter(Boolean)

  const currentValue = form.items[currentIndex].productName

  return allProducts.value
    .map(p => p.name)
    .filter(name => !selected.includes(name) || name === currentValue)
}

function getProductByName(name: string): ProductInfoDto | undefined {
  return allProducts.value.find(p => p.name === name)
}

const itemPrices = computed(() =>
  form.items.map(item => {
    const product = getProductByName(item.productName)
    return product ? product.price * item.quantity : 0
  })
)

const totalPrice = computed(() =>
  itemPrices.value.reduce((sum, price) => sum + price, 0)
)

function addItem() {
  if (form.items.length < allProducts.value.length) {
    form.items.push({ productName: '', quantity: 1 })
  }
}

function removeItem(index: number) {
  if (form.items.length > 1) {
    form.items.splice(index, 1)
  }
}

async function submitForm() {
  try {
    await submitOrder(form)
    alert('Заказ оформлен')
  } catch (err) {
    console.error('Ошибка при оформлении заказа', err)
    alert('Ошибка при оформлении заказа')
  }
}

onMounted(async () => {
  try {
    const response = await fetchProducts()
    if (Array.isArray(response)) {
      allProducts.value = response
    } else {
      console.error('fetchProducts не вернул массив', response)
    }
  } catch (err) {
    console.error('Ошибка при получении списка товаров', err)
  }
})
</script>

<style scoped>
.form-card {
  background: var(--color-background);
  padding: 1.5rem 3rem 3rem;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  margin: 2rem auto 5rem;
  max-width: 850px;
}

h2 {
  margin-bottom: 2rem;
  text-align: center;
  color: var(--color-heading);
  font-family: 'Poppins', sans-serif;
}

h3 {
  margin: 2.5rem 0 1.5rem;
}

.products-title {
  margin-bottom: 1rem;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.product-grid {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.product-block {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-bottom: 0.75rem;
}

.product-line {
  display: flex;
  flex-wrap: nowrap;
  gap: 1rem;
  align-items: flex-end;
  width: 100%;
}

.form-group {
  display: flex;
  flex-direction: column;
  position: relative;
}

label {
  margin-bottom: 0.5rem;
  font-size: 15px;
  font-weight: 500;
  color: var(--color-text);
}

input,
select {
  padding: 0.5rem 1.75rem 0.5rem 0.75rem;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  font-size: 14px;
  background-color: var(--color-input-background);
  color: var(--color-input-text);
  width: 100%;
  box-sizing: border-box;
  appearance: none;
}

select {
  background-image: url("data:image/svg+xml;utf8,<svg fill='%23333' height='24' viewBox='0 0 24 24' width='24' xmlns='http://www.w3.org/2000/svg'><path d='M7 10l5 5 5-5z'/></svg>");
  background-repeat: no-repeat;
  background-position: right 0.75rem center;
  background-size: 1rem;
}

:root[data-theme='dark'] select {
  background-image: url("data:image/svg+xml;utf8,<svg fill='%23fff' height='24' viewBox='0 0 24 24' width='24' xmlns='http://www.w3.org/2000/svg'><path d='M7 10l5 5 5-5z'/></svg>");
}

input[type='number'] {
  appearance: number-input;
}

select option {
  max-height: 50px;
  overflow-y: auto;
}

select:focus {
  outline: 2px solid var(--color-border-hover, #aaa);
}

.small-group {
  min-width: 200px;
  flex: 1;
}

.buttons-inline {
  display: flex;
  gap: 0.5rem;
}

.buttons-inline.single {
  gap: 0.5rem;
}

button {
  margin: 2rem auto 0;
  padding: 0.75rem 5rem;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  font-size: 14px;
  cursor: pointer;
  display: block;
}

button:hover {
  background-color: #369b6c;
}

.outlined-button {
  padding: 0.5rem 1.25rem;
  border: 1px solid var(--color-text);
  background: transparent;
  color: var(--color-text);
  font-weight: bold;
  border-radius: 4px;
  cursor: pointer;
  white-space: nowrap;
  transition: background-color 0.2s ease;
}

.outlined-button:hover {
  background-color: var(--color-background-mute);
}

:root[data-theme='dark'] .outlined-button {
  border-color: white;
  color: white;
}

.price-line {
  display: flex;
  justify-content: space-between;
  padding: 0.25rem 0.5rem 0;
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text);
  margin-top: 0.25rem;
}

.total-price {
  margin-top: 1.5rem;
  font-size: 17px;
  font-weight: bold;
  text-align: right;
}

@media (max-width: 900px) {
  .form-card {
    padding: 1.5rem;
    margin: 2rem 1rem 5rem;
  }

  .form-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }

  .product-line {
    flex-direction: column;
    align-items: stretch;
  }

  label {
    font-size: 13px;
  }

  select,
  input {
    font-size: 13px;
    padding: 0.5rem 1rem;
  }

  button {
    width: 100%;
    margin: 0.5rem auto 1rem;
  }

  .buttons-inline {
    justify-content: center;
  }
}
</style>
