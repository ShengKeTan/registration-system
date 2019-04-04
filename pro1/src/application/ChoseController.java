package application;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


public class ChoseController implements Initializable{ 
	static int flags = 1;
	@FXML
	private Button doctorButton;
	@FXML
	private Button patientButton;
	
	@FXML
	private void on_doctorButton_clicked(){
		flags = 1;  //flags of doctor
		Main.setLoginUI();
	}

	@FXML
	private void on_patientButton_clicked(){
		flags = 0;  //flags of patient
		Main.setLoginUI();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
