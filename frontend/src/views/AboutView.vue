<template>
  <div class="about-page">
    <h2>О проекте</h2>
    <p>
      Данный учебный проект представляет собой торговую платформу с возможностью оформления заказов, 
      учета складских остатков и системой уведомлений. Пользователь выбирает товар, указывает адрес доставки 
      и получает статус заказа.
    </p>
  </div>
</template>

<style scoped>
.about-page {
  max-width: 800px;
  margin: 0 auto;
  font-size: 16px;
  color: var(--color-text);
  line-height: 1.8;
  text-align: justify;
}

.about-page h2 {
  text-align: center;
  margin-bottom: 1.5rem;
}

@media (max-width: 768px) {
  .about-page {
    margin: 0.5rem 1rem 5rem 1rem;
  }
}
</style>
