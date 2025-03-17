package com.example.onlinestore.service;

import com.example.onlinestore.entity.AddressEntity;
import java.util.List;

public interface AddressService {
    Long addAddress(AddressEntity address);
    AddressEntity getAddress(Long id);
    List<AddressEntity> getUserAddresses(Long userId);
    void updateAddress(AddressEntity address);
    void deleteAddress(Long id);
    void setDefaultAddress(Long id, Long userId);
} 