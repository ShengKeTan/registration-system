package application;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.control.textfield.TextFields;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import impl.org.controlsfx.autocompletion.SuggestionProvider;

public class PatController implements Initializable{
	
	static int spe = 1;
	static String newba;
	//pass info to print
	//static String ptime = null;
	static String pregnum = null;
	/*static String pdep = null;
	static String pdocname = null;*/
	
	@FXML
	private Button quit, clear, ok;
	@FXML
	private ChoiceBox<String> reg_type;
	@FXML
	private TextField dep_name,  doc_name, reg_name;
	@FXML
	private TextField pay, need_pay, get, num;
	
	//completions ctro
	private Set<String> autoCompletions;
	SuggestionProvider<String> provider;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		autoCompletions = new HashSet<>(Arrays.asList());
		provider = SuggestionProvider.create(autoCompletions);
		TextFields.bindAutoCompletion(doc_name, provider);
		//connect to mysql
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
				
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		LinkedList<String>searchResult = new LinkedList<>();
		//get department inifo
		try {
			String sql = "SELECT * FROM department";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1){
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				String info1 = rs.getString("depid").trim();
				String info2 = rs.getString("name").trim();
				String info3 = rs.getString("py").trim();
				String info = info1 + " " + info2 + " " + info3;
				System.out.println(info);
				searchResult.add(info);
				}
			TextFields.bindAutoCompletion(dep_name, searchResult);
			}catch(SQLException e1){
				e1.printStackTrace();
				}
				//close connection
		try {
				mycon.close();
			}catch(SQLException e1){
				e1.printStackTrace();
			}
				
		//监视号种输入变化
		reg_type.getSelectionModel().selectedIndexProperty().addListener(new 
				ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number>observable, Number oldValue, Number
							newValue) {
							System.out.println(newValue);
							spe = (int)newValue;
							System.out.println(reg_type.getValue());
							on_regname_click();
						}
				});
		//监视交款金额变化
		pay.textProperty().addListener((obs, oldText, newText)->{
					if(!pay.getText().equals("") && !need_pay.getText().equals("")) {
						Double found = Double.parseDouble(pay.getText()) - Double.parseDouble(need_pay.getText().substring(1));
						if(found >= 0.0) {
							get.setText(Double.toString(found));
						}
						else {
							get.setText("");
						}
					}
					else {
						get.setText("");
					}
				});
		//监视科室名称变化
		dep_name.textProperty().addListener((obs, oldText, newText)->{
			System.out.println(newText);
			doc_name.clear();
			reg_name.clear();
			enter_docname();
			on_depname_click();
		});
	}
	
	private void on_depname_click() {
		if(dep_name.getText().length() > 3) {
			System.out.println(dep_name.getText());
			on_regname_click();
		}
	}
	
	@FXML
	private void enter_docname() {
		//connect to mysql
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
		//prepare to get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		LinkedList<String>searchResult = new LinkedList<>();
		String sql = null;
		try {
			if(dep_name.getText().length() > 3) {
				String temp = "SELECT * FROM doctor WHERE depid = '%1$s'AND speciallist=%2$d";
				sql = String.format(temp, dep_name.getText().substring(0, 1), spe);
				System.out.println(sql);
			}
			else {
				sql = "SELECT * FROM doctor";
			}
			//get info
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				String info1 = rs.getString("docid").trim();
				String info2 = rs.getString("name").trim();
				//pdocname = info2;
				String info3 = rs.getString("py").trim();
				String info = info1 + " " + info2 + " " + info3;
				System.out.println(info);
				searchResult.add(info);
			}
			Set<String> docAutoCompletions = new HashSet<>(searchResult);
			provider.clearSuggestions();
			provider.addPossibleSuggestions(docAutoCompletions);
			//TextFields.bindAutoCompletion(doc_name, searchResult);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1){
			e1.printStackTrace();
		}
	}
	
	@FXML
	private void on_regname_click() {
		//connect to mysql
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
		//prepare to get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		LinkedList<String>searchResult = new LinkedList<>();
		String sql = null;
		try {
			if(dep_name.getText().length() > 3) {
				String temp = "SELECT * FROM register_category WHERE depid = '%1$s'AND speciallist='%2$d'";
				sql = String.format(temp, dep_name.getText().substring(0, 1), spe);
				System.out.println(sql);
			}
			else {
				sql = "SELECT * FROM register_category";
				System.out.println(sql);
			}
			//get info
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1){
			e1.printStackTrace();
		}
		double need2pay = 0;
		try {
			rs = pStatement.executeQuery(); 
			String price = null;
			String info = null;
			while(rs.next()) {
				String info1 = rs.getString("catid").trim();
				String info2 = rs.getString("name").trim();
				String info3 = rs.getString("py").trim();
				info = info1 + " " + info2 + " " + info3;
				searchResult.add(info);
				//get price
				price = rs.getString("reg_fee").trim();
				need2pay = Double.parseDouble(price);
			}
			reg_name.setText(info);
			need_pay.setText("$" + price);
		}catch(SQLException e1) {
			e1.printStackTrace();
			}
		//够不够交
		String Patient = LoginController.logonID;
		System.out.println(Patient);
		try {
			sql = "SELECT balance FROM patient WHERE name=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1, Patient);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			double v = 0;
			while(rs.next()) {
				String value = rs.getString("balance").trim();
				v = Double.parseDouble(value);
				System.out.println(value);
			}
			if(v > need2pay) {
				double zero = 0.0;
				String str1 = "账户余额：" + Double.toString(v - zero) + "元";
				pay.setText(str1);
				//不允许再次编辑
				pay.setEditable(false);
				newba = Double.toString(v - need2pay);
				get.setText("支付后，余额：" + Double.toString(v - need2pay) + "元");
				get.setEditable(false);
			}
			else {
				pay.setEditable(true);
				get.setEditable(true);
				pay.clear();
				get.clear();
			}
			
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1){
			e1.printStackTrace();
		}
	}
	
	@FXML
	private void on_quit_click() {
		on_clear_click();
		Main.setChoseUI();
	}
	@FXML
	private void on_clear_click() {
		dep_name.clear();
		doc_name.clear();
		reg_name.clear();
		pay.clear();
		need_pay.clear();
		get.clear();
		//num.clear();
	}
	@FXML
	private void on_ok_click() {
		//pdep = dep_name.getText().substring(2, 5);
		//connect to mysql
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
		//pre info
		String regnum = null;  //reg num
		String regid = reg_name.getText().substring(0, 1);  //reg id
		String docid = doc_name.getText().substring(0, 1);	//doc id
		String patid = LoginController.ID;
		BigDecimal cost = new BigDecimal(need_pay.getText().substring(1));
		Date dNow = new Date();
		SimpleDateFormat regtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = regtime.format(dNow);
		//ptime = time;
		//read from dbs
		int totalnum = 0;
		int maxnum = 0;
		boolean out = true;
		//get or insert info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "SELECT MAX(register.current_reg_count) FROM register, register_category"
					+ " WHERE register_category.catid = register.catid AND register_category.catid='%1$s'";
			sql = String.format(sql, regid);
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			System.out.println(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				totalnum = rs.getInt("MAX(register.current_reg_count)");
			}	
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			sql = "SELECT max_reg_number FROM register_category WHERE register_category.catid='%1$s'";
			sql = String.format(sql, regid);
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			System.out.println(sql);
			rs = pStatement.executeQuery();
			while(rs.next()) {
				maxnum = rs.getInt("register_category.max_reg_number");
				System.out.println(totalnum + " " + maxnum);
				if(maxnum <= totalnum) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText("挂号失败！");
					alert.setContentText("该号种挂号人数已达上限");
					alert.showAndWait();
					System.out.println("挂号人数已满，挂号失败！");
					return;
				}
				//success
				totalnum++;
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		//generated regnum
		try {
			sql = "SELECT COUNT(*) FROM register";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String getnum = null;
			while(rs.next()) {
				getnum = rs.getString("COUNT(*)").trim();
			}
			int getnumint = (int)Double.parseDouble(getnum) + 1;
			regnum = String.format("%06d", getnumint);
			pregnum = regnum;
			System.out.println(regnum);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		//insert reg info
		try {
			sql = "INSERT INTO register VALUES(?,?,?,?,?,?,?,?)";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			System.out.println(sql);
			pStatement.setString(1, regnum);
			System.out.println(sql);
			pStatement.setString(2, regid);
			pStatement.setString(3, docid);
			pStatement.setString(4, patid);
			pStatement.setInt(5, totalnum);
			pStatement.setBoolean(6, out);
			pStatement.setBigDecimal(7, cost);
			pStatement.setString(8, time);
			System.out.println(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				System.out.println("挂号成功");
				num.setText(regnum);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("挂号成功！");
				alert.setContentText("编号" + regnum);
				alert.showAndWait();
				System.out.println(regnum);
				//on_clear_click();
			}
			else {
				System.out.println("挂号失败");
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			sql = "UPDATE patient SET balance=? WHERE name=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1, newba);
			pStatement.setString(2, LoginController.logonID);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pStatement.executeUpdate();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1){
			e1.printStackTrace();
		}
	}
	@FXML
	private void on_regback_click() {
	
		Main.setRegnumUI();
	}
}
