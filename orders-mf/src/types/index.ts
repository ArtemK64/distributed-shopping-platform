export interface CreateOrderRequest {
  customer: {
    firstName: string
    lastName: string
    middleName?: string
    age: number | null
    email: string
    phone: string
  }
  shippingAddress: {
    country: string
    city: string
    street: string
    house: string
    postalCode: string
  }
  items: {
    productName: string
    quantity: number
  }[]
}

export interface ProductInfoDto {
  id: number | string
  name: string
  category: string
  manufacturer: string
  quantity: number
  price: number
}
