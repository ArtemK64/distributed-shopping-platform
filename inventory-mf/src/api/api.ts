import { api } from './axios'
import type { ProductInfoDto } from '../types'

export const fetchProducts = async (): Promise<ProductInfoDto[]> => {
  const res = await api.get('/api/products')
  return res.data
}
