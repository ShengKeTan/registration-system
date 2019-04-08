package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class RegnumController implements Initializable{
	
	@FXML Label time, regnum, dep, type, docname;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		/*time.setText(PatController.ptime);
		regnum.setText(PatController.pregnum);
		dep.setText(PatController.pdep);
		if(PatController.spe == 1) type.setText("专家号");
		else type.setText("普通号");
		docname.setText(PatController.pdocname);*/
	}
	
	@FXML
	public void on_ok_click() {
		time.setText("");
		regnum.setText("");
		dep.setText("");
		type.setText("");
		docname.setText("");
		Main.setPatUI();
	}

}
