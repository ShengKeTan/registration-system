package application;

import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.Connection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;



public class LoginController  implements Initializable{
	
	static String logonID;
	static String ID;
	
	@FXML
	private TextField usrname;
	@FXML
	private PasswordField password;
	
	@FXML
	private Button back;
	@FXML
	private Button logon;
	@FXML
	private Label la;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		la.setTextFill(Color.web("#0076a3"));
	}
	
	@FXML
	private void on_back_click() {
		usrname.clear();
		password.clear();
		la.setText("");
		Main.setChoseUI();
	}
	
	private void logon() {
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String gotpassword = "";
		//null usr flags
		int flag = 0;
		
		//connect to mysql
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
		
		try {
			String sql;
			//parent or doctor
			if(ChoseController.flags == 1) {
				sql = "SELECT * from doctor WHERE name=?";
			}
			else {
				sql = "SELECT * from patient WHERE name=?";
			}
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1, usrname.getText().trim());
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				gotpassword = rs.getString("password").trim();
				flag = 1;
				if(ChoseController.flags == 1) ID = rs.getString("docid").trim();
				else ID = rs.getString("pid").trim();
			}
			
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		if(password.getText().trim().equals(gotpassword)) {
			logonID = usrname.getText().trim();
			la.setText("");
			usrname.clear();
			password.clear();
			//update datetime
			Date dnow = new Date();
			SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = t.format(dnow);
			try {
				String temp;
				//parent or doctor
				if(ChoseController.flags == 1) {
					temp = "UPDATE doctor SET last_login_datetime = '%1$s' WHERE name = '%2$s'";
				}
				else {
					temp = "UPDATE patient SET last_login_datetime = '%1$s' WHERE name = '%2$s'";
				}
				String up = String.format(temp, currentTime, logonID);
				pStatement = (PreparedStatement)mycon.prepareStatement(up);
				System.out.println(temp);
				System.out.println(currentTime);
				System.out.println(logonID);
			}catch(SQLException e1) {
				e1.printStackTrace();
			}
			try {
				pStatement.executeUpdate();
			}catch(SQLException e1) {
				e1.printStackTrace();
			}
			//change scene
			if(ChoseController.flags == 1) {
				Main.setDoctorUI();
			}
			else Main.setPatUI();
		}
		else {
			if(flag == 0) la.setText("用户不存在！");
			else la.setText("密码错误！");
			System.out.println("密码错误！");
		}
		//close connection
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	@FXML
	private void on_logon_click() {
		if(usrname.getText().equals("")) {
			la.setText("用户名不能为空，请输入用户名");
			System.out.println("用户名不能为空，请输入用户名");
			return;
		}
		if(password.getText().equals("")) {
			la.setText("密码不能为空，请输入密码");
			System.out.println("密码不能为空，请输入密码");
			return;
		}
		//check usr name and password
		logon();
	}	
}
