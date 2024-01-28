package com.dlabeling.labeling.generate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/24
 */
@Component
public class DBManager {

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void execute(String sql){
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void execute(String dbName, String sql) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setCatalog(dbName);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        boolean execute = preparedStatement.execute();
        ResultSetMetaData metaData = preparedStatement.getMetaData();

    }

    public void updateLabelInfo(){

    }

    public void createTable(String createTableSql){
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(createTableSql);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSetMetaData select(String tableName, String sql){
        return null;
    }
}
