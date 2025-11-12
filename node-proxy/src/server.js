/*
 * Node.js 单接口代理服务
 * - 将 /api/* 的请求转发到 TARGET_ORIGIN
 * - 通过 ALLOWED_HOSTS 白名单限制目标主机，避免开放代理风险
 */

const express = require('express');
const morgan = require('morgan');
const cors = require('cors');
const { createProxyMiddleware } = require('http-proxy-middleware');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3000;
const TARGET_ORIGIN = process.env.TARGET_ORIGIN || 'http://localhost:8080';
const ALLOWED_HOSTS = (process.env.ALLOWED_HOSTS || 'localhost,127.0.0.1')
  .split(',')
  .map(h => h.trim())
  .filter(Boolean);

// 基础中间件
app.use(morgan('dev'));
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// 简单健康检查
app.get('/health', (req, res) => {
  res.json({ status: 'ok', port: PORT, target: TARGET_ORIGIN });
});

// 安全检查：只允许目标主机在白名单中
function isHostAllowed(targetUrl) {
  try {
    const u = new URL(targetUrl);
    return ALLOWED_HOSTS.includes(u.hostname);
  } catch (e) {
    return false;
  }
}

if (!isHostAllowed(TARGET_ORIGIN)) {
  console.error('[Proxy] TARGET_ORIGIN 不在 ALLOWED_HOSTS 白名单中，服务未启动:', {
    TARGET_ORIGIN,
    ALLOWED_HOSTS,
  });
  process.exit(1);
}

// 单接口转发：/api/*
app.use(
  '/api',
  createProxyMiddleware({
    target: TARGET_ORIGIN,
    changeOrigin: true,
    secure: false,
    // 去掉前缀，将 /api/v1/... 代理为 http://backend/v1/...
    pathRewrite: { '^/api': '' },
    logLevel: process.env.NODE_ENV === 'development' ? 'debug' : 'warn',
    onProxyReq: (proxyReq, req) => {
      // 处理 JSON 请求体转发
      if (
        req.body && Object.keys(req.body).length &&
        proxyReq.getHeader('Content-Type')?.includes('application/json')
      ) {
        const bodyData = JSON.stringify(req.body);
        proxyReq.setHeader('Content-Length', Buffer.byteLength(bodyData));
        proxyReq.write(bodyData);
      }
    },
  })
);

app.listen(PORT, () => {
  console.log(`[Proxy] 启动成功: http://localhost:${PORT} -> ${TARGET_ORIGIN}`);
  console.log(`[Proxy] 白名单: ${ALLOWED_HOSTS.join(', ')}`);
});
