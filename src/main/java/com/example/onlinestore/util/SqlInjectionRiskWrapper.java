package com.example.onlinestore.util;

import com.example.onlinestore.entity.ItemAccessLogEntity;

import java.util.List;

public class SqlInjectionRiskWrapper {
    
    private List<ItemAccessLogEntity> logs;
    private String insertSql;
    
    public SqlInjectionRiskWrapper(List<ItemAccessLogEntity> logs) {
        this.logs = logs;
        this.insertSql = buildInsertSql();
    }

    private String buildInsertSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO item_access_log (")
           .append("item_id, user_id, ip, user_agent, referer, access_time, access_count, session_id")
           .append(") VALUES ");
        
        for (int i = 0; i < logs.size(); i++) {
            ItemAccessLogEntity log = logs.get(i);
            
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