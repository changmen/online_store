package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AddressService集成测试
 * 注意：此测试需要数据库环境，仅作为示例
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional // 每个测试后回滚数据库事务
class AddressServiceIntegrationTest {

    @Autowired
    private AddressService addressService;

    @Test
    void fullAddressLifecycle_IntegrationTest() {
        // 这个测试演示了完整的地址生命周期
        // 注意：在实际运行中需要配置测试数据库
        
        // Given - 准备测试数据
        Address address = new Address();
        address.setMemberId(1L); // 假设用户ID为1的用户存在
        address.setReceiverName("集成测试用户");
        address.setReceiverPhone("13912345678");
        address.setProvince("江苏省");
        address.setCity("南京市");
        address.setDistrict("玄武区");
        address.setDetailAddress("集成测试地址123号");
        address.setDefault(false);

        // 测试1：创建地址
        Address createdAddress = addressService.createAddress(address);
        assertNotNull(createdAddress);
        assertNotNull(createdAddress.getId());
        assertEquals(address.getReceiverName(), createdAddress.getReceiverName());

        // 测试2：根据ID获取地址
        Address fetchedAddress = addressService.getAddressById(createdAddress.getId());
        assertNotNull(fetchedAddress);
        assertEquals(createdAddress.getId(), fetchedAddress.getId());
        assertEquals(createdAddress.getReceiverName(), fetchedAddress.getReceiverName());

        // 测试3：更新地址
        fetchedAddress.setReceiverName("更新后的用户名");
        fetchedAddress.setDefault(true);
        Address updatedAddress = addressService.updateAddress(fetchedAddress);
        assertEquals("更新后的用户名", updatedAddress.getReceiverName());
        assertTrue(updatedAddress.isDefault());

        // 测试4：获取用户的所有地址
        List<Address> userAddresses = addressService.getAddressesByMemberId(1L);
        assertNotNull(userAddresses);
        assertTrue(userAddresses.size() >= 1);

        // 测试5：获取默认地址
        Address defaultAddress = addressService.getDefaultAddress(1L);
        assertNotNull(defaultAddress);
        assertTrue(defaultAddress.isDefault());
        assertEquals(updatedAddress.getId(), defaultAddress.getId());

        // 测试6：删除地址
        addressService.deleteAddress(createdAddress.getId());
        
        // 验证删除成功
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.getAddressById(createdAddress.getId()));
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void multipleAddresses_DefaultAddressManagement() {
        // 测试多个地址的默认地址管理
        // 注意：需要数据库环境
        
        Long memberId = 1L;
        
        // 创建第一个地址（默认地址）
        Address address1 = createTestAddress(memberId, "地址1", true);
        Address created1 = addressService.createAddress(address1);
        
        // 创建第二个地址（非默认）
        Address address2 = createTestAddress(memberId, "地址2", false);
        Address created2 = addressService.createAddress(address2);
        
        // 验证只有第一个地址是默认的
        Address defaultAddr = addressService.getDefaultAddress(memberId);
        assertEquals(created1.getId(), defaultAddr.getId());
        assertTrue(defaultAddr.isDefault());
        
        // 设置第二个地址为默认
        addressService.setDefaultAddress(created2.getId(), memberId);
        
        // 验证现在第二个地址是默认的
        Address newDefaultAddr = addressService.getDefaultAddress(memberId);
        assertEquals(created2.getId(), newDefaultAddr.getId());
        assertTrue(newDefaultAddr.isDefault());
        
        // 清理数据
        addressService.deleteAddress(created1.getId());
        addressService.deleteAddress(created2.getId());
    }

    private Address createTestAddress(Long memberId, String receiverName, boolean isDefault) {
        Address address = new Address();
        address.setMemberId(memberId);
        address.setReceiverName(receiverName);
        address.setReceiverPhone("13812345678");
        address.setProvince("测试省");
        address.setCity("测试市");
        address.setDistrict("测试区");
        address.setDetailAddress("测试详细地址");
        address.setDefault(isDefault);
        return address;
    }
}