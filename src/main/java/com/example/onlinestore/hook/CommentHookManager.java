package com.example.onlinestore.hook;

import com.example.onlinestore.bean.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CommentHookManager {
    private static final Logger logger = LoggerFactory.getLogger(CommentHookManager.class);

    // 使用线程安全的集合存储Hook
    private final Map<CommentHookPoint, List<CommentHook>> hooks = new ConcurrentHashMap<>();

    public CommentHookManager() {
        // 初始化所有Hook点的列表
        for (CommentHookPoint point : CommentHookPoint.values()) {
            hooks.put(point, new CopyOnWriteArrayList<>());
        }
    }

    /**
     * 注册Hook
     */
    public void registerHook(CommentHook hook) {
        if (hook == null) {
            return;
        }

        List<CommentHook> pointHooks = hooks.get(hook.getPoint());
        pointHooks.add(hook);
        
        // 按优先级排序
        pointHooks.sort(Comparator.comparingInt(CommentHook::getOrder));
        
        logger.info("Registered comment hook: {} for point: {}", 
                hook.getClass().getSimpleName(), hook.getPoint());
    }

    /**
     * 执行指定Hook点的所有Hook
     */
    public boolean executeHooks(CommentHookPoint point, Comment comment) {
        List<CommentHook> pointHooks = hooks.get(point);
        if (pointHooks == null || pointHooks.isEmpty()) {
            return true;
        }

        for (CommentHook hook : pointHooks) {
            try {
                logger.debug("Executing comment hook: {} for point: {}", 
                        hook.getClass().getSimpleName(), point);
                
                boolean continueExecution = hook.execute(comment);
                if (!continueExecution) {
                    logger.info("Hook execution chain interrupted by: {}", 
                            hook.getClass().getSimpleName());
                    return false;
                }
            } catch (Exception e) {
                logger.error("Error executing comment hook: {}", 
                        hook.getClass().getSimpleName(), e);
                // 继续执行其他Hook
            }
        }
        return true;
    }
} 