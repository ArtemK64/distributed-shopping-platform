import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import federation from '@originjs/vite-plugin-federation'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  
  return {
    plugins: [
      vue(),
      federation({
        name: 'container',
        remotes: {
          'inventory-mf': '/inventory-mf/assets/remoteEntry.js',
          'orders-mf': '/orders-mf/assets/remoteEntry.js',
          'notifications-mf': '/notifications-mf/assets/remoteEntry.js',
        },
        shared: ['vue']
      })
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    build: {
      target: 'esnext',
      minify: false,
      cssCodeSplit: false,
      rollupOptions: {
        output: {
          format: 'es',
          entryFileNames: 'assets/[name].js',
          chunkFileNames: 'assets/[name].js',
          assetFileNames: 'assets/[name].[ext]'
        }
      }
    },
    server: {
      port: 5173,
      cors: true,
      proxy: {
        '/inventory-mf': {
          target: 'http://localhost:5001',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/inventory-mf/, '')
        },
        '/orders-mf': {
          target: 'http://localhost:5002',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/orders-mf/, '')
        },
        '/notifications-mf': {
          target: 'http://localhost:5003',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/notifications-mf/, '')
        }
      }
    }
  }
})
