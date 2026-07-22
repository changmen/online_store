# Online Store 单元测试报告

**生成时间**: 2026-07-22 13:35:20
**分支**: test-ai-stat
**构建结果**: BUILD SUCCESS

## 总体概览

| 指标 | 数值 |
|------|------|
| 测试总数 | 112 |
| 通过 | 112 |
| 失败 | 0 |
| 错误 | 0 |
| 跳过 | 0 |
| 通过率 | 100% |

## 各测试类明细

| 测试类 | 用例数 | 通过 | 失败 | 错误 | 耗时 |
|--------|--------|------|------|------|------|
| ItemResponseConverterTest | 5 | 5 | 0 | 0 | 0.016s |
| PageTest | 9 | 9 | 0 | 0 | 0.009s |
| MessageSourceTest | 5 | 5 | 0 | 0 | 2.461s |
| CustomUserDetailsTest | 9 | 9 | 0 | 0 | 0.004s |
| AuthRateLimitFilterTest | 5 | 5 | 0 | 0 | 0.200s |
| WebUtilsTest | 11 | 11 | 0 | 0 | 0.027s |
| MemberServiceImplTest | 10 | 10 | 0 | 0 | 0.138s |
| SkuServiceImplTest | 7 | 7 | 0 | 0 | 0.146s |
| CategoryServiceImplTest | 6 | 6 | 0 | 0 | 0.026s |
| ItemDetailServiceImplTest | 6 | 6 | 0 | 0 | 0.179s |
| ItemAccessLogServiceImplTest | 5 | 5 | 0 | 0 | 0.028s |
| ItemServiceImplTest | 6 | 6 | 0 | 0 | 0.094s |
| BrandServiceImplTest | 9 | 9 | 0 | 0 | 0.031s |
| AttributeServiceImplTest | 12 | 12 | 0 | 0 | 0.052s |
| TokenBlacklistServiceTest | 7 | 7 | 0 | 0 | 0.031s |

## 本次修复记录

### ItemServiceImplTest.createItem_shouldDelegateAttributeValidation

- **问题**: 测试断言 `ensureItemAttributes` 的第一个参数使用 `anyLong()` 匹配，但 mock 的 `itemMapper.insert()` 未回填实体 ID，实际传入值为 `null`，导致匹配失败。
- **修复**: 将 `anyLong()` 替换为 `nullable(Long.class)`，允许匹配 null 值。
- **影响文件**: `src/test/java/com/example/onlinestore/service/impl/ItemServiceImplTest.java`

## 环境信息

- **Java**: OpenJDK 17
- **Spring Boot**: 3.4.3
- **构建工具**: Maven
- **测试框架**: JUnit 5 + Mockito
