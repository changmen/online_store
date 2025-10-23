package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AddressServiceImpl 单元测试类
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("地址服务实现类测试")
class AddressServiceImplTest {

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    private AddressEntity mockEntity;
    private Address mockAddress;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        mockEntity = createMockEntity();
        mockAddress = createMockAddress();
    }

    @Test
    @DisplayName("根据ID查询地址 - 成功")
    void getById_Success() {
        // given
        Long id = 1L;
        when(addressMapper.selectById(id)).thenReturn(mockEntity);

        // when
        Address result = addressService.getById(id);

        // then
        assertNotNull(result);
        assertEquals(mockEntity.getId(), result.getId());
        assertEquals(mockEntity.getReceiverName(), result.getReceiverName());
        assertEquals(mockEntity.getReceiverPhone(), result.getReceiverPhone());
        verify(addressMapper, times(1)).selectById(id);
    }

    @Test
    @DisplayName("根据ID查询地址 - ID为空")
    void getById_NullId() {
        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.getById(null));
        assertEquals(ErrorCode.INVALID_PARAMETER, exception.getErrorCode());
        assertEquals("地址ID不能为空", exception.getMessage());
        verify(addressMapper, never()).selectById(any());
    }

    @Test
    @DisplayName("根据ID查询地址 - 地址不存在")
    void getById_NotFound() {
        // given
        Long id = 999L;
        when(addressMapper.selectById(id)).thenReturn(null);

        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.getById(id));
        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals("地址信息不存在", exception.getMessage());
        verify(addressMapper, times(1)).selectById(id);
    }

    @Test
    @DisplayName("根据用户ID查询地址列表 - 成功")
    void getByMemberId_Success() {
        // given
        Long memberId = 1L;
        List<AddressEntity> entities = Arrays.asList(mockEntity, createMockEntity());
        when(addressMapper.selectByMemberId(memberId)).thenReturn(entities);

        // when
        List<Address> result = addressService.getByMemberId(memberId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockEntity.getId(), result.get(0).getId());
        verify(addressMapper, times(1)).selectByMemberId(memberId);
    }

    @Test
    @DisplayName("根据用户ID查询地址列表 - 用户ID为空")
    void getByMemberId_NullMemberId() {
        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.getByMemberId(null));
        assertEquals(ErrorCode.INVALID_PARAMETER, exception.getErrorCode());
        assertEquals("用户ID不能为空", exception.getMessage());
        verify(addressMapper, never()).selectByMemberId(any());
    }

    @Test
    @DisplayName("查询默认地址 - 成功")
    void getDefaultByMemberId_Success() {
        // given
        Long memberId = 1L;
        when(addressMapper.selectDefaultByMemberId(memberId)).thenReturn(mockEntity);

        // when
        Address result = addressService.getDefaultByMemberId(memberId);

        // then
        assertNotNull(result);
        assertEquals(mockEntity.getId(), result.getId());
        verify(addressMapper, times(1)).selectDefaultByMemberId(memberId);
    }

    @Test
    @DisplayName("查询默认地址 - 无默认地址")
    void getDefaultByMemberId_NoDefault() {
        // given
        Long memberId = 1L;
        when(addressMapper.selectDefaultByMemberId(memberId)).thenReturn(null);

        // when
        Address result = addressService.getDefaultByMemberId(memberId);

        // then
        assertNull(result);
        verify(addressMapper, times(1)).selectDefaultByMemberId(memberId);
    }

    @Test
    @DisplayName("创建地址 - 成功")
    void create_Success() {
        // given
        mockAddress.setIsDefault(false);
        when(addressMapper.countByMemberId(mockAddress.getMemberId())).thenReturn(5);
        when(addressMapper.insert(any(AddressEntity.class))).thenReturn(1);

        // when
        Address result = addressService.create(mockAddress);

        // then
        assertNotNull(result);
        verify(addressMapper, times(1)).countByMemberId(mockAddress.getMemberId());
        verify(addressMapper, times(1)).insert(any(AddressEntity.class));
        verify(addressMapper, never()).clearDefaultByMemberId(any());
    }

    @Test
    @DisplayName("创建地址 - 设置为默认地址")
    void create_SetAsDefault() {
        // given
        mockAddress.setIsDefault(true);
        when(addressMapper.countByMemberId(mockAddress.getMemberId())).thenReturn(5);
        when(addressMapper.clearDefaultByMemberId(mockAddress.getMemberId())).thenReturn(1);
        when(addressMapper.insert(any(AddressEntity.class))).thenReturn(1);

        // when
        Address result = addressService.create(mockAddress);

        // then
        assertNotNull(result);
        verify(addressMapper, times(1)).clearDefaultByMemberId(mockAddress.getMemberId());
        verify(addressMapper, times(1)).insert(any(AddressEntity.class));
    }

    @Test
    @DisplayName("创建地址 - 地址数量超限")
    void create_ExceedLimit() {
        // given
        when(addressMapper.countByMemberId(mockAddress.getMemberId())).thenReturn(20);

        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.create(mockAddress));
        assertEquals(ErrorCode.OPERATION_NOT_ALLOWED, exception.getErrorCode());
        assertTrue(exception.getMessage().contains("地址数量已达上限"));
        verify(addressMapper, times(1)).countByMemberId(mockAddress.getMemberId());
        verify(addressMapper, never()).insert(any());
    }

    @Test
    @DisplayName("创建地址 - 地址信息为空")
    void create_NullAddress() {
        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.create(null));
        assertEquals(ErrorCode.INVALID_PARAMETER, exception.getErrorCode());
        assertEquals("地址信息不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("创建地址 - 用户ID为空")
    void create_NullMemberId() {
        // given
        mockAddress.setMemberId(null);

        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.create(mockAddress));
        assertEquals(ErrorCode.INVALID_PARAMETER, exception.getErrorCode());
        assertEquals("用户ID不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("更新地址 - 成功")
    void update_Success() {
        // given
        mockAddress.setId(1L);
        mockAddress.setIsDefault(false);
        when(addressMapper.selectByIdAndMemberId(mockAddress.getId(), mockAddress.getMemberId()))
            .thenReturn(mockEntity);
        when(addressMapper.update(any(AddressEntity.class))).thenReturn(1);
        when(addressMapper.selectById(mockAddress.getId())).thenReturn(mockEntity);

        // when
        Address result = addressService.update(mockAddress);

        // then
        assertNotNull(result);
        verify(addressMapper, times(1))
            .selectByIdAndMemberId(mockAddress.getId(), mockAddress.getMemberId());
        verify(addressMapper, times(1)).update(any(AddressEntity.class));
        verify(addressMapper, never()).clearDefaultByMemberId(any());
    }

    @Test
    @DisplayName("更新地址 - 设置为默认地址")
    void update_SetAsDefault() {
        // given
        mockAddress.setId(1L);
        mockAddress.setIsDefault(true);
        when(addressMapper.selectByIdAndMemberId(mockAddress.getId(), mockAddress.getMemberId()))
            .thenReturn(mockEntity);
        when(addressMapper.clearDefaultByMemberId(mockAddress.getMemberId())).thenReturn(1);
        when(addressMapper.update(any(AddressEntity.class))).thenReturn(1);
        when(addressMapper.selectById(mockAddress.getId())).thenReturn(mockEntity);

        // when
        Address result = addressService.update(mockAddress);

        // then
        assertNotNull(result);
        verify(addressMapper, times(1)).clearDefaultByMemberId(mockAddress.getMemberId());
        verify(addressMapper, times(1)).update(any(AddressEntity.class));
    }

    @Test
    @DisplayName("更新地址 - 地址ID为空")
    void update_NullId() {
        // given
        mockAddress.setId(null);

        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.update(mockAddress));
        assertEquals(ErrorCode.INVALID_PARAMETER, exception.getErrorCode());
        assertEquals("地址ID不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("更新地址 - 无权限")
    void update_NoPermission() {
        // given
        mockAddress.setId(1L);
        when(addressMapper.selectByIdAndMemberId(mockAddress.getId(), mockAddress.getMemberId()))
            .thenReturn(null);

        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.update(mockAddress));
        assertEquals(ErrorCode.OPERATION_NOT_ALLOWED, exception.getErrorCode());
        assertEquals("无权限修改该地址", exception.getMessage());
    }

    @Test
    @DisplayName("删除地址 - 成功")
    void delete_Success() {
        // given
        Long id = 1L;
        Long memberId = 1L;
        when(addressMapper.selectByIdAndMemberId(id, memberId)).thenReturn(mockEntity);
        when(addressMapper.deleteById(id)).thenReturn(1);

        // when
        assertDoesNotThrow(() -> addressService.delete(id, memberId));

        // then
        verify(addressMapper, times(1)).selectByIdAndMemberId(id, memberId);
        verify(addressMapper, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("删除地址 - 参数为空")
    void delete_NullParams() {
        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.delete(null, 1L));
        assertEquals(ErrorCode.INVALID_PARAMETER, exception.getErrorCode());
        assertEquals("地址ID和用户ID不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("删除地址 - 无权限")
    void delete_NoPermission() {
        // given
        Long id = 1L;
        Long memberId = 1L;
        when(addressMapper.selectByIdAndMemberId(id, memberId)).thenReturn(null);

        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.delete(id, memberId));
        assertEquals(ErrorCode.OPERATION_NOT_ALLOWED, exception.getErrorCode());
        assertEquals("无权限删除该地址", exception.getMessage());
    }

    @Test
    @DisplayName("设置默认地址 - 成功")
    void setDefault_Success() {
        // given
        Long id = 1L;
        Long memberId = 1L;
        when(addressMapper.selectByIdAndMemberId(id, memberId)).thenReturn(mockEntity);
        when(addressMapper.clearDefaultByMemberId(memberId)).thenReturn(1);
        when(addressMapper.setDefaultById(id)).thenReturn(1);

        // when
        assertDoesNotThrow(() -> addressService.setDefault(id, memberId));

        // then
        verify(addressMapper, times(1)).clearDefaultByMemberId(memberId);
        verify(addressMapper, times(1)).setDefaultById(id);
    }

    @Test
    @DisplayName("设置默认地址 - 参数为空")
    void setDefault_NullParams() {
        // when & then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.setDefault(null, 1L));
        assertEquals(ErrorCode.INVALID_PARAMETER, exception.getErrorCode());
        assertEquals("地址ID和用户ID不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("验证地址所有权 - 属于用户")
    void validateOwnership_Valid() {
        // given
        Long id = 1L;
        Long memberId = 1L;
        when(addressMapper.selectByIdAndMemberId(id, memberId)).thenReturn(mockEntity);

        // when
        boolean result = addressService.validateOwnership(id, memberId);

        // then
        assertTrue(result);
        verify(addressMapper, times(1)).selectByIdAndMemberId(id, memberId);
    }

    @Test
    @DisplayName("验证地址所有权 - 不属于用户")
    void validateOwnership_Invalid() {
        // given
        Long id = 1L;
        Long memberId = 1L;
        when(addressMapper.selectByIdAndMemberId(id, memberId)).thenReturn(null);

        // when
        boolean result = addressService.validateOwnership(id, memberId);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("验证地址所有权 - 参数为空")
    void validateOwnership_NullParams() {
        // when & then
        assertFalse(addressService.validateOwnership(null, 1L));
        assertFalse(addressService.validateOwnership(1L, null));
    }

    @Test
    @DisplayName("检查是否可以添加更多地址 - 可以添加")
    void canAddMore_True() {
        // given
        Long memberId = 1L;
        when(addressMapper.countByMemberId(memberId)).thenReturn(10);

        // when
        boolean result = addressService.canAddMore(memberId);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("检查是否可以添加更多地址 - 不能添加")
    void canAddMore_False() {
        // given
        Long memberId = 1L;
        when(addressMapper.countByMemberId(memberId)).thenReturn(20);

        // when
        boolean result = addressService.canAddMore(memberId);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("检查是否可以添加更多地址 - 用户ID为空")
    void canAddMore_NullMemberId() {
        // when & then
        assertFalse(addressService.canAddMore(null));
    }

    /**
     * 创建模拟的地址实体对象
     */
    private AddressEntity createMockEntity() {
        AddressEntity entity = new AddressEntity();
        entity.setId(1L);
        entity.setMemberId(1L);
        entity.setReceiverName("张三");
        entity.setReceiverPhone("13812345678");
        entity.setProvinceCode("110000");
        entity.setProvinceName("北京市");
        entity.setCityCode("110100");
        entity.setCityName("北京市");
        entity.setDistrictCode("110101");
        entity.setDistrictName("东城区");
        entity.setDetailAddress("王府井大街1号");
        entity.setPostalCode("100006");
        entity.setIsDefault(1);
        entity.setTag("家");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        return entity;
    }

    /**
     * 创建模拟的地址Bean对象
     */
    private Address createMockAddress() {
        Address address = new Address();
        address.setMemberId(1L);
        address.setReceiverName("张三");
        address.setReceiverPhone("13812345678");
        address.setProvinceCode("110000");
        address.setProvinceName("北京市");
        address.setCityCode("110100");
        address.setCityName("北京市");
        address.setDistrictCode("110101");
        address.setDistrictName("东城区");
        address.setDetailAddress("王府井大街1号");
        address.setPostalCode("100006");
        address.setIsDefault(true);
        address.setTag("家");
        return address;
    }
}