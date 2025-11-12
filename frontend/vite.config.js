import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      // 前端本地开发到 Node 代理服务
      '/api': {
        target: 'http://localhost:3000',
        changeOrigin: true,
        // 若 Node 代理去掉 /api 前缀，这里也需要保持一致
        // rewrite: (path) => path.replace(/^\/api/, '')
      },
    },
  },
})
