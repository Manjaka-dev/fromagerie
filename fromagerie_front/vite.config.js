import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  define: {
    global: 'globalThis',
    'process.env': {}
  },
  server: {
    host: true,
    port: 3000
  },
  optimizeDeps: {
    exclude: ['fsevents']
  }
})
