package com.example.onlinestore.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CommonUtils 单元测试类
 * 
 * 测试通用工具类的字段更新功能，验证各种边界情况和正常场景
 */
class CommonUtilsTest {

    private AtomicReference<String> testField;
    private Consumer<String> testSetter;
    private boolean setterCalled;

    @BeforeEach
    void setUp() {
        testField = new AtomicReference<>();
        setterCalled = false;
        testSetter = value -> {
            testField.set(value);
            setterCalled = true;
        };
    }

    @Test
    @DisplayName("新值非空且不同 - 应该更新字段并返回true")
    void testUpdateFieldIfChanged_WithDifferentNonNullValue_ShouldUpdateAndReturnTrue() {
        // Given
        String oldValue = "oldValue";
        String newValue = "newValue";
        
        // When
        boolean result = CommonUtils.updateFieldIfChanged(newValue, oldValue, testSetter);
        
        // Then
        assertTrue(result, "当新值与旧值不同时应返回true");
        assertTrue(setterCalled, "setter方法应该被调用");
        assertEquals(newValue, testField.get(), "字段应该被更新为新值");
    }

    @Test
    @DisplayName("新值为null - 应该不更新字段并返回false")
    void testUpdateFieldIfChanged_WithNullNewValue_ShouldNotUpdateAndReturnFalse() {
        // Given
        String oldValue = "oldValue";
        String newValue = null;
        
        // When
        boolean result = CommonUtils.updateFieldIfChanged(newValue, oldValue, testSetter);
        
        // Then
        assertFalse(result, "当新值为null时应返回false");
        assertFalse(setterCalled, "setter方法不应该被调用");
        assertNull(testField.get(), "字段应该保持初始状态");
    }

    @Test
    @DisplayName("新值与旧值相同 - 应该不更新字段并返回false")
    void testUpdateFieldIfChanged_WithSameValue_ShouldNotUpdateAndReturnFalse() {
        // Given
        String oldValue = "sameValue";
        String newValue = "sameValue";
        
        // When
        boolean result = CommonUtils.updateFieldIfChanged(newValue, oldValue, testSetter);
        
        // Then
        assertFalse(result, "当新值与旧值相同时应返回false");
        assertFalse(setterCalled, "setter方法不应该被调用");
        assertNull(testField.get(), "字段应该保持初始状态");
    }

    @Test
    @DisplayName("旧值为null新值非null - 应该更新字段并返回true")
    void testUpdateFieldIfChanged_WithNullOldValueAndNonNullNewValue_ShouldUpdateAndReturnTrue() {
        // Given
        String oldValue = null;
        String newValue = "newValue";
        
        // When
        boolean result = CommonUtils.updateFieldIfChanged(newValue, oldValue, testSetter);
        
        // Then
        assertTrue(result, "当旧值为null新值非null时应返回true");
        assertTrue(setterCalled, "setter方法应该被调用");
        assertEquals(newValue, testField.get(), "字段应该被更新为新值");
    }

    @Test
    @DisplayName("新值和旧值都为null - 应该不更新字段并返回false")
    void testUpdateFieldIfChanged_WithBothNullValues_ShouldNotUpdateAndReturnFalse() {
        // Given
        String oldValue = null;
        String newValue = null;
        
        // When
        boolean result = CommonUtils.updateFieldIfChanged(newValue, oldValue, testSetter);
        
        // Then
        assertFalse(result, "当新值和旧值都为null时应返回false");
        assertFalse(setterCalled, "setter方法不应该被调用");
        assertNull(testField.get(), "字段应该保持初始状态");
    }

