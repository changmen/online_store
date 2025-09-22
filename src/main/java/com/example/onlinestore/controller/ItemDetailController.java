package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.ItemDetail;
import com.example.onlinestore.bean.Member;
import com.example.onlinestore.dto.ItemDetailResponse;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.dto.converter.ItemDetailConverter;
import com.example.onlinestore.service.ItemAccessLogService;
import com.example.onlinestore.service.ItemDetailService;
import com.example.onlinestore.service.MemberService;
import com.example.onlinestore.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
public class ItemDetailController {
    @Autowired
    private ItemDetailService itemDetailService;

    @Autowired
    private ItemDetailConverter itemDetailConverter;

    @Autowired
    private ItemAccessLogService itemAccessLogService;

    @Autowired
    private MemberService memberService;

    @Value("${async-record-access-log:true}")
    private boolean asyncRecordAccessLog;

    @GetMapping("/{itemId}/detail")
    public Response<ItemDetailResponse> getItemDetail(@PathVariable("itemId") Long itemId, HttpServletRequest request) {
        ItemDetail detail = itemDetailService.getItemDetail(itemId);
        // 记录访问日志
        String ip = WebUtils.getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");
        String sessionId = request.getSession(false) != null ? request.getSession(false).getId() : "";
        Member member = memberService.getLoginMember();
        Item item = detail.getItem();

        String memberId = member != null ? String.valueOf(member.getId()) : "";
        String memberName = member != null ? member.getBaseInfo().getName() : "";

        if (asyncRecordAccessLog) {
            itemAccessLogService.asyncRecordAccessLog(itemId, item.getName(), memberId, memberName, ip, userAgent, referer, sessionId);
        } else {
            itemAccessLogService.recordAccess(itemId, item.getName(), memberId, memberName, ip, userAgent, referer, sessionId);
        }

        return Response.success(itemDetailConverter.convert(detail));
    }
} 