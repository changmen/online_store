package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.AddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {
    void insertAddress(AddressEntity address);
    
    AddressEntity findById(Long id);
    
    List<AddressEntity> findByUserId(Long userId);
    
    void updateAddress(AddressEntity address);
    
    void deleteAddress(Long id);
    
    AddressEntity findDefaultAddress(Long userId);
    
    void clearDefaultAddress(Long userId);
    
    void setDefaultAddress(@Param("id") Long id, @Param("userId") Long userId);
} 