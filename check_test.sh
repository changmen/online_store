#!/bin/bash

echo "检查AttributeServiceImplTest.java的结构和语法..."

# 检查文件是否存在
TEST_FILE="/data/workspace/online_store/src/test/java/com/example/onlinestore/service/impl/AttributeServiceImplTest.java"

if [ -f "$TEST_FILE" ]; then
    echo "✓ 测试文件存在"
    
    # 检查测试类的基本结构
    echo -e "\n检查测试类结构:"
    
    # 检查是否有@ExtendWith注解
    if grep -q "@ExtendWith(MockitoExtension.class)" "$TEST_FILE"; then
        echo "✓ 包含MockitoExtension注解"
    else
        echo "✗ 缺少MockitoExtension注解"
    fi
    
    # 检查是否有Mock注解
    if grep -q "@Mock" "$TEST_FILE"; then
        echo "✓ 包含Mock注解"
    else
        echo "✗ 缺少Mock注解"
    fi
    
    # 检查是否有InjectMocks注解
    if grep -q "@InjectMocks" "$TEST_FILE"; then
        echo "✓ 包含InjectMocks注解"
    else
        echo "✗ 缺少InjectMocks注解"
    fi
    
    # 统计测试方法数量
    TEST_METHOD_COUNT=$(grep -c "@Test" "$TEST_FILE")
    echo "✓ 包含 $TEST_METHOD_COUNT 个测试方法"
    
    # 列出所有测试方法
    echo -e "\n测试方法列表:"
    grep -A 1 "@Test" "$TEST_FILE" | grep "void test" | sed 's/.*void /- /' | sed 's/(.*//'
    
    # 检查导入语句
    echo -e "\n检查关键导入:"
    if grep -q "import org.junit.jupiter.api.Test" "$TEST_FILE"; then
        echo "✓ JUnit 5导入正确"
    fi
    
    if grep -q "import org.mockito" "$TEST_FILE"; then
        echo "✓ Mockito导入正确"
    fi
    
    if grep -q "import static org.junit.jupiter.api.Assertions" "$TEST_FILE"; then
        echo "✓ JUnit断言导入正确"
    fi
    
    echo -e "\n文件行数统计:"
    wc -l "$TEST_FILE"
    
    echo -e "\n✅ AttributeServiceImplTest.java 检查完成"
else
    echo "✗ 测试文件不存在"
fi