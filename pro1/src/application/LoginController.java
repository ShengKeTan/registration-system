package application;

import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class LoginController {
	
	@FXML
	private TextField usrname;
	@FXML
	private PasswordField password;
	
	@FXML
	private Button back;
	@FXML
	private Button logon;
	
	@FXML
	private void on_back_click() {
		Stage tempStage = (Stage)back.getScene().getWindow();
		tempStage.close();
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("chose.fxml"));

		AnchorPane root = new AnchorPane();
		Scene myScene = new Scene(root);
		try{
			myScene.setRoot((Parent)loader.load());
			Stage newStage = new Stage();
			newStage.setTitle("身份认证");
			newStage.setScene(myScene);
			newStage.show();
		}catch(IOException ex){
			ex.printStackTrace();
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
		
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
		
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String gotpassword = "";
		
		try {
			pStatement = (PreparedStatement)mycon.prepareStatement("SELECT password from doctor WHERE name=?");
			pStatement.setString(1, usrname.getText().trim());
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			if(rs != null) {
				rs.next();
			}
			else {
				System.out.println("账号不存在！");
			}
			gotpassword = rs.getString("password").trim();
			
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		
		if(password.getText().trim().equals(gotpassword)) {
			System.out.printf("欢迎您！%s医生\n",usrname.getText());
			Stage tempStage = (Stage)back.getScene().getWindow();
			tempStage.close();
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctorwin.fxml"));

			AnchorPane root = new AnchorPane();
			Scene myScene = new Scene(root);
			try{
				myScene.setRoot((Parent)loader.load());
				Stage newStage = new Stage();
				newStage.setTitle("医生管理客户端");
				newStage.setScene(myScene);
				newStage.show();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		else {
			System.out.println("密码错误！");
		}
		
	}
	
}
