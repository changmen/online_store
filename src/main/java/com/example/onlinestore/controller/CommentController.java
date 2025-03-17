package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Comment;
import com.example.onlinestore.dto.PageResponse;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 创建评论
     */
    @PostMapping
    public Response<Long> createComment( @RequestBody Comment comment,
                                      @RequestHeader("X-User-Id") String userId) {
        try {
            comment.setUserId(Long.parseLong(userId));
            Long commentId = commentService.addComment(comment);
            return Response.success(commentId);
        } catch (IllegalArgumentException e) {
            return Response.fail(e.getMessage());
        }
    }

    /**
     * 获取评论详情
     */
    @GetMapping("/{id}")
    public Response<Comment> getComment(@PathVariable("id") Long id) {
        Comment comment = commentService.getComment(id);
        if (comment != null) {
            return Response.success(comment);
        }
        return Response.fail("Comment not found");
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Response<Void> deleteComment(@PathVariable("id") Long id) {
        boolean success = commentService.deleteComment(id);
        if (success) {
            return Response.success();
        }
        return Response.fail("Failed to delete comment");
    }

    /**
     * 获取商品的评论列表
     */
    @GetMapping("/items/{itemId}")
    public Response<PageResponse<Comment>> listItemComments(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        List<Comment> comments = commentService.getItemComments(itemId, page, size);
        long total = commentService.countItemComments(itemId);
        
        PageResponse<Comment> pageResponse = PageResponse.of(comments, total, page, size);
        return Response.success(pageResponse);
    }

} 