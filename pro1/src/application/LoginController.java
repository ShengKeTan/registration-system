package application;

import javafx.scene.control.*;
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
	
	@FXML
	private TextField usrname;
	@FXML
	private PasswordField password;
	
	@FXML
	private Button back;
	@FXML
	private Button logon;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	private void on_back_click() {
		usrname.clear();
		password.clear();
		Main.setChoseUI();
		//Event.fireEvent(Main.getPrimaryStage(),
	    		//new WindowEvent(Main.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST ));
	}
	
	private void logon() {
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String gotpassword = "";
		
		//connect to mysql
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
		
		try {
			String sql;
			//parent or doctor
			if(ChoseController.flags == 1) {
				sql = "SELECT password from doctor WHERE name=?";
			}
			else {
				sql = "SELECT password from patient WHERE name=?";
			}
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1, usrname.getText().trim());
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			if(rs != null) {
				rs.next();
			}
			gotpassword = rs.getString("password").trim();
			
		}catch(SQLException e1) {
			System.out.println("账号不存在！");
			e1.printStackTrace();
		}
		
		if(password.getText().trim().equals(gotpassword)) {
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
				String up = String.format(temp, currentTime, usrname.getText().trim());
				pStatement = (PreparedStatement)mycon.prepareStatement(up);
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
			System.out.println("密码错误！");
		}
		//close connection
		try {
			usrname.clear();
			password.clear();
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	@FXML
	private void on_logon_click() {
		if(usrname.getText().equals("")) {
			System.out.println("用户名不能为空，请输入用户名");
			return;
		}
		if(password.getText().equals("")) {
			System.out.println("密码不能为空，请输入密码");
			return;
		}
		//check usr name and password
		logon();
	}	
}
