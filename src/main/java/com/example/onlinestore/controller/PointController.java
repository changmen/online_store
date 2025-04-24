package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.bean.PointRecord;
import com.example.onlinestore.bean.PointRule;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.enums.PointRuleStatus;
import com.example.onlinestore.service.MemberService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/rules")
    public Response<PointRule> createRule(
            @RequestParam @NotNull(message = "规则名称不能为空") String name,
            @RequestParam @NotNull(message = "规则描述不能为空") String description,
            @RequestParam @NotNull(message = "积分数量不能为空") @DecimalMin(value = "1", message = "积分数量必须大于0") BigDecimal points) {
        PointRule rule = memberService.createRule(name, description, points);
        return Response.success(rule);
    }

    @PutMapping("/rules/{ruleId}/status")
    public Response<Void> updateRuleStatus(
            @PathVariable @NotNull @Min(value = 1, message = "规则ID必须大于0") Long ruleId,
            @RequestParam @NotNull PointRuleStatus status) {
        memberService.updateRuleStatus(ruleId, status);
        return Response.success();
    }

    @GetMapping("/members/balance")
    public Response<BigDecimal> getMemberPointBalance() {
        Member member = memberService.getLoginMember();
        BigDecimal balance = memberService.getMemberPointBalance(member.getId());
        return Response.success(balance);
    }

    @GetMapping("/members/records")
    public Response<List<PointRecord>> getMemberPointRecords() {
        Member member = memberService.getLoginMember();
        List<PointRecord> records = memberService.getMemberPointRecords(member.getId());
        return Response.success(records);
    }
} 