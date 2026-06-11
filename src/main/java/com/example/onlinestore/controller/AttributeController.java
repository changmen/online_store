package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.dto.AttributeResponse;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.dto.UpdateAttributeRequest;
import com.example.onlinestore.service.AttributeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attributes")
@Validated
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService attributeService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<AttributeResponse> addAttribute(@Valid @RequestBody CreateAttributeRequest request) {
        Attribute attribute = attributeService.createAttribute(request);
        return Response.success(AttributeResponse.of(attribute));
    }

    @GetMapping("/{attributeId}")
    public Response<AttributeResponse> getAttribute(@Positive @PathVariable("attributeId") Long attributeId) {
        Attribute attribute = attributeService.getAttributeById(attributeId);
        return Response.success(AttributeResponse.of(attribute));
    }


    @PutMapping("/{attributeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<Void> updateAttribute(@Positive @PathVariable("attributeId") Long attributeId,
                                                      @Valid @RequestBody UpdateAttributeRequest request) {
        attributeService.updateAttribute(attributeId,request);
        return Response.success();
    }
}
