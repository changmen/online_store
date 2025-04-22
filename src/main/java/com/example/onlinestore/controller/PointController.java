package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.bean.PointRecord;
import com.example.onlinestore.bean.PointRule;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.enums.PointRuleStatus;
import com.example.onlinestore.service.MemberService;
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
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal points) {
        PointRule rule = memberService.createRule(name, description, points);
        return Response.success(rule);
    }

    @PutMapping("/rules/{ruleId}/status")
    public Response<Void> updateRuleStatus(
            @PathVariable Long ruleId,
            @RequestParam PointRuleStatus status) {
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

    @PostMapping("/earn")
    public Response<Void> earnPoints(
            @RequestParam Long orderId,
            @RequestParam BigDecimal points,
            @RequestParam String description) {
        Member member = memberService.getLoginMember();
        memberService.earnPoints(member.getId(), orderId, points, description);
        return Response.success();
    }

    @PostMapping("/consume")
    public Response<Void> consumePoints(
            @RequestParam Long orderId,
            @RequestParam BigDecimal points,
            @RequestParam String description) {
        Member member = memberService.getLoginMember();
        memberService.consumePoints(member.getId(), orderId, points, description);
        return Response.success();
    }
} 