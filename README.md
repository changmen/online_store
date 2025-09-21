# 🛒 Online Store - 在线商店系统

一个基于 Spring Boot 和微服务架构的现代化在线商店系统，提供完整的电商平台解决方案。

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.2-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)

## 📋 项目简介

本项目是一个功能完善的在线商店系统，采用现代化的微服务架构设计，具备以下核心功能：

### 🚀 核心功能

- **👤 用户管理**: 用户注册、登录、个人信息管理
- **🛍 商品管理**: 商品信息、分类、品牌、属性管理
- **📦 库存管理**: SKU管理、库存跟踪
- **🔐 安全认证**: JWT令牌认证、Spring Security集成
- **📊 访问日志**: 商品访问记录和统计
- **🌐 国际化**: 支持多语言（中文/英文）
- **☁️ 云存储**: 阿里云OSS文件存储集成

### 🏗 架构特点

- **微服务架构**: 基于Spring Cloud构建
- **服务发现**: Nacos服务注册与发现
- **配置中心**: Nacos动态配置管理
- **数据持久化**: MyBatis + MySQL
- **缓存系统**: Redis分布式缓存
- **容器化部署**: Docker Compose支持

## 🛠 技术栈

### 后端框架
- **Java 17** - 编程语言
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Cloud Alibaba 2022.0.0.0** - 阿里云微服务

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0** - 内存数据库
- **MyBatis 3.0.3** - ORM框架
- **PageHelper 2.1.0** - 分页插件

### 服务治理
- **Nacos 2.2.0** - 服务注册与配置中心
- **Jedis 5.2.0** - Redis客户端

### 安全认证
- **JWT 0.11.5** - 令牌认证
- **Spring Security** - 权限控制

### 工具库
- **Lombok 1.18.36** - 代码简化
- **Apache Commons Lang3 3.17.0** - 工具类库
- **Jackson** - JSON处理
- **Aliyun OSS 3.18.1** - 对象存储

## 📁 项目结构

```
online_store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java          # 应用启动类
│   ├── bean/                                # 业务对象
│   │   ├── Member.java                      # 用户信息
│   │   ├── Item.java                        # 商品信息
│   │   ├── Category.java                    # 商品分类
│   │   ├── Brand.java                       # 品牌信息
│   │   └── ...
│   ├── controller/                          # 控制层
│   │   ├── MemberController.java            # 用户管理API
│   │   ├── ItemController.java              # 商品管理API
│   │   ├── CategoryController.java          # 分类管理API
│   │   └── ...
│   ├── service/                             # 业务层
│   │   ├── MemberService.java               # 用户业务逻辑
│   │   ├── ItemService.java                 # 商品业务逻辑
│   │   └── ...
│   ├── mapper/                              # 数据访问层
│   │   ├── MemberMapper.java                # 用户数据访问
│   │   ├── ItemMapper.java                  # 商品数据访问
│   │   └── ...
│   ├── entity/                              # 数据实体
│   │   ├── MemberEntity.java                # 用户实体
│   │   ├── ItemEntity.java                  # 商品实体
│   │   └── ...
│   ├── dto/                                 # 数据传输对象
│   │   ├── LoginRequest.java                # 登录请求
│   │   ├── ItemResponse.java                # 商品响应
│   │   └── ...
│   ├── config/                              # 配置类
│   │   ├── SecurityConfig.java              # 安全配置
│   │   ├── RedisConfig.java                 # Redis配置
│   │   └── ...
│   ├── security/                            # 安全模块
│   │   ├── JwtAuthenticationFilter.java     # JWT过滤器
│   │   └── JwtTokenUtil.java                # JWT工具类
│   └── utils/                               # 工具类
│       ├── CommonUtils.java                 # 通用工具
│       └── ...
├── src/main/resources/
│   ├── mapper/                              # MyBatis映射文件
│   ├── sql/                                 # 数据库初始化脚本
│   ├── i18n/                                # 国际化资源文件
│   ├── application.yaml                     # 应用配置
│   └── bootstrap.yaml                       # 引导配置
├── scripts/                                 # 脚本文件
├── docker-compose.yaml                      # Docker编排文件
└── pom.xml                                  # Maven配置
``` 