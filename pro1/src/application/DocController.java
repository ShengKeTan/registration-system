package application;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DocController implements Initializable{
	
	@FXML
	private Button quit;
	@FXML
	private DatePicker date_end,date_begin;
	//pat info view
	private ObservableList<Patientpage> patdate = FXCollections.observableArrayList();
	@FXML private TableView<Patientpage> patinfo;
	@FXML private TableColumn<Patientpage, String> catid, patname, time, type, ureg;
	//doc income view
	private ObservableList<Income> getdate = FXCollections.observableArrayList();
	@FXML private TableView<Income> incominfo;
	@FXML private TableColumn<Income, String> depname, docid, docname, regtype, number, income;
	
	LocalDate datetmp; 
	LocalDate datetmp2; 
	String time_begin,time_end;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//StringExpression cellData;
		catid.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getCatid()));
		patname.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getPatname()));
		time.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getTime()));
		type.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getType()));
		ureg.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getUreg()));
		depname.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDepname()));
		docid.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDocid()));
		docname.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDocname()));
		regtype.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getType()));
		number.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getNumber()));
		income.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getIncom()));
		date_end.setValue(LocalDate.now());
		date_begin.setValue(LocalDate.now());
		//对日期选择时间进行监听
		date_begin.setOnAction(event->{
			patdate.clear();
			getdate.clear();
			showPatinfo();
			showIncome();
		});
		date_end.setOnAction(event->{
			patdate.clear();
			getdate.clear();
			showPatinfo();
			showIncome();
		});
	}
	@FXML
	public void showPatinfo() {
		//get time
		//time_begin = date_begin.getValue().toString();
		//time_end = date_end.getValue().toString();
		patdate.clear();
		if(date_begin == null || date_end == null) {
			time_begin = LocalDate.now().toString();
			time_end = LocalDate.now().toString();
		}
		else {
			datetmp = date_begin.getValue();
			datetmp2 = date_end.getValue();
		}
		if(datetmp==null || datetmp2==null)
		{
			time_begin = LocalDate.now().toString();
			time_end = LocalDate.now().toString();
		}
		else
		{
			time_begin = datetmp.toString();
			time_end = datetmp2.toString();
		}
		time_begin += " 00:00:00";
		time_end += " 23:59:59";
		//connect to mysql
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "SELECT * FROM register, patient, doctor, register_category"
					+ " WHERE register.pid=patient.pid" 
					+ " AND register.catid=register_category.catid"
					+ " AND register.docid=doctor.docid"
					+ " AND register.reg_datetime>='%1$s'"
					+ " AND register.reg_datetime<='%2$s'"
					+ " AND register.docid='%3$s'";
			String tt = String.format(sql, time_begin, time_end, LoginController.ID);
			pStatement = (PreparedStatement)mycon.prepareStatement(tt);
			//System.out.println(LoginController.ID);
			//System.out.println(tt);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String catid = null;
			String name = null;
			String time = null;
			String type = null;
			String unreg = null;
			while(rs.next()) {
				catid = rs.getString("reg_id");
				name = rs.getString("patient.name");
				time = rs.getString("reg_datetime");
				//unreg = rs.getString("register.unreg");
				if(rs.getInt("register_category.speciallist") == 1) {
					type = "专家号";
				}
				else type = "普通号";
				if(rs.getString("register.unreg").equals("1")) {
					unreg = "未退";
				}
				else unreg = "已退";
				System.out.println(catid + name + time + type + unreg);
				patdate.add(new Patientpage(catid,name,time,type,unreg));
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		patinfo.setItems(patdate);
		getdate.clear();
	}
	@FXML
	private void showIncome() {
		getdate.clear();
		//connect to mysql
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
				
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "SELECT department.name, doctor.docid, doctor.name,"
					+ " register_category.speciallist, COUNT(*), register.reg_fee,"
					+ " MIN(register.reg_datetime), MAX(register.reg_datetime)"
					+ " FROM register, doctor, register_category, department"
					+ " WHERE "
					+ " doctor.depid=department.depid"
					+ " AND register.docid=doctor.docid"
					+ " AND register_category.catid=register.catid"
					+ " AND register.unreg='1'"
					+ " GROUP BY doctor.docid, register_category.speciallist, register.reg_fee";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			//System.out.println(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String pname = null;
			String did = null;
			String dname = null;
			String rtype = null;
			String total = null;
			String get = null;
			//String beTime = null;
			//String endTime = null;
			while(rs.next()) {
				//beTime = rs.getString("MIN(register.reg_datetime)");
				//endTime = rs.getString("MAX(register.reg_datetime)");
				pname = rs.getString("department.name").trim();
				did = rs.getString("doctor.docid").trim();
				dname = rs.getString("doctor.name").trim();
				int inttotal = rs.getInt("COUNT(*)");
				BigDecimal regFee = rs.getBigDecimal("register.reg_fee");
				double regifee = regFee.doubleValue();
				double totalfee = inttotal*regifee;
				total = String.valueOf(inttotal);
				get = String.valueOf(totalfee);
				if(rs.getInt("register_category.speciallist") == 1) {
					rtype = "专家号";
				}
				else rtype = "普通号";
				System.out.println(pname + did + dname + total + get + rtype);
				getdate.add(new Income(pname,did,dname,rtype,total,get));
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		incominfo.setItems(getdate);
		patdate.clear();
	}
	
	@FXML
	private void on_quit_click() {
		Main.setChoseUI();
	}
	

}
