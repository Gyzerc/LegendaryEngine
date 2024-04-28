package com.gyzer.legendaryrealms.Databse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public abstract class DataProvider {
    public abstract void init();
    public abstract void setHikarPool();
    public abstract void closeHikarPool();
    public void createDataTable(Connection connection, DatabaseTable table) {
        if (connection != null) {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.executeUpdate(table.getBuilder().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ResultSet> getDataStringResult(Connection connection, Builder builder, String target) {
        if (connection != null) {
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try {
                statement = connection.prepareStatement("SELECT * FROM " + builder.getTableName() + " WHERE `" + builder.getMainKey() + "` = '" + target + "' LIMIT 1;");
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    return Optional.of(resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public<T> void setData(Connection connection, Builder builder, String target, T... ts) {
        if (connection != null) {
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(builder.getInsertString(target));
                int a = 1;
                for (T t : ts) {
                    ps.setObject(a, t);
                    a++;
                }
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Builder {
        private String tableName;
        private String mainKey;
        private StringBuilder stringBuilder;
        private List<String> keys;

        public Builder(String tableName) {
            this.keys = new ArrayList<>();
            this.tableName = tableName;
            stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS "+tableName+" (");
        }

        public Builder addTextKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" TEXT DEFAULT NULL");
            keys.add(keyName);
            return this;
        }

        public Builder addUUIDKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" UUID DEFAULT NULL");
            keys.add(keyName);
            return this;
        }

        public Builder addBlobKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" BLOB DEFAULT NULL");
            keys.add(keyName);
            return this;
        }

        public Builder addIntegerKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` INTEGER NOT NULL");
            keys.add(keyName);
            return this;
        }

        public Builder addDoubleKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` DOUBLE NOT NULL");
            keys.add(keyName);
            return this;
        }
        public Builder addLongKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` LONG NOT NULL");
            keys.add(keyName);
            return this;
        }
        public Builder addVarcharKey(String keyName,int length){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` varchar("+length+") NOT NULL");
            keys.add(keyName);
            return this;
        }
        public Builder addBooleanKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` BOOLEAN NOT NULL");
            keys.add(keyName);
            return this;
        }
        public Builder build(String mainKey){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            this.mainKey = mainKey;
            stringBuilder.append("PRIMARY KEY (`"+mainKey+"`));");
            return this;
        }

        public String getTableName() {
            return tableName;
        }

        public String getMainKey() {
            return mainKey;
        }

        @Override
        public String toString(){
            return stringBuilder.toString();
        }

        public String getInsertString(String target) { //`
            StringBuilder main = new StringBuilder("REPLACE INTO "+tableName+" ");
            StringBuilder keys = new StringBuilder("(");
            StringBuilder keys_unknow = new StringBuilder("(");
            for (int i =0 ; i < this.keys.size() ; i ++) {
                keys.append("`").append(this.keys.get(i)).append("`");
                keys_unknow.append("?");
                if (i == this.keys.size() - 1 ) {
                    keys.append(")");
                    keys_unknow.append(")");
                    break;
                } else {
                    keys.append(",");
                    keys_unknow.append(",");
                }
            }
            main.append(keys).append(" VALUES ").append(keys_unknow);
            return main.toString();
        }

    }

}

