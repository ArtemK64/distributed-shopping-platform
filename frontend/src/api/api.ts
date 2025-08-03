import { api } from './axios'
import type { ProductInfoDto, NotificationDto, CreateOrderRequest } from '@/types'

export const fetchProducts = async (): Promise<ProductInfoDto[]> => {
  const res = await api.get('/products')
  return res.data
}

export const fetchNotificationsByEmail = async (
  email: string
): Promise<NotificationDto[]> => {
  const res = await api.get(`/notifications/email/${email}`)
  return res.data
}

export const submitOrder = async (payload: CreateOrderRequest): Promise<void> => {
  await api.post('/orders', payload)
}
