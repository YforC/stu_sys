package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db_connection {

    /**
     * 数据库连接类
     */
    // 数据库连接信息
    private static final String URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=Asia/Shanghai"; // 替换为你的数据库URL
    // 替换为你的数据库名称
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 关闭数据库连接
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 测试数据库连接
    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            System.out.println("数据库连接成功！");
            closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
