import { api } from './axios'
import type { NotificationDto } from '../types'

export const fetchNotificationsByEmail = async (
  email: string
): Promise<NotificationDto[]> => {
  const res = await api.get(`/api/notifications/email/${email}`)
  return res.data
}
