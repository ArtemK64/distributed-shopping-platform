import { api } from './axios'
import type { CreateOrderRequest, ProductInfoDto } from '../types'

export const fetchProducts = async (): Promise<ProductInfoDto[]> => {
  const res = await api.get('/api/products')
  return res.data
}

export const submitOrder = async (payload: CreateOrderRequest): Promise<void> => {
  await api.post('/api/orders', payload)
}