    @Test
    @DisplayName("测试Integer类型 - 应该正确处理数字类型")
    void testUpdateFieldIfChanged_WithIntegerType_ShouldWorkCorrectly() {
        // Given
        AtomicReference<Integer> intField = new AtomicReference<>();
        Consumer<Integer> intSetter = intField::set;
        Integer oldValue = 10;
        Integer newValue = 20;
        
        // When
        boolean result = CommonUtils.updateFieldIfChanged(newValue, oldValue, intSetter);
        
        // Then
        assertTrue(result, "不同的Integer值应该触发更新");
        assertEquals(newValue, intField.get(), "Integer字段应该被正确更新");
    }

    @Test
    @DisplayName("测试Integer相同值 - 应该不更新")
    void testUpdateFieldIfChanged_WithSameIntegerValue_ShouldNotUpdate() {
        // Given
        AtomicReference<Integer> intField = new AtomicReference<>();
        Consumer<Integer> intSetter = intField::set;
        Integer oldValue = 42;
        Integer newValue = 42;
        
        // When
        boolean result = CommonUtils.updateFieldIfChanged(newValue, oldValue, intSetter);
        
        // Then
        assertFalse(result, "相同的Integer值不应该触发更新");
        assertNull(intField.get(), "字段应该保持初始状态");
    }

    @Test
    @DisplayName("测试复杂对象 - 应该正确处理自定义对象")
    void testUpdateFieldIfChanged_WithCustomObject_ShouldWorkCorrectly() {
        // Given
        TestObject oldObj = new TestObject("old", 1);
        TestObject newObj = new TestObject("new", 2);
        AtomicReference<TestObject> objField = new AtomicReference<>();
        Consumer<TestObject> objSetter = objField::set;
        
        // When
        boolean result = CommonUtils.updateFieldIfChanged(newObj, oldObj, objSetter);
        
        // Then
        assertTrue(result, "不同的对象应该触发更新");
        assertEquals(newObj, objField.get(), "对象字段应该被正确更新");
    }

    @Test
    @DisplayName("测试相同复杂对象 - 应该不更新")
    void testUpdateFieldIfChanged_WithSameCustomObject_ShouldNotUpdate() {
        // Given
        TestObject oldObj = new TestObject("same", 1);
        TestObject newObj = new TestObject("same", 1);  // 值相同的对象
        AtomicReference<TestObject> objField = new AtomicReference<>();
        Consumer<TestObject> objSetter = objField::set;
        
        // When
        boolean result = CommonUtils.updateFieldIfChanged(newObj, oldObj, objSetter);
        
        // Then
        assertFalse(result, "equals相等的对象不应该触发更新");
        assertNull(objField.get(), "字段应该保持初始状态");
    }

    @Test
    @DisplayName("测试空字符串 - 应该正确处理空字符串")
    void testUpdateFieldIfChanged_WithEmptyString_ShouldWorkCorrectly() {
        // Given
        String oldValue = "notEmpty";
        String newValue = "";  // 空字符串，但不是null
        
        // When
        boolean result = CommonUtils.updateFieldIfChanged(newValue, oldValue, testSetter);
        
        // Then
        assertTrue(result, "空字符串与非空字符串不同，应该触发更新");
        assertTrue(setterCalled, "setter方法应该被调用");
        assertEquals("", testField.get(), "字段应该被更新为空字符串");
    }

    @Test
    @DisplayName("setter抛出异常 - 异常应该向上传播")
    void testUpdateFieldIfChanged_WithSetterException_ShouldPropagateException() {
        // Given
        String oldValue = "old";
        String newValue = "new";
        Consumer<String> failingSetter = value -> {
            throw new RuntimeException("Setter failed");
        };
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CommonUtils.updateFieldIfChanged(newValue, oldValue, failingSetter);
        });
        
        assertEquals("Setter failed", exception.getMessage(), "异常消息应该正确传播");
    }

    /**
     * 测试用的简单对象类
     */
    private static class TestObject {
        private final String name;
        private final int value;

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestObject that = (TestObject) obj;
            return value == that.value && 
                   java.util.Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(name, value);
        }

        @Override
        public String toString() {
            return "TestObject{name='" + name + "', value=" + value + '}';
        }
    }
}