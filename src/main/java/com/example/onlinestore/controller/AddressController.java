package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.bean.Member;
import com.example.onlinestore.dto.AddressRequest;
import com.example.onlinestore.dto.AddressResponse;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    @Autowired
    private MemberService memberService;

    @PostMapping
    public Response<AddressResponse> addAddress(@Valid @RequestBody AddressRequest request) {
        Member member = memberService.getLoginMember();
        request.setMemberId(member.getId());
        Address address = memberService.addAddress(request);
        return Response.success(AddressResponse.of(address));
    }

    @PutMapping("/{id}")
    public Response<Void> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {
        Member member = memberService.getLoginMember();
        request.setMemberId(member.getId());
        memberService.updateAddress(id, request);
        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteAddress(
            @PathVariable Long id,
            @RequestParam Long memberId) {
        memberService.deleteAddress(id, memberId);
        return Response.success();
    }

    @GetMapping("/member")
    public Response<List<AddressResponse>> getAddressesByMemberId() {
        Member member = memberService.getLoginMember();
        List<Address> addresses = memberService.getAddressesByMemberId(member.getId());
        if (addresses.isEmpty()) {
            return Response.success(Collections.emptyList());
        }
        List<AddressResponse> addressResponses = addresses.stream()
                .map(AddressResponse::of)
                .collect(Collectors.toList());
        return Response.success(addressResponses);
    }

    @GetMapping("/member/default")
    public Response<AddressResponse> getDefaultAddress() {
        Member member = memberService.getLoginMember();
        Address defaultAddress = memberService.getDefaultAddress(member.getId());
        return Response.success(AddressResponse.of(defaultAddress));
    }

    @PutMapping("/{id}/default")
    public Response<Void> setDefaultAddress(
            @PathVariable Long id) {
        Member member = memberService.getLoginMember();
        memberService.setDefaultAddress(id, member.getId());
        return Response.success();
    }
} 