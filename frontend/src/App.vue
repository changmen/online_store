<template>
  <main style="padding: 24px; font-family: system-ui, -apple-system, Segoe UI, Roboto, Ubuntu, Cantarell, Noto Sans, sans-serif;">
    <h1>Vue + Node 代理演示</h1>
    <p>点击下方按钮，调用 <code>/api/v1/categories/1</code> 通过 Node 代理转发到后端。</p>
    <button @click="callHealth" style="padding: 8px 16px;">调用 /api/v1/categories/1</button>
    <pre v-if="result" style="margin-top: 16px; background:#f5f5f5; padding:12px;">{{ result }}</pre>
  </main>
</template>

<script setup>
import { ref } from 'vue'

const result = ref('')

async function callHealth() {
  try {
    const res = await fetch('/api/v1/categories/1')
    const json = await res.json()
    result.value = JSON.stringify(json, null, 2)
  } catch (e) {
    result.value = '调用失败: ' + (e?.message || e)
  }
}
</script>
