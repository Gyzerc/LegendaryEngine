package com.gyzer.legendaryrealms.Databse;

public class DatabaseTable {
    private String tableName;
    private DataProvider.Builder builder;

    public DatabaseTable(String tableName, DataProvider.Builder builder) {
        this.tableName = tableName;
        this.builder = builder;
    }

    public String getTableName() {
        return tableName;
    }

    public DataProvider.Builder getBuilder() {
        return builder;
    }
}
