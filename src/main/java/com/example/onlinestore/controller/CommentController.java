package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Comment;
import com.example.onlinestore.dto.CommentStatistics;
import com.example.onlinestore.dto.PageResponse;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Response<Long> createComment(@RequestBody Comment comment,
                                        @RequestHeader("X-User-Id") String userId) {
        comment.setUserId(Long.parseLong(userId));
        Long commentId = commentService.addComment(comment);
        return Response.success(commentId);
    }

    @GetMapping("/{id}")
    public Response<Comment> getComment(@PathVariable("id") Long id) {
        Comment comment = commentService.getComment(id);
        if (comment != null) {
            return Response.success(comment);
        }
        return Response.fail("Comment not found");
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteComment(@PathVariable("id") Long id) {
        boolean success = commentService.deleteComment(id);
        if (success) {
            return Response.success();
        }
        return Response.fail("Failed to delete comment");
    }

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

    @GetMapping("/items/{itemId}/statistics")
    public Response<CommentStatistics> getItemCommentStatistics(
            @PathVariable("itemId") Long itemId) {
        CommentStatistics statistics = commentService.getItemCommentStatistics(itemId);
        return Response.success(statistics);
    }
}
