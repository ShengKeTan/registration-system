package application;

import java.sql.*;
import java.sql.Connection;

public class ContoMysql {
	public Connection connect2mysql() {
		Connection mycon = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			////连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
			mycon = (Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true", "root", "tsk1998810");
			if(mycon != null) {
				System.out.println("Successfully connected!");
		
			}
			return mycon;
		}catch(Exception ex) {
			System.out.println("connected error!");
		}
		return mycon;
	}

}
