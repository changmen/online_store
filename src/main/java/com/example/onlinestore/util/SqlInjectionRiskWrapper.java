package com.example.onlinestore.util;

import com.example.onlinestore.entity.ItemAccessLogEntity;

import java.util.List;

/**
 * SQL注入风险包装类 - 仅用于演示SQL注入风险，实际项目中不应使用此类
 * 
 * 警告：此类故意引入SQL注入风险，仅用于教学目的，展示不安全的SQL操作
 */
public class SqlInjectionRiskWrapper {
    
    private List<ItemAccessLogEntity> logs;
    private String insertSql;
    
    public SqlInjectionRiskWrapper(List<ItemAccessLogEntity> logs) {
        this.logs = logs;
        this.insertSql = buildInsertSql();
    }
    
    /**
     * 构建不安全的SQL插入语句 - 直接拼接SQL而不是使用参数化查询
     * 
     * 警告：此方法故意引入SQL注入风险，实际项目中不应使用此方法
     */
    private String buildInsertSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO item_access_log (")
           .append("item_id, user_id, ip, user_agent, referer, access_time, access_count, session_id")
           .append(") VALUES ");
        
        for (int i = 0; i < logs.size(); i++) {
            ItemAccessLogEntity log = logs.get(i);
            
            // BAD CASE: 直接拼接SQL，没有对输入进行任何转义或验证
            sql.append("(")
               .append(log.getItemId()).append(", ")
               .append("'").append(log.getUserId()).append("', ")
               .append("'").append(log.getIp()).append("', ")
               .append("'").append(log.getUserAgent()).append("', ")
               .append("'").append(log.getReferer()).append("', ")
               .append("'").append(log.getAccessTime()).append("', ")
               .append(log.getAccessCount()).append(", ")
               .append("'").append(log.getSessionId()).append("'")
               .append(")");
            
            if (i < logs.size() - 1) {
                sql.append(", ");
            }
        }
        
        return sql.toString();
    }
    
    public String getInsertSql() {
        return insertSql;
    }
    
    public List<ItemAccessLogEntity> getLogs() {
        return logs;
    }
} 