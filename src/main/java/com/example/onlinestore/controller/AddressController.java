package com.example.onlinestore.controller;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public Response<Long> createAddress(@RequestBody AddressEntity address, 
                                      @RequestHeader("X-User-Id") String userId) {
        address.setUserId(Long.parseLong(userId));
        Long addressId = addressService.addAddress(address);
        return Response.success(addressId);
    }

    @GetMapping("/{id}")
    public Response<AddressEntity> getAddress(@PathVariable("id") Long id) {
        AddressEntity address = addressService.getAddress(id);
        return Response.success(address);
    }

    @GetMapping
    public Response<List<AddressEntity>> listAddresses(@RequestHeader("X-User-Id") String userId) {
        List<AddressEntity> addresses = addressService.getUserAddresses(Long.parseLong(userId));
        return Response.success(addresses);
    }

    @PutMapping("/{id}")
    public Response<Void> updateAddress(@PathVariable("id") Long id, 
                                      @RequestBody AddressEntity address) {
        address.setId(id);
        addressService.updateAddress(address);
        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteAddress(@PathVariable("id") Long id) {
        addressService.deleteAddress(id);
        return Response.success();
    }

    @PostMapping("/{id}/default")
    public Response<Void> setDefaultAddress(@PathVariable("id") Long id,
                                          @RequestHeader("X-User-Id") String userId) {
        addressService.setDefaultAddress(id, Long.parseLong(userId));
        return Response.success();
    }
} 