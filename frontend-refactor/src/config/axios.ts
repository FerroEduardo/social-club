import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL

axios.defaults.baseURL = API_URL
axios.defaults.timeout = 5_000 // 5000 ms = 5 s
axios.defaults.withCredentials = true

axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      console.error(error.response.data)
      console.error(error.response.status)
      console.error(error.response.headers)
    } else if (error.request) {
      console.log(error.request)
    } else {
      console.error('Error', error.message)
    }

    return Promise.reject(error)
  }
)
