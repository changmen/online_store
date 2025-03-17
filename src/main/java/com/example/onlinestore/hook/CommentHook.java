package com.example.onlinestore.hook;

import com.example.onlinestore.bean.Comment;

public interface CommentHook {
    /**
     * 获取Hook执行的优先级，数字越小优先级越高
     */
    int getOrder();

    /**
     * 获取Hook点
     */
    CommentHookPoint getPoint();

    /**
     * 执行Hook
     * @param comment 评论对象
     * @return 是否继续执行后续Hook
     */
    boolean execute(Comment comment);
} 