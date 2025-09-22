package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.dto.AttributeResponse;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.dto.UpdateAttributeRequest;
import com.example.onlinestore.service.AttributeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attributes")
public class AttributeController {
    @Autowired
    private  AttributeService attributeService;

    @PostMapping("")
    public Response<AttributeResponse> addAttribute(@Valid @RequestBody CreateAttributeRequest request) {
        Attribute attribute = attributeService.createAttribute(request);
        return Response.success(AttributeResponse.of(attribute));
    }

    @GetMapping("/{attributeId}")
    public Response<AttributeResponse> getAttribute(@PathVariable("attributeId") Long attributeId) {
        Attribute attribute = attributeService.getAttributeById(attributeId);
        return Response.success(AttributeResponse.of(attribute));
    }


    @PutMapping("/{attributeId}")
    public Response<Void> updateAttribute(@PathVariable("attributeId") Long attributeId,
                                                      @Valid @RequestBody UpdateAttributeRequest request) {
        attributeService.updateAttribute(attributeId,request);
        return Response.success();
    }
}
