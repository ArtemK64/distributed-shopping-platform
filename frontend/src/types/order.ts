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
