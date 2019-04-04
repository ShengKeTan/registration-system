package application;

import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


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
		Main.setChoseUI();
		//Event.fireEvent(Main.getPrimaryStage(),
	    		//new WindowEvent(Main.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST ));
	}
	private void logon() {
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String gotpassword = "";
		
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
		
		try {
			String sql;
			//parent or doctor
			if(ChoseController.flags == 1) {
				sql = "SELECT password from doctor WHERE name=?";
			}
			else {
				sql = "SELECT password from parent WHERE name=?";
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
			if(ChoseController.flags == 1) {
				Main.setDoctorUI();
			}
			else Main.setPatUI();
		}
		else {
			System.out.println("密码错误！");
		}
	}
	@FXML
	private void on_logon_click() {
		if(usrname.getText().equals("")) {
			//JOptionPane.showMessageDialog(null, "用户名不能为空，请输入用户名");
			System.out.println("用户名不能为空，请输入用户名");
			return;
		}
		if(password.getText().equals("")) {
			//JOptionPane.showMessageDialog(null, "密码不能为空，请输入密码");
			System.out.println("密码不能为空，请输入密码");
			return;
		}
		//check usrname and password
		logon();
	}	
}
