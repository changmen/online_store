package com.example.onlinestore.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Endpoint(id = "dbinfo")
public class DatabaseInfoEndpoint {

    private final DataSource dataSource;

    public DatabaseInfoEndpoint(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @ReadOperation
    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> dbInfo = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            dbInfo.put("url", metaData.getURL());
            dbInfo.put("driverName", metaData.getDriverName());
            dbInfo.put("driverVersion", metaData.getDriverVersion());
            dbInfo.put("username", metaData.getUserName());
            dbInfo.put("databaseProductName", metaData.getDatabaseProductName());
            dbInfo.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            
            List<Map<String, Object>> tables = new ArrayList<>();
            try (ResultSet rs = metaData.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    Map<String, Object> tableInfo = new HashMap<>();
                    String tableName = rs.getString("TABLE_NAME");
                    tableInfo.put("name", tableName);
                    tableInfo.put("type", rs.getString("TABLE_TYPE"));
                    tableInfo.put("schema", rs.getString("TABLE_SCHEM"));
                    tableInfo.put("catalog", rs.getString("TABLE_CAT"));
                    
                    List<Map<String, Object>> columns = new ArrayList<>();
                    try (ResultSet colRs = metaData.getColumns(conn.getCatalog(), null, tableName, "%")) {
                        while (colRs.next()) {
                            Map<String, Object> columnInfo = new HashMap<>();
                            columnInfo.put("name", colRs.getString("COLUMN_NAME"));
                            columnInfo.put("type", colRs.getString("TYPE_NAME"));
                            columnInfo.put("size", colRs.getInt("COLUMN_SIZE"));
                            columnInfo.put("nullable", colRs.getBoolean("IS_NULLABLE"));
                            columnInfo.put("defaultValue", colRs.getString("COLUMN_DEF"));
                            columns.add(columnInfo);
                        }
                    }
                    tableInfo.put("columns", columns);
                    
                    List<String> primaryKeys = new ArrayList<>();
                    try (ResultSet pkRs = metaData.getPrimaryKeys(conn.getCatalog(), null, tableName)) {
                        while (pkRs.next()) {
                            primaryKeys.add(pkRs.getString("COLUMN_NAME"));
                        }
                    }
                    tableInfo.put("primaryKeys", primaryKeys);
                    
                    tables.add(tableInfo);
                }
            }
            dbInfo.put("tables", tables);

        } catch (Exception e) {
            dbInfo.put("error", e.getMessage());
        }

        return dbInfo;
    }
} 