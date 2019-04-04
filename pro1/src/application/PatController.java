package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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
import impl.org.controlsfx.autocompletion.SuggestionProvider;

public class PatController implements Initializable{
	
	@FXML
	private Button quit, clear, ok;
	@FXML
	private ComboBox<String> reg_type;
	@FXML
	private TextField dep_name,  doc_name, reg_name;
	@FXML
	private TextField pay, needpay, get, num;
	
	//completions ctro
	private Set<String> autoCompletions;
	SuggestionProvider<String> provider;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//set type of autocomple
				autoCompletions = new HashSet<>(Arrays.asList("A","B","C"));
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
						
					}
				});
				//监视交款金额变化
				pay.textProperty().addListener((obs, oldText, newText)->{
					if(!pay.getText().equals("") && !needpay.getText().equals("")) {
						Double found = Double.parseDouble(pay.getText()) - Double.parseDouble(needpay.getText().substring(1));
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
		needpay.clear();
		get.clear();
		num.clear();
	}
	@FXML
	private void on_ok_click() {
		
	}
	

}
