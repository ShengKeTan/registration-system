package application;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ChoseController { 
	@FXML
	private Button doctorButton;
	@FXML
	private Button patientButton;
	
	@FXML
	private void on_doctorButton_clicked(){
		Stage tempStage = (Stage)patientButton.getScene().getWindow();
		tempStage.close();
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("doctorlogin.fxml"));

		AnchorPane root = new AnchorPane();
		Scene myScene = new Scene(root);
		try{
			myScene.setRoot((Parent)loader.load());
			Stage newStage = new Stage();
			newStage.setTitle("医生登陆");
			newStage.setScene(myScene);
			newStage.show();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	@FXML
	private void on_patientButton_clicked(){
		Stage tempStage = (Stage)patientButton.getScene().getWindow();
		tempStage.close();
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("patientlogin.fxml"));

		AnchorPane root = new AnchorPane();
		Scene myScene = new Scene(root);
		try{
			myScene.setRoot((Parent)loader.load());
			Stage newStage = new Stage();
			newStage.setTitle("病人登陆");
			newStage.setScene(myScene);
			newStage.show();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
}
