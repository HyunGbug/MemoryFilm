package com.smile.www.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBUtil {
    public static Connection getConnection() throws SQLException {
        try {
            Context initContext = new InitialContext();
            DataSource dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/project");
            return dataSource.getConnection();
        } catch (NamingException e) {
            throw new SQLException("DB 연결 오류", e);
        }
    }
}
